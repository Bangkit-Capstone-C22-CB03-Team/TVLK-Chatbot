package com.bangkit.capstone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.capstone.R
import com.bangkit.capstone.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setCardClick()
    }

    private fun setCardClick() {
        binding.cardView1.setOnClickListener { moveMain() }
        binding.cardView2.setOnClickListener { moveMain() }
        binding.cardView3.setOnClickListener { moveMain() }
        binding.cardView4.setOnClickListener { moveMain() }
        binding.cardView5.setOnClickListener { moveMain() }
        binding.cardView6.setOnClickListener { moveMain() }
    }

    private fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}