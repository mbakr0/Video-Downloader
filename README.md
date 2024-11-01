# Video-Downloader

Android Application for downloading videos 

## Description

Video-Downloader allows you to download videos by username for YouTube, TikTok, and Instagram using [rapidapi](https://rapidapi.com/)

## Getting Started

### Dependencies

* Jetpack compose
* Retrofit
* Okhttp3
* RxJava
* Glide
* Room database
* ExoPlayer

### Prerequisite
subscribe to API
* [Youtube API](https://rapidapi.com/ytjar/api/yt-api)
* [Tiktok API](https://rapidapi.com/hearhour/api/tiktok-get-all-video-by-username)
* [Instagram API](https://rapidapi.com/logicbuilder/api/instagram-data1)
  
add key
```
    private val InterceptorChain = { chain:Interceptor.Chain->
        val request = chain.request().newBuilder()
            .header("X-RapidAPI-Key","API_KEY")
            .build()
        chain.proceed(request)
    }
```
### preview

[Video](https://drive.google.com/file/d/1v42kRSNM4JxvEUfuWkyfj-_Kk0MBJwLJ/view)
