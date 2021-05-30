package com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.util.ImageProcessingHelper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
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

    fun refreshLocalThumbnail(context: Context, thumbnailUrl: String) {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            isThumbnailProcessing.value = true
            val cacheFileName = ImageProcessingHelper.convertUrlToFileName(thumbnailUrl)
            val bitmap = if (ImageProcessingHelper.isImageCacheFileExist(context, cacheFileName)) {
                ImageProcessingHelper.loadLocalImage(context, cacheFileName)
            } else {
                ImageProcessingHelper.downloadImage(thumbnailUrl)?.apply {
                    ImageProcessingHelper.saveFile(context, cacheFileName, this)
                }
            }
            if (bitmap != null) {
                localThumbnail.value = BitmapDrawable(context.resources, bitmap)
            } else {
                onError("Bitmap loading fail!")
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