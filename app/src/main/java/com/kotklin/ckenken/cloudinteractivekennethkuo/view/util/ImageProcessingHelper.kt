package com.kotklin.ckenken.cloudinteractivekennethkuo.view.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@ExperimentalCoroutinesApi
object ImageProcessingHelper {
    private const val TAG = "ImageDownloadHelper"
    private const val IMAGE_FOLDER_NAME = "image"

    fun convertUrlToFileName(url: String): String {
        return url.replace("/", "-") + ".png"
    }

    suspend fun getCacheImageFolder(context: Context): File = withContext(Dispatchers.IO) {
        val dir = File(context.cacheDir.path, IMAGE_FOLDER_NAME)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return@withContext dir
    }

    suspend fun isImageCacheFileExist(context: Context, fileName: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(getCacheImageFolder(context), fileName)
        return@withContext file.exists()
    }

    suspend fun loadLocalImage(context: Context, fileName: String): Bitmap? {
        val file = File(getCacheImageFolder(context), fileName)
        return BitmapFactory.decodeFile(file.path)
    }

    fun downloadImage(url: String): Flow<Bitmap> =
        callbackFlow<Bitmap> {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(TAG, "downloadImage onFailure() error", e)
                    close()
                }

                override fun onResponse(call: Call, response: Response) {
                    val stream = response.body()?.byteStream()
                    val bitmap = BitmapFactory.decodeStream(stream)
                    trySend(bitmap).isSuccess
                    close()
                }
            })
            awaitClose()
        }

    suspend fun saveFile(context: Context, fileName: String, bitmap: Bitmap): Boolean = withContext(Dispatchers.IO) {
        try {
            val newFile = File(getCacheImageFolder(context).path, fileName)
            FileOutputStream(newFile).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "saveFile Error!", e)
            return@withContext false
        }
    }
}