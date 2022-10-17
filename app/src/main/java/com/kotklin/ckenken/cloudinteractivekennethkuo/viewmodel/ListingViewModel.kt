package com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.AlbumService
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.ImageFileManager
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.OuterPhotoItem
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import java.net.InetSocketAddress
import java.net.Proxy

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

    fun getCurrentSystemSettings(): Proxy? {
        val proxyHost = System.getProperty("http.proxyHost")
        val proxyPort = System.getProperty("http.proxyPort")?.toIntOrNull()
        return if (!proxyHost.isNullOrEmpty() && proxyPort != null) {
            Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHost, proxyPort))
        } else null
    }

    class KkmanInseptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            Log.d("ckenken", "xxxxxxx")
            return chain.proceed(chain.request())
        }
    }

    private val httpClient by lazy { HttpClient(OkHttp.create {
        addInterceptor(KkmanInseptor())
        getCurrentSystemSettings()?.let {
            proxy = it
        }
    }) {
        expectSuccess = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json{
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    } }

    fun refreshData() {
        isDataRequesting.value = true
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            val response: List<PhotoItem> = httpClient.get("https://jsonplaceholder.typicode.com/photos") {
                Log.d("ckenken", "body = $body")
            }.body()

            Log.d("ckenken", "response = $response")

            withContext(Dispatchers.Main) {
                photoItemList.value = response
                loadingErrorMessage.value = ""
                isDataRequesting.value = false
            }
        }
    }

    fun refreshLocalThumbnail(photoItem: OuterPhotoItem) {
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