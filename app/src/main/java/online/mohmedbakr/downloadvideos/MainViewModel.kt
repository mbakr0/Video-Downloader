package online.mohmedbakr.downloadvideos

import android.annotation.SuppressLint
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import online.mohmedbakr.downloadvideos.core.Platform
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.model.VideoItemUi
import online.mohmedbakr.downloadvideos.network.ApiClient
import online.mohmedbakr.downloadvideos.repository.DownLoadVideoRepository
import online.mohmedbakr.downloadvideos.repository.LocalVideoRepository
import online.mohmedbakr.downloadvideos.repository.RemoteVideoRepository

@SuppressLint("CheckResult")
class MainViewModel(
    database: VideoDataBase,
    apiClient: ApiClient,
) : ViewModel() {
    private val _uiStateError by lazy { MutableLiveData<String>() }
    private val _requestApiLoading by lazy { MutableLiveData<Boolean>() }
    private val _uiStateSuccess by lazy { MutableLiveData<String>() }

    private val remoteVideoRepository = RemoteVideoRepository(database, apiClient)
    private val localVideoRepository = LocalVideoRepository(database)
    private val downLoadVideoRepository = DownLoadVideoRepository(MainActivity.downloadManager,MainActivity.downloadFile)
    private val compositeDisposable = CompositeDisposable()

   val uiStateError: LiveData<String>
        get() = _uiStateError
    val uiStateSuccess: LiveData<String>
        get() = _uiStateSuccess
    val requestApiLoading: LiveData<Boolean>
        get() = _requestApiLoading

    val videoList = localVideoRepository.getAllLocalVideos()

    fun getRemoteVideos(platform: Platform, username: String) {
        _requestApiLoading.value = true
        remoteVideoRepository.getRemoteVideos(platform, username)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
               compositeDisposable.add(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, error ->
                if (!result.isNullOrEmpty())
                    onSuccess(result)
                else
                    error.message?.let { onError(it) }
            }
    }
    fun downloadVideo(video: VideoItemUi) {
        downLoadVideoRepository.createFile(video.username)
        if (video.platform == Platform.Youtube && !URLUtil.isValidUrl(video.urlVideo))
            compositeDisposable.add(getYoutubeVideoUrl(video))
        else
            downloadFileObservable(video)
    }

    private fun getYoutubeVideoUrl(video: VideoItemUi): Disposable {
       return remoteVideoRepository.getYoutubeUrl(video.urlVideo)
            .subscribe { result, error ->
                if (!result.isNullOrEmpty()) {
                    video.urlVideo = result
                    downloadFileObservable(video)
                    _uiStateSuccess.postValue(result)
                } else
                    onError(error.message!!)
            }
    }
    private fun downloadFileObservable(video: VideoItemUi): Disposable {
        return  downLoadVideoRepository.downloadFileObservable(video)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result,error->
                if (result.isNullOrEmpty())
                    onError(error.message!!)
                else
                    onSuccess(result)
            }
    }

    private fun onSuccess(result: String) {
        _uiStateSuccess.value = result
        finishLoading()
    }

    private fun onError(message: String) {
        _uiStateError.value = message
        finishLoading()
    }

    private fun finishLoading() {
        _requestApiLoading.value = false
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()

    }

}