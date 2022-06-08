package com.bangkit.capstone.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.bangkit.capstone.R
import com.bangkit.capstone.databinding.ActivityOnboardingBinding
import com.bangkit.capstone.ui.MainActivity
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var checker: Checker
    private var isDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checker = Checker(applicationContext)

        binding.finishBtn.setOnClickListener {
            setDone()
            moveActivity()
        }
        // Action Bar Hide
        supportActionBar?.hide()
    }

    private fun setDone() {
        lifecycleScope.launch {
            when(isDone) {
                false -> checker.setDone(PassChecker.DONE)
            }
        }
    }
    private fun moveActivity() {
        val intent = Intent(this, MainActivity::class.java)
        Timer("SettingUp", false).schedule(500) {
            startActivity(intent)
        }
    }
}