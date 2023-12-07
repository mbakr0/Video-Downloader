package online.mohmedbakr.downloadvideos.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import online.mohmedbakr.downloadvideos.core.Platform

data class VideoItemUi(
    var urlVideo: String,
    val cover: String,
    val title: String,
    val platform: Platform,
    val username:String,
    var currentValue:MutableState<Float> = mutableFloatStateOf(0f),
    var maxValue:MutableState<Float> = mutableFloatStateOf(0f),
    var inProgress: MutableState<Boolean> = mutableStateOf(false)
)

