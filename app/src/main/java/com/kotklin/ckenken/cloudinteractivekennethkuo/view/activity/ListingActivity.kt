package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kotklin.ckenken.cloudinteractivekennethkuo.R
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.PhotoItem
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.adapter.PhotoListAdapter
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.BasicModelFactory
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.ListingViewModel
import kotlinx.android.synthetic.main.activity_listing.*

class ListingActivity : AppCompatActivity(), OnPhotoClickListener {

    private val viewModel by lazy {
        ViewModelProvider(this, BasicModelFactory).get(ListingViewModel::class.java)
    }

    private val photoListAdapter by lazy { PhotoListAdapter(arrayListOf(), this, lifecycleScope) }

    override fun onClick(photoItem: PhotoItem) {
        val intent = getRedirectIntent(photoItem)
        startActivity(intent)
    }

    private fun getRedirectIntent(photoItem: PhotoItem): Intent {
        val intent = Intent(this@ListingActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.KEY_ID, photoItem.photoId)
        intent.putExtra(DetailActivity.KEY_TITLE, photoItem.title)
        intent.putExtra(DetailActivity.KEY_THUMBNAIL, photoItem.thumbnailUrl)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)

        viewModel.refreshData()

        photoItemListView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = photoListAdapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.photoItemList.observe(this, { photoList ->
            photoItemListView.visibility = View.VISIBLE
            photoListAdapter.updatePhotoList(photoList)
        })

        viewModel.loadingErrorMessage.observe(this, { isError ->
            listingError.visibility = if(TextUtils.isEmpty(isError)) View.GONE else View.VISIBLE
        })

        viewModel.isDataRequesting.observe(this) { isLoading ->
            loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                listingError.visibility = View.GONE
                photoItemListView.visibility = View.GONE
            }
        }
    }
}