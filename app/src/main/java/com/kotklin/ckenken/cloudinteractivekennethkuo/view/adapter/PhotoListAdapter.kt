package com.kotklin.ckenken.cloudinteractivekennethkuo.view.adapter

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotklin.ckenken.cloudinteractivekennethkuo.R
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity.OnPhotoClickListener
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.util.ImageProcessingHelper
import kotlinx.android.synthetic.main.item_photo.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoListAdapter(private val photoItemList: ArrayList<PhotoItem>,
                       private val onItemClickListener: OnPhotoClickListener,
                       private val coroutineScope: CoroutineScope): RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    companion object {
        private const val TAG = "PhotoListAdapter"
    }

    fun updatePhotoList(newPhotoList: List<PhotoItem>) {
        photoItemList.clear()
        photoItemList.addAll(newPhotoList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = PhotoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
    )

    override fun getItemCount() = photoItemList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoItemList[position], position)
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(photoItemList[position])
        }
    }

    inner class PhotoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imageView = view.photoItemImage
        private val photoId = view.photoItemId
        private val photoTitle = view.photoItemTitle

        fun bind(photoItem: PhotoItem, position: Int) {
            photoId.text = photoItem.photoId.toString()
            photoTitle.text = photoItem.title
            coroutineScope.launch(Dispatchers.Main) {
                val cacheFileName = ImageProcessingHelper.convertUrlToFileName(photoItem.thumbnailUrl)
                imageView.setImageDrawable(photoItem.localThumbnail)
                if (photoItem.localThumbnail == null) {
                    val bitmap = if (ImageProcessingHelper.isImageCacheFileExist(imageView.context, cacheFileName)) {
                        ImageProcessingHelper.loadLocalImage(imageView.context, cacheFileName)
                    } else {
                        ImageProcessingHelper.downloadImage(photoItem.thumbnailUrl)?.apply {
                            ImageProcessingHelper.saveFile(imageView.context, cacheFileName, this)
                        }
                    }
                    if (bitmap != null) {
                        photoItem.localThumbnail = BitmapDrawable(imageView.context.resources, bitmap)
                    }
                    notifyItemChanged(position)
                }
            }
        }
    }
}