package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.kotklin.ckenken.cloudinteractivekennethkuo.R
import com.kotklin.ckenken.cloudinteractivekennethkuo.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_THUMBNAIL = "thumbnail"
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val id = intent.getIntExtra(KEY_ID, 0)
        val title = intent.getStringExtra(KEY_TITLE)
        val thumbnailPath = intent.getStringExtra(KEY_THUMBNAIL)

        detailId.text = id.toString()
        detailTitle.text = title

        viewModel.localThumbnail.observe(this, {
            detailImage.setImageDrawable(it)
        })

        viewModel.refreshLocalThumbnail(this, thumbnailPath!!)

        viewModel.loadingErrorMessage.observe(this, { errorMessage ->
            loadingError.text = errorMessage
            loadingError.visibility = if(TextUtils.isEmpty(errorMessage)) View.GONE else View.VISIBLE
        })

        viewModel.isThumbnailProcessing.observe(this) { isLoading ->
            loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                detailId.visibility = View.GONE
                detailTitle.visibility = View.GONE
            } else {
                detailId.visibility = View.VISIBLE
                detailTitle.visibility = View.VISIBLE
            }
        }

        detailContainer.setOnClickListener {
            onBackPressed()
        }
    }
}