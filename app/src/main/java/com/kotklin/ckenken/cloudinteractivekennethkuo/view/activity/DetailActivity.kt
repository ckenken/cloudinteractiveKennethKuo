package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.kotklin.ckenken.cloudinteractivekennethkuo.databinding.ActivityDetailBinding
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.DetailViewModel
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.DetailViewModelFactory

class DetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_THUMBNAIL = "thumbnail"
    }

    private val viewModel by lazy {
        ViewModelProvider(this, DetailViewModelFactory(application)).get(DetailViewModel::class.java)
    }

    private lateinit var viewBinding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityDetailBinding.inflate(layoutInflater, findViewById(android.R.id.content), false).also {
            viewBinding = it
            setContentView(it.root)
        }


        val id = intent.getIntExtra(KEY_ID, 0)
        val title = intent.getStringExtra(KEY_TITLE)
        val thumbnailPath = intent.getStringExtra(KEY_THUMBNAIL)

        viewBinding.apply {
            detailId.text = id.toString()
            detailTitle.text = title

            observeViewModel()

            if (thumbnailPath != null) {
                viewModel.refreshLocalThumbnail(thumbnailPath)
            }

            detailContainer.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun observeViewModel() {
        viewBinding.apply {
            viewModel.localThumbnail.observe(this@DetailActivity) {
                detailImage.setImageDrawable(it)
            }

            viewModel.loadingErrorMessage.observe(this@DetailActivity) { errorMessage ->
                loadingError.text = errorMessage
                loadingError.visibility =
                    if (TextUtils.isEmpty(errorMessage)) View.GONE else View.VISIBLE
            }

            viewModel.isThumbnailProcessing.observe(this@DetailActivity) { isLoading ->
                if (isLoading) {
                    loadingView.visibility = View.VISIBLE
                    detailId.visibility = View.GONE
                    detailTitle.visibility = View.GONE
                } else {
                    loadingView.visibility = View.GONE
                    detailId.visibility = View.VISIBLE
                    detailTitle.visibility = View.VISIBLE
                }
            }
        }
    }
}