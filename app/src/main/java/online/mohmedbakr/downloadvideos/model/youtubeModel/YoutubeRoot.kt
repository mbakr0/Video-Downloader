package online.mohmedbakr.downloadvideos.model.youtubeModel

data class YoutubeRoot (
    val continuation:String,
    val videoList:List<YoutubeVideo>
)