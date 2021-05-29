package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.kotklin.ckenken.cloudinteractivekennethkuo.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_THUMBNAIL = "thumbnail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val id = intent.getIntExtra(KEY_ID, 0)
        val title = intent.getStringExtra(KEY_TITLE)
        val thumbnailPath = intent.getStringExtra(KEY_THUMBNAIL)

        detailId.text = id.toString()
        detailTitle.text = title
        if (!TextUtils.isEmpty(thumbnailPath)) {
            val bitmap = BitmapFactory.decodeFile(thumbnailPath)
            detailImage.setImageBitmap(bitmap)
        }
    }
}