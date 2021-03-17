package com.mountain96.random

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.mountain96.random.ui.foods.InitSettings
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

class SplashActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(object: Runnable {
            override fun run() {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }, 2000)
    }

    inner class RandomButtonThread(val pressedTime: Long, val progressBar: ProgressBar) : Thread() {

        override fun run() {


        }
    }
}