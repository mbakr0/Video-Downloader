package online.mohmedbakr.downloadvideos.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import online.mohmedbakr.downloadvideos.core.Platform

@Entity
data class VideoInfo(
    @PrimaryKey(autoGenerate = true) val vId: Int = 0,
    @ColumnInfo(name = "url_video") val urlVideo: String,
    val cover: String,
    val title: String,
    val username:String,
    val platform: Platform,
    val date:Long = System.currentTimeMillis()
)
