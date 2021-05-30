package com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.CoroutineContext

object ImageFileManager {
    private const val TAG = "ImageDownloadHelper"
    private const val IMAGE_FOLDER_NAME = "image"
    private val THREAD_LIMIT_NUM = Runtime.getRuntime().availableProcessors()
    private val limitedDispatcher = LimitedDispatcher()

    class LimitedDispatcher : CoroutineDispatcher() {
        private val execService = Executors.newFixedThreadPool(THREAD_LIMIT_NUM)
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            execService.submit(block)
        }
    }

    suspend fun getLocalImageBitmap(context: Context, url: String, isQuickDownload: Boolean): Bitmap? = withContext(Dispatchers.IO) {
        return@withContext if (isImageCacheFileExist(context, url)) {
            loadLocalImage(context, url)
        } else {
            val dispatcher = if (isQuickDownload) Dispatchers.IO else limitedDispatcher
            withContext(dispatcher) {
                downloadImage(url)?.apply {
                    saveFile(context, url, this)
                }
            }
        }
    }

    private fun convertUrlToFileName(url: String): String {
        return url.replace("/", "-") + ".png"
    }

    private suspend fun getCacheImageFolder(context: Context): File = withContext(Dispatchers.IO) {
        val dir = File(context.cacheDir.path, IMAGE_FOLDER_NAME)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return@withContext dir
    }

    private suspend fun isImageCacheFileExist(context: Context, url: String): Boolean = withContext(Dispatchers.IO) {
        val fileName = convertUrlToFileName(url)
        val file = File(getCacheImageFolder(context), fileName)
        return@withContext file.exists()
    }

    private suspend fun loadLocalImage(context: Context, url: String): Bitmap? {
        val fileName = convertUrlToFileName(url)
        val file = File(getCacheImageFolder(context), fileName)
        return BitmapFactory.decodeFile(file.path)
    }

    private fun downloadImage(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection().apply {
                if (this is HttpsURLConnection) {
                    addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
                }
            }
            BitmapFactory.decodeStream(connection.getInputStream())
        } catch (e: Exception) {
            Log.e(TAG, "downloadImage() error! ", e)
            null
        }
    }

    private suspend fun saveFile(context: Context, url: String, bitmap: Bitmap): Boolean = withContext(Dispatchers.IO) {
        try {
            val fileName = convertUrlToFileName(url)
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