package com.instagramreel.ui.ui.scout.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.instagramreel.R
import com.instagramreel.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private var binding : ActivitySplashScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        initUI()

    }

    private fun initUI() {
        Handler(Looper.getMainLooper()).postDelayed({
          startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        },2500)

    }
}