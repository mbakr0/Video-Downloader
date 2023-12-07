package online.mohmedbakr.downloadvideos.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import online.mohmedbakr.downloadvideos.core.Platform
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.network.ApiClient
import online.mohmedbakr.downloadvideos.repository.videoRepository.InstagramRepository
import online.mohmedbakr.downloadvideos.repository.videoRepository.TiktokRepository
import online.mohmedbakr.downloadvideos.repository.videoRepository.YoutubeRepository

class RemoteVideoRepository(database:VideoDataBase, apiClient: ApiClient) {

    private val instagramRepository = InstagramRepository(apiClient,database)
    private val tiktokRepository = TiktokRepository(apiClient,database)
    private val youtubeRepository = YoutubeRepository(apiClient,database)
    companion object {
        const val DOWNLOAD_VIDEOS_MESSAGE = "Videos downloaded successfully"
        const val URL_UPDATED_MESSAGE = "Url Updated successfully"
    }
    fun getRemoteVideos(platform: Platform, username: String) = when(platform){
        Platform.Instagram -> instagramRepository.getRemoteVideos(username)
        Platform.Tiktok -> tiktokRepository.getRemoteVideos(username)
        Platform.Youtube -> youtubeRepository.getRemoteVideos(username)
    }

    fun getYoutubeUrl(id: String): Single<String> {
        return youtubeRepository.getVideoUrl(id)
            .subscribeOn(Schedulers.io())
    }

}