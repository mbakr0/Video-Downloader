package online.mohmedbakr.downloadvideos.core

import online.mohmedbakr.downloadvideos.database.VideoInfo
import online.mohmedbakr.downloadvideos.model.VideoItemUi

fun VideoInfo.asVideoItemUi() = VideoItemUi(
    urlVideo = urlVideo,
    cover = cover,
    title = title,
    platform = platform,
    username = username,
)

fun String.getPlatform(): Platform = Platform.valueOf(this)