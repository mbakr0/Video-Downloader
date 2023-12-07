package online.mohmedbakr.downloadvideos.repository.videoRepository

import io.reactivex.Single
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.model.mapper.ListMapperImpl
import online.mohmedbakr.downloadvideos.model.mapper.fromJsonToPlatform.JSONToYoutube
import online.mohmedbakr.downloadvideos.model.mapper.fromJsonToPlatform.JSONToYoutubeUrl
import online.mohmedbakr.downloadvideos.model.mapper.fromPlatformToVideoInfo.YoutubeMapper
import online.mohmedbakr.downloadvideos.model.youtubeModel.YoutubeRoot
import online.mohmedbakr.downloadvideos.network.ApiClient
import online.mohmedbakr.downloadvideos.repository.LocalVideoRepository
import online.mohmedbakr.downloadvideos.repository.RemoteVideoRepository.Companion.DOWNLOAD_VIDEOS_MESSAGE

class YoutubeRepository(private val apiClient: ApiClient, dataBase: VideoDataBase) {
    private var youtubeContinuation: String? = null
    private val localVideoRepository = LocalVideoRepository(dataBase)
    fun getRemoteVideos(username: String) :Single<String>{
       return apiClient.getYoutubeVideos(username,youtubeContinuation)
            .doOnSuccess { result ->
                val videos : YoutubeRoot = JSONToYoutube().map(result)
                youtubeContinuation = videos.continuation
                val mapper = ListMapperImpl(YoutubeMapper(username))
                localVideoRepository.insertAll(mapper.map(videos.videoList))
            }.doOnError {
           }
            .map { DOWNLOAD_VIDEOS_MESSAGE }

    }
    fun getVideoUrl(id:String): Single<String> {
        return apiClient.getYoutubeVideoLink(id)
            .map { JSONToYoutubeUrl().map(it) }
            .doOnSuccess {
               localVideoRepository.updateUrlVideo(id,it)
            }
    }
}