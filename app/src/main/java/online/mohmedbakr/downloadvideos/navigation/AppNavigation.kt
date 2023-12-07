package online.mohmedbakr.downloadvideos.navigation


enum class Screen{
    SelectPlatform,
    Username,
    UserVideo,
    VideoPlayer;
}
sealed class NavigationItem(val route: String) {
    data object SelectPlatform : NavigationItem(Screen.SelectPlatform.name)
    data object Username : NavigationItem(Screen.Username.name)
    data object UserVideo : NavigationItem(Screen.UserVideo.name)
    data object VideoPlayer : NavigationItem(Screen.VideoPlayer.name)
    fun withArgument(vararg args:String) : String{
        return this.route + args.joinToString("}/{","/{","}")
    }
    fun withData(vararg args:String):String{
        return this.route + args.joinToString("/","/")
    }
}
