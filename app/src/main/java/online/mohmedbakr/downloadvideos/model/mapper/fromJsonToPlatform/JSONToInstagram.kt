package online.mohmedbakr.downloadvideos.model.mapper.fromJsonToPlatform

import com.google.gson.JsonElement
import online.mohmedbakr.downloadvideos.model.instagramModel.InstagramRoot
import online.mohmedbakr.downloadvideos.model.instagramModel.InstagramVideo
import online.mohmedbakr.downloadvideos.model.mapper.Mapper

class JSONToInstagram : Mapper<JsonElement,InstagramRoot> {
    override fun map(input: JsonElement): InstagramRoot {
        val videoList = mutableListOf<InstagramVideo>()

        val endCursor = input.asJsonObject.getAsJsonPrimitive("endCursor").asString
        val items = input.asJsonObject.getAsJsonArray("items")
        for (item in items) {
            videoList.add(
                InstagramVideo(
                    urlVideo = item.asJsonObject.getAsJsonArray("video_versions")[0]
                        .asJsonObject.getAsJsonPrimitive("url").asString,
                    cover = item.asJsonObject.getAsJsonObject("image_versions2")
                        .getAsJsonArray("candidates")[0].asJsonObject.getAsJsonPrimitive("url").asString,
                    title = item.asJsonObject.getAsJsonObject("caption").getAsJsonPrimitive("text").asString
                )
            )
        }
        return InstagramRoot(endCursor, videoList)
    }
}