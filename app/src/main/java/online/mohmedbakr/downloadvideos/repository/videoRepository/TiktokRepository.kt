package online.mohmedbakr.downloadvideos.repository.videoRepository

import io.reactivex.Single
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.model.mapper.ListMapperImpl
import online.mohmedbakr.downloadvideos.model.mapper.fromPlatformToVideoInfo.TiktokMapper
import online.mohmedbakr.downloadvideos.network.ApiClient
import online.mohmedbakr.downloadvideos.repository.LocalVideoRepository
import online.mohmedbakr.downloadvideos.repository.RemoteVideoRepository.Companion.DOWNLOAD_VIDEOS_MESSAGE

class TiktokRepository(private val apiClient: ApiClient, dataBase: VideoDataBase) {
    private var tiktokMaxCursor = "0"
    private val localVideoRepository = LocalVideoRepository(dataBase)
    fun getRemoteVideos(username: String): Single<String> =
        apiClient.getTiktokVideos("@$username",tiktokMaxCursor)
             .doOnSuccess {
                 tiktokMaxCursor = it.maxCursor.toString()
                 val mapper = ListMapperImpl(TiktokMapper(username))
                 localVideoRepository.insertAll(mapper.map(it.videos))
             }
            .map {
                DOWNLOAD_VIDEOS_MESSAGE
            }

}