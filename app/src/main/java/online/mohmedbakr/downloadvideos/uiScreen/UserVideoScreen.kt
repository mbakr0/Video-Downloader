package online.mohmedbakr.downloadvideos.uiScreen

import android.text.format.Formatter.formatFileSize
import android.webkit.URLUtil
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import online.mohmedbakr.downloadvideos.MainActivity
import online.mohmedbakr.downloadvideos.MainViewModel
import online.mohmedbakr.downloadvideos.MainViewModelFactory
import online.mohmedbakr.downloadvideos.R
import online.mohmedbakr.downloadvideos.core.Platform
import online.mohmedbakr.downloadvideos.core.asVideoItemUi
import online.mohmedbakr.downloadvideos.core.getPlatform
import online.mohmedbakr.downloadvideos.model.VideoItemUi
import online.mohmedbakr.downloadvideos.navigation.NavigationItem
import online.mohmedbakr.downloadvideos.ui.theme.Green100
import online.mohmedbakr.downloadvideos.ui.theme.Green700
import online.mohmedbakr.downloadvideos.ui.theme.Primary
import online.mohmedbakr.downloadvideos.ui.theme.Secondary
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun VideoScreen(
    username:String,
    platform: String,
    navController: NavController,
    mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(
        MainActivity.videoDataBase,
        MainActivity.apiClient
    )
    )) {
    val platformObject = platform.getPlatform()
    VideoList(mainViewModel,username,platformObject,navController)

}
@Composable
fun VideoList(
    mainViewModel: MainViewModel,
    username: String,
    platformObject: Platform,
    navController: NavController
) {
    val videoList by mainViewModel.videoList.observeAsState()
    val list = videoList?.filter { it.username == username && it.platform == platformObject }
        ?.map {
            it.asVideoItemUi()
        }
        ?: listOf()
    var loading = true
    if (list.isNotEmpty()) loading = false
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp))
    {
       if (loading)
            CircularProgressIndicator(
                color = Green700,
                modifier = Modifier.align(Alignment.Center)
            )

        else if (list.isNotEmpty()) {
           LazyColumn{
               items(list) {
                   ItemList(it, mainViewModel,navController)
               }
           }
        }
    }

}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemList(videoItemUiState: VideoItemUi, viewModel: MainViewModel, navController: NavController) {
    var showDownloadIcon by remember { mutableStateOf(true) }
    val currentValue by remember { videoItemUiState.currentValue }

    Column (modifier = Modifier.padding(8.dp)
        .clickable(
            indication = null,
            interactionSource = MutableInteractionSource()) {
            if (URLUtil.isValidUrl(videoItemUiState.urlVideo))
            {
                val encodedUrl = URLEncoder.encode(videoItemUiState.urlVideo, StandardCharsets.UTF_8.toString())
                navController.navigate(NavigationItem.VideoPlayer.withData(encodedUrl))

            }
    }){

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            GlideImage(
                loading = placeholder(R.drawable.baseline_image_24),
                model  = videoItemUiState.cover,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(100.dp)
            )
            Text(text = videoItemUiState.title,
                Modifier.weight(1f))
            if (showDownloadIcon) IconButton(onClick = {
                showDownloadIcon = false
                viewModel.downloadVideo(videoItemUiState)
            }) {
                Icon(painter = painterResource(id = R.drawable.outline_download_24), contentDescription = "")
            }
            else if(currentValue < 1f)
                CircularProgressIndicator(color = Green700)
        }
        Divider(thickness = 5.dp, color = Color.White)
        CustomProgressIndicator(videoItemUiState)
    }

}
@Composable
fun CustomProgressIndicator(videoItemUiState: VideoItemUi) {
    val currentValue by remember { videoItemUiState.currentValue }
    val inProgress by remember { videoItemUiState.inProgress }
    val maxValue by remember { videoItemUiState.maxValue }

    val formattedMaxValue = formatFileSize(LocalContext.current , maxValue.toLong())
    val formattedCurrentValue = formatFileSize(LocalContext.current , currentValue.toLong())

    if (inProgress)
        Column(horizontalAlignment = Alignment.End, modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)){
        ProgressStatus(formattedCurrentValue,formattedMaxValue)
        CustomLinearProgressIndicator(currentValue,maxValue)

    }

}


@Composable
private fun ProgressStatus(currentValue: String, maxValue: String ) {
    Text(text = buildAnnotatedString {

        val fontSize = TextUnit(14f, TextUnitType.Sp)
        val emphasisSpan =
            typography.bodyLarge.copy(color = if (currentValue == maxValue) Primary else Secondary,fontSize = fontSize)
                .toSpanStyle()
        val defaultSpan =
            typography.bodyMedium.copy(color = Primary,fontSize = fontSize).toSpanStyle()
        append(AnnotatedString(currentValue, spanStyle = emphasisSpan))
        append(AnnotatedString(text = "/", spanStyle = defaultSpan))
        append(AnnotatedString(text = maxValue, spanStyle = defaultSpan))
    }
    )
}
@Composable
private fun CustomLinearProgressIndicator(currentValue: Float,maxValue: Float){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 4.dp)) {
        val progress = currentValue * size.width / maxValue
        drawLine(
            Green100,
            Offset(0f,0f),
            Offset(size.width,0f),
            10f,
            StrokeCap.Round)
        drawLine(
            Green700,
            Offset(0f,0f),
            Offset(progress,0f),
            10f,
            StrokeCap.Round)
    }
}

