package com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel

import retrofit2.Response
import retrofit2.http.GET

interface AlbumApi {
    @GET("photos")
    suspend fun getPhotoItemList(): Response<List<PhotoItem>>
}