package com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.ImageFileManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val application: Application) : ViewModel() {
    companion object {
        const val TAG = "DetailViewModel"
    }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext , throwable ->
        Log.e(ListingViewModel.TAG, "coroutine error!", throwable)
        onError(throwable.toString())
    }

    val loadingErrorMessage = MutableLiveData<String>()

    val isThumbnailProcessing = MutableLiveData<Boolean>()

    val localThumbnail = MutableLiveData<Drawable>()

    fun refreshLocalThumbnail(thumbnailUrl: String) {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            isThumbnailProcessing.value = true
            ImageFileManager.getLocalImageBitmap(application, thumbnailUrl)?.apply {
                localThumbnail.value = BitmapDrawable(application.resources, this)
            }
            isThumbnailProcessing.value = false
        }
    }

    private fun onError(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            loadingErrorMessage.value = message
            isThumbnailProcessing.value = false
        }
    }
}