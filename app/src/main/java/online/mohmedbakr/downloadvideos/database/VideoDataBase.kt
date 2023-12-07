package online.mohmedbakr.downloadvideos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [VideoInfo::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class VideoDataBase : RoomDatabase() {
    abstract fun getVideoDao(): VideoDao

    companion object {
        @Volatile private var INSTANCE: VideoDataBase? = null
        fun getInstance(context: Context): VideoDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                VideoDataBase::class.java, "videosDatabase.db")
                .build()
    }
}
