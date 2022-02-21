package com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.AlbumService
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.ImageFileManager
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem
import kotlinx.coroutines.*

class ListingViewModel(private val application: Application) : ViewModel() {
    companion object {
        const val TAG = "ListingViewModel"
    }

    private val albumService = AlbumService.getAlbumService()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext , throwable ->
        Log.e(TAG, "coroutine error!", throwable)
        onError(throwable.toString())
    }

    val photoItemList = MutableLiveData<List<PhotoItem>>()

    val loadingErrorMessage = MutableLiveData<String>()

    val isDataRequesting = MutableLiveData<Boolean>()

    fun refreshData() {
        isDataRequesting.value = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = albumService.getPhotoItemList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    photoItemList.value = response.body()
                    loadingErrorMessage.value = ""
                    isDataRequesting.value = false
                }
            } else {
                onError("refresh data error!")
            }
        }
    }

    fun refreshLocalThumbnail(photoItem: PhotoItem) {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            ImageFileManager.getLocalImageBitmap(application, photoItem.thumbnailUrl, false)?.apply {
                photoItem.localThumbnail.value = BitmapDrawable(application.resources, this)
            }
        }
    }

    private fun onError(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            loadingErrorMessage.value = message
            isDataRequesting.value = false
        }
    }
}