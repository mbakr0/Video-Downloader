package online.mohmedbakr.downloadvideos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import online.mohmedbakr.downloadvideos.database.VideoDataBase
import online.mohmedbakr.downloadvideos.network.ApiClient

class MainViewModelFactory(
    private val database: VideoDataBase,
    private val apiClient: ApiClient,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(database,apiClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}