package com.kotklin.ckenken.cloudinteractivekennethkuo.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotklin.ckenken.cloudinteractivekennethkuo.R
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity.DetailActivity
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.util.ImageProcessingHelper
import kotlinx.android.synthetic.main.item_photo.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import java.io.File

@ExperimentalCoroutinesApi
class PhotoListAdapter(private val photoItemList: ArrayList<PhotoItem>,
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
        holder.bind(photoItemList[position])
    }

    inner class PhotoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val photoItemContainer = view.photoItemContainer
        private val imageView = view.photoItemImage
        private val photoId = view.photoItemId
        private val photoTitle = view.photoItemTitle

        fun bind(photoItem: PhotoItem) {
            photoId.text = photoItem.photoId.toString()
            photoTitle.text = photoItem.title
            coroutineScope.launch(Dispatchers.Main) {
//                imageView.loadImage(photoItem.thumbnailUrl)
                val cacheFileName = ImageProcessingHelper.convertUrlToFileName(photoItem.thumbnailUrl)
                if (ImageProcessingHelper.isImageCacheFileExist(imageView.context, cacheFileName)) {
                    imageView.setImageBitmap(ImageProcessingHelper.loadLocalImage(imageView.context, cacheFileName))
                } else {
                    val bitmap = ImageProcessingHelper.downloadImage(photoItem.thumbnailUrl)
                        .catch { throwable ->
                            Log.e(TAG, "downloadImage fail", throwable)
                        }
                        .single()
                    imageView.setImageBitmap(bitmap)
                    ImageProcessingHelper.saveFile(imageView.context, cacheFileName, bitmap)
                }

                val intent = getRedirectIntent(cacheFileName, photoItem)
                photoItemContainer.setOnClickListener {
                    it.context.startActivity(intent)
                }
            }
        }

        private suspend fun getRedirectIntent(cacheFileName: String, photoItem: PhotoItem): Intent {
            val intent = Intent(imageView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.KEY_ID, photoItem.photoId)
            intent.putExtra(DetailActivity.KEY_TITLE, photoItem.title)

            if (ImageProcessingHelper.isImageCacheFileExist(imageView.context, cacheFileName)) {
                intent.putExtra(DetailActivity.KEY_THUMBNAIL, File(ImageProcessingHelper.getCacheImageFolder(imageView.context).path, cacheFileName).path)
            }
            return intent
        }
    }
}