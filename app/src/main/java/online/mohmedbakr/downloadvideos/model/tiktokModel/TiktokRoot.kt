package online.mohmedbakr.downloadvideos.model.tiktokModel

import com.google.gson.annotations.SerializedName

data class TiktokRoot(
    val videos: List<TiktokVideo>,
    @SerializedName("max_cursor")
    val maxCursor: Long
)
