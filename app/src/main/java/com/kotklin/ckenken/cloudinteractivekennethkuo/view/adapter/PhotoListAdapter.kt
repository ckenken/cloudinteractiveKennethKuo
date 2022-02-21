package com.example.myapplication.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kotklin.ckenken.cloudinteractivekennethkuo.databinding.ItemPhotoBinding
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity.ListingActivity
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.ListingViewModel

class PhotoListAdapter(private val photoItemList: ArrayList<PhotoItem>,
                       private val listingActivity: ListingActivity,
                       private val viewModel: ListingViewModel
): RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

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
        ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = photoItemList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoItemList[position], position)
        holder.itemView.setOnClickListener {
            listingActivity.onClick(photoItemList[position])
        }
    }

    inner class PhotoViewHolder(itemViewBinding: ItemPhotoBinding): RecyclerView.ViewHolder(itemViewBinding.root) {
        private val photoImage = itemViewBinding.photoItemImage
        private val photoId = itemViewBinding.photoItemId
        private val photoTitle = itemViewBinding.photoItemTitle

        fun bind(photoItem: PhotoItem, position: Int) {
            photoId.text = photoItem.photoId.toString()
            photoTitle.text = photoItem.title
            photoImage.setImageDrawable(photoItem.localThumbnail.value)
            if (photoItem.localThumbnail.value == null) {
                photoItem.localThumbnail.observe(listingActivity) {
                    notifyItemChanged(position)
                }
                viewModel.refreshLocalThumbnail(photoItem)
            }
        }
    }
}