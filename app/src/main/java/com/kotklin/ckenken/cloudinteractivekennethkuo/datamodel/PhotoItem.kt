package com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName

data class PhotoItem(
    @SerializedName("albumId")
    val albumId: Int,
    @SerializedName("id")
    val photoId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String,
    var localThumbnail: Drawable?
)