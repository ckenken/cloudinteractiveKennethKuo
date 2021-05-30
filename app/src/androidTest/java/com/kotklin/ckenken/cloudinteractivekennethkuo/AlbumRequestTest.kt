package com.kotklin.ckenken.cloudinteractivekennethkuo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.AlbumService
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumRequestTest {
    @Test
    fun testAlbumRequest() =
        runBlocking {
            val albumService = AlbumService.getAlbumService()
            val response = albumService.getPhotoItemList()
            Assert.assertTrue(response.isSuccessful)
            val list = response.body()
            Assert.assertNotNull(list)
            Assert.assertTrue(list!!.isNotEmpty())
        }
}