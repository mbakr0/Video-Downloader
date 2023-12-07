package online.mohmedbakr.downloadvideos.model.tiktokModel

import com.google.gson.annotations.SerializedName

data class TiktokVideo(
    val row: Long,
    @SerializedName("url_video")
    val urlVideo: String,
    val cover: String,
    val title: String,
    @SerializedName("play_count")
    val playCount: Long,
)
