package online.mohmedbakr.downloadvideos.model.mapper.fromPlatformToVideoInfo

import online.mohmedbakr.downloadvideos.core.Platform
import online.mohmedbakr.downloadvideos.database.VideoInfo
import online.mohmedbakr.downloadvideos.model.mapper.Mapper
import online.mohmedbakr.downloadvideos.model.tiktokModel.TiktokVideo

class TiktokMapper(private val username:String) :Mapper<TiktokVideo,VideoInfo> {
    override fun map(input: TiktokVideo): VideoInfo {
        return VideoInfo(
            platform = Platform.Tiktok,
            username = username,
            urlVideo = input.urlVideo,
            cover = input.cover,
            title = input.title
        )
    }
}