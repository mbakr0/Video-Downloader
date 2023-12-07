package online.mohmedbakr.downloadvideos.repository

import android.app.DownloadManager
import androidx.core.net.toUri
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import online.mohmedbakr.downloadvideos.MainActivity
import online.mohmedbakr.downloadvideos.model.VideoItemUi
import java.io.File

class DownLoadVideoRepository(private val downloadManager: DownloadManager, private var file: File) {

    fun downloadFileObservable(video: VideoItemUi): Single<String> {
        return  Single.create<String> { emitter->
            runCatching { openStream(video) }
                .onFailure { emitter.onError(it) }
                .onSuccess { emitter.onSuccess("Video downloaded successfully") } }
            .subscribeOn(Schedulers.io())
    }

    private fun openStream(video: VideoItemUi) {
        video.inProgress.value = true
        val file = File(file, "${video.title}.mp4")
        val request = DownloadManager.Request(video.urlVideo.toUri())
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setMimeType("video/mp4")
            .setTitle(video.title)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setDestinationUri(file.toUri())
        val downloadID = downloadManager.enqueue(request)
        var finishDownload = false
        while (!finishDownload) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
            if (cursor.moveToFirst()) {
                val columnTotal = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                val totalSize = cursor.getLong(columnTotal).toFloat()
                if (video.maxValue.value <= 0f)
                    video.maxValue.value = totalSize
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                when (cursor.getInt(columnIndex)) {
                    DownloadManager.STATUS_FAILED or DownloadManager.STATUS_PENDING or DownloadManager.STATUS_PAUSED-> {
                        finishDownload = true
                        video.inProgress.value = false
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        video.currentValue.value = totalSize
                        finishDownload = true
                        video.inProgress.value = false
                    }
                    DownloadManager.STATUS_RUNNING -> {
                        if (totalSize >= 0) {
                            val columnDownloaded =
                                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                            video.currentValue.value = cursor.getLong(columnDownloaded).toFloat()
                        }
                    }
                }
            }
        }
    }

    fun createFile(name: String) = File(MainActivity.downloadFile, name).apply {
        mkdir()
        file = this
    }
}