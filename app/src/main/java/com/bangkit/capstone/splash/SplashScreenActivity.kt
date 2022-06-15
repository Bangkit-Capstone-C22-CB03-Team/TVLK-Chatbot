package com.bangkit.capstone.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.capstone.R
import com.bangkit.capstone.ui.AccessbilityActivity
import java.util.*
import kotlin.concurrent.schedule

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        val intent = Intent(this, AccessbilityActivity::class.java)
        Timer("SettingUp", false).schedule(2000) {
            startActivity(intent)
            finish()
        }
    }
}