package online.mohmedbakr.downloadvideos.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import online.mohmedbakr.downloadvideos.SelectPlatform
import online.mohmedbakr.downloadvideos.uiScreen.UsernameScreen
import online.mohmedbakr.downloadvideos.uiScreen.VideoPlayerScreen
import online.mohmedbakr.downloadvideos.uiScreen.VideoScreen

@Composable
fun AppNavHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = NavigationItem.SelectPlatform.route
    ) {
        val platformArg = "platform"
        val usernameArg = "username"
        val videoUrlArg = "videoUrl"
        composable(NavigationItem.SelectPlatform.route) {
            SelectPlatform(navController)
        }
        composable(
            NavigationItem.Username.withArgument(platformArg),
            arguments = listOf(navArgument(platformArg) {
                type = StringType
            })
        ) {
            val platform = it.arguments?.getString(platformArg)!!
            UsernameScreen(navController, platform)
        }

        composable(
            NavigationItem.UserVideo.withArgument(usernameArg, platformArg),
            arguments = listOf(
                navArgument(usernameArg) { type = StringType },
                navArgument(platformArg) { type = StringType })
        ) {
            val platform = it.arguments?.getString(platformArg)!!
            val username = it.arguments?.getString(usernameArg)!!
            VideoScreen(username, platform,navController)
        }

        composable(NavigationItem.VideoPlayer.withArgument(videoUrlArg),
            arguments = listOf(
                navArgument(videoUrlArg){type = StringType}
            )
        ) {
            val videoUrl = it.arguments?.getString(videoUrlArg)!!
            VideoPlayerScreen(url = videoUrl)
        }

    }
}