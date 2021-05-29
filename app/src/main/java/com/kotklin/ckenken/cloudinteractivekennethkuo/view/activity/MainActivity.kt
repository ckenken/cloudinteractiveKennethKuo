package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotklin.ckenken.cloudinteractivekennethkuo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ListingActivity::class.java)
            startActivity(intent)
        }
    }
}