package com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.AlbumService
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem
import kotlinx.coroutines.*

class ListingViewModel : ViewModel() {
    companion object {
        const val TAG = "ListingViewModel"
    }

    private val albumService = AlbumService.getAlbumService()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext , throwable ->
        Log.e(TAG, "coroutine error!", throwable)
    }

    val photoItemList = MutableLiveData<List<PhotoItem>>()

    val loadingErrorMessage = MutableLiveData<String>()
    val isDataLoading = MutableLiveData<Boolean>()

    fun refreshData() {
        isDataLoading.value = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = albumService.getPhotoItemList()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    photoItemList.value = response.body()
                    loadingErrorMessage.value = ""
                    isDataLoading.value = false
                }
            } else {
                onError("refresh data error!")
            }
        }
    }

    private fun onError(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            loadingErrorMessage.value = message
            isDataLoading.value = false
        }
    }
}