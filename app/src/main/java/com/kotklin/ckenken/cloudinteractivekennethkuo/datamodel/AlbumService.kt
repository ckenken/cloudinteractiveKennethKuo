package com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AlbumService {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    
    fun getAlbumService(): AlbumApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlbumApi::class.java)
    }
}