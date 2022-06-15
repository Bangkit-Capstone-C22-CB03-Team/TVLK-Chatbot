package com.bangkit.capstone.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.bangkit.capstone.databinding.ActivityAccessbilityBinding
import com.bangkit.capstone.splash.Checker
import com.bangkit.capstone.splash.OnboardingActivity
import com.bangkit.capstone.splash.PassChecker

class AccessbilityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccessbilityBinding
    private lateinit var checker: Checker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessbilityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        bindingButton()
        checker = Checker(applicationContext)
        observeOnboard()
    }

    //Splash Stuff
    private fun observeOnboard() {
        checker.isDoneFlow.asLiveData().observe(this) {
                passChecker ->
            passChecker?.let {
                when(passChecker) {
                    PassChecker.UNDONE -> moveOnboarding()
                }
            }
        }
    }
    private fun moveOnboarding() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun bindingButton() {
        val intent = Intent(this, CategoryActivity::class.java)


        binding.textBtn.setOnClickListener {
            intent.putExtra(CategoryActivity.ACCESSBILITY, false)
            startActivity(intent)
        }

        binding.voiceBtn.setOnClickListener {
            intent.putExtra(CategoryActivity.ACCESSBILITY, true)
            startActivity(intent)
        }
    }


}