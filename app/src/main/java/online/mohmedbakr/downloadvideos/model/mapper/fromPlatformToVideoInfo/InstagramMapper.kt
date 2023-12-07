package online.mohmedbakr.downloadvideos.model.mapper.fromPlatformToVideoInfo

import online.mohmedbakr.downloadvideos.core.Platform
import online.mohmedbakr.downloadvideos.database.VideoInfo
import online.mohmedbakr.downloadvideos.model.instagramModel.InstagramVideo
import online.mohmedbakr.downloadvideos.model.mapper.Mapper

class InstagramMapper(private val username:String) : Mapper<InstagramVideo, VideoInfo> {
    override fun map(input: InstagramVideo): VideoInfo {
        return VideoInfo(
            platform = Platform.Instagram,
            username = username,
            urlVideo = input.urlVideo,
            cover = input.cover,
            title = input.title
        )
    }
}