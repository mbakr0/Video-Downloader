package online.mohmedbakr.downloadvideos

import android.app.DownloadManager
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import online.mohmedbakr.downloadvideos.core.Platform
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.navigation.AppNavHost
import online.mohmedbakr.downloadvideos.navigation.NavigationItem
import online.mohmedbakr.downloadvideos.network.ApiClient
import online.mohmedbakr.downloadvideos.network.ApiClientInstance
import online.mohmedbakr.downloadvideos.ui.theme.DownloadVideosTheme
import java.io.File

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var downloadFile: File
        lateinit var downloadManager:DownloadManager
        lateinit var videoDataBase: VideoDataBase
        lateinit var apiClient: ApiClient
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        downloadFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            resources.getString(R.string.app_name)
        ).apply { mkdir() }

        downloadManager = baseContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        apiClient = ApiClientInstance.getInstance()
        setContent {
            DownloadVideosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    videoDataBase = VideoDataBase.getInstance(applicationContext)
                    val navController = rememberNavController()
                    AppNavHost(navController, PaddingValues(5.dp))
                }
            }
        }
    }
}
@Composable
fun SelectPlatform(navController: NavHostController) {
    val selectedOption = remember { mutableStateOf(Platform.Tiktok) }
    val interactionSource = remember { MutableInteractionSource()}
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.align(Alignment.Center)){
            ItemListPlatform(R.drawable.tiktok, Platform.Tiktok,selectedOption)
            ItemListPlatform(R.drawable.instagram, Platform.Instagram,selectedOption)
            ItemListPlatform(R.drawable.youtube, Platform.Youtube,selectedOption)
            Button(onClick = {
                navController.navigate(NavigationItem.Username.withData(selectedOption.value.name))
            },interactionSource = interactionSource, modifier = Modifier
                .padding(top = 150.dp)
                .scaleOnPress(interactionSource)) {
                Text(text = "Select Platform", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", modifier = Modifier.padding(start = 30.dp))
            }
        }
    }


}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemListPlatform(drawableId: Int, platform: Platform, selectedOption: MutableState<Platform>){

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()) {
                selectedOption.value = platform
            }
    ){
        RadioButton(
            selected = selectedOption.value == platform,
            onClick = { selectedOption.value = platform }
    )
        GlideImage(model = drawableId , contentDescription = "",
            Modifier
                .size(36.dp)
                .padding(end = 5.dp)
        )
        Text(text = platform.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}


fun Modifier.scaleOnPress(
    interactionSource: InteractionSource
) = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1f,
        label = "scale",
    )
    graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
}
































