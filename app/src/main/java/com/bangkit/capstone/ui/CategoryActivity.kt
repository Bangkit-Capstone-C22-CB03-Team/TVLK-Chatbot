package com.bangkit.capstone.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        binding.cardView1.setOnClickListener { moveMain(0) }
        binding.cardView2.setOnClickListener { moveMain(1) }
        binding.cardView3.setOnClickListener { moveMain(2) }
        binding.cardView4.setOnClickListener { moveMain(3) }
        binding.cardView5.setOnClickListener { moveMain(4) }
        binding.cardView6.setOnClickListener { moveMain(5) }
    }

    private fun moveMain(cat: Int) {
        val intent = Intent(this@CategoryActivity, MainActivity::class.java)
        intent.putExtra(MainActivity.CATEGORY, cat)
        startActivity(intent)
        finish()
    }



}