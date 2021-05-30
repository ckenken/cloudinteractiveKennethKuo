package com.kotklin.ckenken.cloudinteractivekennethkuo.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kotklin.ckenken.cloudinteractivekennethkuo.R
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity.ListingActivity
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.ListingViewModel
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoListAdapter(private val photoItemList: ArrayList<PhotoItem>,
                       private val listingActivity: ListingActivity,
                       private val viewModel: ListingViewModel): RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    companion object {
        private const val TAG = "PhotoListAdapter"
    }

    fun updatePhotoList(newPhotoList: List<PhotoItem>) {
        photoItemList.clear()
        photoItemList.addAll(newPhotoList)
        photoItemList.map { item ->
            item.localThumbnail = MutableLiveData<Drawable?>()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = PhotoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
    )

    override fun getItemCount() = photoItemList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoItemList[position], position)
        holder.itemView.setOnClickListener {
            listingActivity.onClick(photoItemList[position])
        }
    }

    inner class PhotoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val photoImage = view.photoItemImage
        private val photoId = view.photoItemId
        private val photoTitle = view.photoItemTitle

        fun bind(photoItem: PhotoItem, position: Int) {
            photoId.text = photoItem.photoId.toString()
            photoTitle.text = photoItem.title
            photoImage.setImageDrawable(photoItem.localThumbnail.value)
            if (photoItem.localThumbnail.value == null) {
                photoItem.localThumbnail.observe(listingActivity, {
                    notifyItemChanged(position)
                })
                viewModel.refreshLocalThumbnail(photoItem)
            }
        }
    }
}