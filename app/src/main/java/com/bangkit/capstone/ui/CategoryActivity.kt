package com.bangkit.capstone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.capstone.R

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        supportActionBar?.hide()
    }
}