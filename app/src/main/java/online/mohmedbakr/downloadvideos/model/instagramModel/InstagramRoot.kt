package online.mohmedbakr.downloadvideos.model.instagramModel

data class InstagramRoot(
    val endCursor:String,
    val videoList:List<InstagramVideo>
)