package online.mohmedbakr.downloadvideos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDao {

    @Query("SELECT * FROM videoinfo")
    fun getAllVideos():LiveData<List<VideoInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<VideoInfo>)

    @Delete
    fun deleteAll(list: List<VideoInfo>)

    @Query("UPDATE VideoInfo SET url_video = :newUrl WHERE url_video = :oldUrl")
    fun updateUrl(oldUrl:String,newUrl: String)
}