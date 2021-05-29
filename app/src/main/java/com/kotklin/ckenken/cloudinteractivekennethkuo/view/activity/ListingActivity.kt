package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kotklin.ckenken.cloudinteractivekennethkuo.R
import com.kotklin.ckenken.cloudinteractivekennethkuo.view.adapter.PhotoListAdapter
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.ListingViewModel
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ListingActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ListingViewModel::class.java)
    }

    @ExperimentalCoroutinesApi
    private val photoListAdapter by lazy { PhotoListAdapter(arrayListOf(), lifecycleScope) }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)

        viewModel.refreshData()

        photoItemListView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = photoListAdapter
        }

        lifecycleScope

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

        viewModel.isDataLoading.observe(this) { isLoading ->
            loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                listingError.visibility = View.GONE
                photoItemListView.visibility = View.GONE
            }
        }
    }
}