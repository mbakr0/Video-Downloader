package online.mohmedbakr.downloadvideos.repository

import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.database.VideoInfo
import java.net.HttpURLConnection
import java.net.URL




class LocalVideoRepository(dataBase: VideoDataBase) {
    private val videoDao = dataBase.getVideoDao()
    fun insertAll(list: List<VideoInfo>){
        videoDao.insertAll(list)
    }

    fun updateUrlVideo(oldUrl:String,newUrl:String) {
        videoDao.updateUrl(oldUrl,newUrl)
    }

    fun getAllLocalVideos(): LiveData<List<VideoInfo>> {
        return videoDao.getAllVideos()
    }

    private fun deleteAllVideo(list: List<VideoInfo>?){
       list?.let {  videoDao.deleteAll(list) }
    }


    fun refreshDatabase(){
        val list = getAllLocalVideos().value?.filter {
            URLUtil.isValidUrl(it.urlVideo)
        }?.filter {
            isWorkingUrl(it.cover) || isWorkingUrl(it.urlVideo)
        }
        deleteAllVideo(list)
    }

    private fun isWorkingUrl(url: String): Boolean {
        val connection = URL(url).openConnection() as HttpURLConnection
        val responseCode = connection.responseCode
        return responseCode == HttpURLConnection.HTTP_OK
    }


}