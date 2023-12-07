package online.mohmedbakr.downloadvideos.database

import androidx.room.TypeConverter
import online.mohmedbakr.downloadvideos.core.getPlatform
import online.mohmedbakr.downloadvideos.core.Platform

class Converters {
    @TypeConverter
    fun stringToPlatform(str:String?): Platform? {
        return str?.getPlatform()
    }
    @TypeConverter
    fun platformToString(platform: Platform?): String? {
        return platform?.name
    }
}