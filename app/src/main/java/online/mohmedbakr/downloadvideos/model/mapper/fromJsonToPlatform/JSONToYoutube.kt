package online.mohmedbakr.downloadvideos.model.mapper.fromJsonToPlatform

import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import online.mohmedbakr.downloadvideos.model.mapper.Mapper
import online.mohmedbakr.downloadvideos.model.youtubeModel.YoutubeRoot
import online.mohmedbakr.downloadvideos.model.youtubeModel.YoutubeVideo

class JSONToYoutube :Mapper<JsonElement,YoutubeRoot> {
    override fun map(input: JsonElement): YoutubeRoot {
        val videoList = mutableListOf<YoutubeVideo>()
        val continuation = input.asJsonObject.getAsJsonPrimitive("continuation").asString
        val dataList = input.asJsonObject.getAsJsonArray("data")
        for (data in dataList) {
            videoList.add(
                YoutubeVideo(
                    videoId = data.asJsonObject.getAsJsonPrimitive("videoId").asString,
                    cover = if (data.asJsonObject.get("richThumbnail") is JsonNull)
                        data.asJsonObject.getAsJsonArray("thumbnail").last().asJsonObject.getAsJsonPrimitive("url").asString
                    else
                        data.asJsonObject.getAsJsonArray("richThumbnail")[0].asJsonObject.getAsJsonPrimitive("url").asString
                    ,
                    title = data.asJsonObject.getAsJsonPrimitive("title").asString
                )
            )

        }
        Log.d("Mapper",videoList.toString())
        return YoutubeRoot(continuation, videoList)
    }
}
class JSONToYoutubeUrl:Mapper<JsonElement,String>{
    override fun map(input: JsonElement): String {
        return input.asJsonObject.getAsJsonArray("formats").last().asJsonObject.getAsJsonPrimitive("url").asString
    }

}