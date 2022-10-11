package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.adapter.PhotoListAdapter
import com.kotklin.ckenken.cloudinteractivekennethkuo.databinding.ActivityListingBinding
import com.kotklin.ckenken.cloudinteractivekennethkuo.datamodel.OuterPhotoItem
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.ListingViewModel
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.ListingViewModelFactory

class ListingActivity : AppCompatActivity(), OnPhotoItemClickListener {

    private val viewModel by lazy {
        ViewModelProvider(this, ListingViewModelFactory(application)).get(ListingViewModel::class.java)
    }

    private lateinit var viewBinding : ActivityListingBinding

    private val photoListAdapter by lazy { PhotoListAdapter(arrayListOf(), this, viewModel) }

    override fun onClick(photoItem: OuterPhotoItem) {
        val intent = getRedirectIntent(photoItem)
        startActivity(intent)
    }

    private fun getRedirectIntent(photoItem: OuterPhotoItem): Intent {
        val intent = Intent(this@ListingActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.KEY_ID, photoItem.photoId)
        intent.putExtra(DetailActivity.KEY_TITLE, photoItem.title)
        intent.putExtra(DetailActivity.KEY_THUMBNAIL, photoItem.thumbnailUrl)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityListingBinding.inflate(layoutInflater, findViewById(android.R.id.content), false).also {
            viewBinding = it
            setContentView(it.root)
        }

        viewBinding.photoItemListView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = photoListAdapter
        }

        observeViewModel()
        viewModel.refreshData()
    }

    private fun observeViewModel() {
        viewBinding.apply {
            viewModel.photoItemList.observe(this@ListingActivity) { photoList ->
                photoItemListView.visibility = View.VISIBLE
                photoListAdapter.updatePhotoList(photoList)
            }

            viewModel.loadingErrorMessage.observe(this@ListingActivity) { isError ->
                listingError.visibility =
                    if (TextUtils.isEmpty(isError)) View.GONE else View.VISIBLE
            }

            viewModel.isDataRequesting.observe(this@ListingActivity) { isLoading ->
                loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    listingError.visibility = View.GONE
                    photoItemListView.visibility = View.GONE
                }
            }
        }
    }
}