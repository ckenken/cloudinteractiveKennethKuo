package com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
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

    // Not in API response
    var localThumbnail: MutableLiveData<Drawable?>
)