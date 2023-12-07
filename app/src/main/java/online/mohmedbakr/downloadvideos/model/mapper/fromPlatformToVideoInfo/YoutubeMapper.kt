package online.mohmedbakr.downloadvideos.model.mapper.fromPlatformToVideoInfo

import online.mohmedbakr.downloadvideos.core.Platform
import online.mohmedbakr.downloadvideos.database.VideoInfo
import online.mohmedbakr.downloadvideos.model.mapper.Mapper
import online.mohmedbakr.downloadvideos.model.youtubeModel.YoutubeVideo

class YoutubeMapper (private val username:String): Mapper<YoutubeVideo,VideoInfo> {
    override fun map(input: YoutubeVideo): VideoInfo {
        return VideoInfo(
            platform = Platform.Youtube,
            username = username,
            urlVideo = input.videoId,
            cover = input.cover,
            title = input.title
        )
    }
}