package com.kotklin.ckenken.cloudinteractivekennethkuo.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotklin.ckenken.cloudinteractivekennethkuo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater, findViewById(android.R.id.content), false).also {
            viewBinding = it
            setContentView(it.root)
        }

        viewBinding.startButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ListingActivity::class.java)
            startActivity(intent)
        }
    }

}