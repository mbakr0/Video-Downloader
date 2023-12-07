package online.mohmedbakr.downloadvideos.uiScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import online.mohmedbakr.downloadvideos.MainActivity
import online.mohmedbakr.downloadvideos.MainViewModel
import online.mohmedbakr.downloadvideos.MainViewModelFactory
import online.mohmedbakr.downloadvideos.R
import online.mohmedbakr.downloadvideos.core.Platform
import online.mohmedbakr.downloadvideos.core.getPlatform
import online.mohmedbakr.downloadvideos.navigation.NavigationItem
import online.mohmedbakr.downloadvideos.ui.theme.Green100
import online.mohmedbakr.downloadvideos.ui.theme.Green700


@Composable
fun UsernameScreen(
    navController: NavHostController,
    platform: String,
    mainViewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
            MainActivity.videoDataBase,
            MainActivity.apiClient
        )
    ),
) {
    val platformObject = platform.getPlatform()

    Column(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        CustomSearchBar(platformObject, mainViewModel)
        FolderList(navController, platformObject,mainViewModel)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(platform: Platform, mainViewModel: MainViewModel) {
    var queryString by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    val historyItems = remember { mutableSetOf<String>() }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isActive) 0.dp else 8.dp),
        query = queryString,
        onQueryChange = { queryString = it },
        onSearch = {
            if (it.isNotEmpty()) {
                isActive = false
                historyItems.add(it)
                mainViewModel.getRemoteVideos(platform, queryString.trim())
            }
        },
        active = isActive,
        onActiveChange = { activeChange ->
            isActive = activeChange
        },
        placeholder = { Text(text = "Search by username") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    ) {
        historyItems.forEach { historyItem ->
            Row(modifier = Modifier.padding(all = 16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 12.dp),
                    painter = painterResource(id = R.drawable.history), contentDescription = null
                )
                Text(text = historyItem)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FolderList(
    navController: NavHostController,
    platform: Platform,
    mainViewModel: MainViewModel,
) {
    val videoList by mainViewModel.videoList.observeAsState()
    val usernameList = videoList
    ?.filter {
        it.platform == platform }
    ?.map { v->v.username }
    ?.distinct() ?: listOf()

    val loading by mainViewModel.requestApiLoading.observeAsState()
    val imageId = when (platform) {
        Platform.Instagram -> R.drawable.instagram
        Platform.Tiktok -> R.drawable.tiktok
        Platform.Youtube -> R.drawable.youtube
    }

    if (loading != null && loading == true)
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            trackColor = Green100,
            color = Green700,
        )

    if (usernameList.isEmpty())
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "No Data",
                fontSize = TextUnit(16f, TextUnitType.Sp),
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    else
        LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
            items(usernameList.size) {
                val username = usernameList[it]
                val videoNum = videoList
                    ?.filter { video->
                        video.username == username && video.platform == platform}
                    ?.size
                    ?: 0
                ListItem(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) {
                        navController.navigate(
                            NavigationItem.UserVideo.withData(
                                username,
                                platform.name
                            )
                        )
                    },
                    headlineContent = {
                        Text(
                            text = username,
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    },
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.folder),
                            contentDescription = ""
                        )
                    },
                    trailingContent = { GlideImage(model = imageId, contentDescription = "") },
                    supportingContent = { Text(text = "$videoNum Videos") }
                )
            }

        }
}
