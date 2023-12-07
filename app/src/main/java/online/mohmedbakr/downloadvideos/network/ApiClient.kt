package online.mohmedbakr.downloadvideos.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.mohmedbakr.downloadvideos.model.tiktokModel.TiktokRoot
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiClient {
    @Headers("X-RapidAPI-Host: tiktok-get-all-video-by-username.p.rapidapi.com")
    @GET(TIKTOK_URL + "tiktok/allvideos")
    fun getTiktokVideos(@Query("username") username:String, @Query("max_cursor") maxCursor:String) : Single<TiktokRoot>

    @Headers("X-Rapidapi-Host: instagram-data1.p.rapidapi.com")
    @GET(INSTAGRAM_URL + "user/reels")
    fun getInstagramVideos(@Query("username") username: String,@Query("end_cursor")endCursor:String?) : Single<JsonElement>

    @Headers("X-RapidAPI-Host: yt-api.p.rapidapi.com")
    @GET(YOUTUBE_URL+"channel/videos")
    fun getYoutubeVideos(@Query("forUsername") username: String,@Query("token")continuation:String?) : Single<JsonElement>

    @Headers("X-RapidAPI-Host: yt-api.p.rapidapi.com")
    @GET(YOUTUBE_URL+"dl")
    fun getYoutubeVideoLink(@Query("id") id:String) : Single<JsonElement>

    companion object {
        private const val TIKTOK_URL =  "https://tiktok-get-all-video-by-username.p.rapidapi.com/"
        private const val INSTAGRAM_URL =  "https://instagram-data1.p.rapidapi.com/"
        private const val YOUTUBE_URL = "https://yt-api.p.rapidapi.com/"
    }
}

object ApiClientInstance {
    @Volatile private var INSTANCE: ApiClient? = null
    fun getInstance(): ApiClient =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildRetrofit().also { INSTANCE = it }
        }

    private fun buildRetrofit() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://rapidapi.com/")
            .client(httpClient)
            .build()
            .create(ApiClient::class.java)


    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val InterceptorChain = { chain:Interceptor.Chain->
        val request = chain.request().newBuilder()
            .header("X-RapidAPI-Key","API_KEY")
            .build()
        chain.proceed(request)
    }

    private val httpClient = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(InterceptorChain)
        .build()


}