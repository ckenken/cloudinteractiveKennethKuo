package com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.Serializable

@Serializable
data class PhotoItem(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)

data class OuterPhotoItem(
    val albumId: Int,
    val photoId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,

//     Not in API response
    var localThumbnail: MutableLiveData<Drawable?>
) {
    constructor(photoItem: PhotoItem) : this(
        albumId = photoItem.albumId,
        photoId = photoItem.id,
        title = photoItem.title,
        url = photoItem.url,
        thumbnailUrl = photoItem.thumbnailUrl,
        localThumbnail = MutableLiveData<Drawable?>()
    )
}