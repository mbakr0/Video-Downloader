package online.mohmedbakr.downloadvideos.repository.videoRepository

import io.reactivex.Single
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.model.instagramModel.InstagramRoot
import online.mohmedbakr.downloadvideos.model.mapper.ListMapperImpl
import online.mohmedbakr.downloadvideos.model.mapper.fromJsonToPlatform.JSONToInstagram
import online.mohmedbakr.downloadvideos.model.mapper.fromPlatformToVideoInfo.InstagramMapper
import online.mohmedbakr.downloadvideos.network.ApiClient
import online.mohmedbakr.downloadvideos.repository.LocalVideoRepository
import online.mohmedbakr.downloadvideos.repository.RemoteVideoRepository.Companion.DOWNLOAD_VIDEOS_MESSAGE

class InstagramRepository(private val apiClient: ApiClient, dataBase: VideoDataBase) {
    private var instagramMaxCursor: String? = null
    private val localVideoRepository = LocalVideoRepository(dataBase)
    fun getRemoteVideos(username: String): Single<String> {
        return apiClient.getInstagramVideos(username,instagramMaxCursor)
            .doOnSuccess {
                val videos : InstagramRoot = JSONToInstagram().map(it)
                instagramMaxCursor = videos.endCursor
                val mapper = ListMapperImpl(InstagramMapper(username))
                localVideoRepository.insertAll(mapper.map(videos.videoList))
            }
            .map { DOWNLOAD_VIDEOS_MESSAGE }
    }
}