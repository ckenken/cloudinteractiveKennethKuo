package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem

interface OnPhotoClickListener {
    fun onClick(photoItem: PhotoItem)
}