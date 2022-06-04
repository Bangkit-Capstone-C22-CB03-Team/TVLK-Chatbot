package com.bangkit.capstone.ui
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.database.Message
import com.bangkit.capstone.databinding.ActivityMainBinding
import com.bangkit.capstone.helper.DateHelper
import com.bangkit.capstone.viewmodel.CheckInternetAccess
import com.bangkit.capstone.viewmodel.MainViewModel
import com.bangkit.capstone.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel : MainViewModel
    private lateinit var message: String
    private lateinit var adapter: ChatAdapter
    private lateinit var checkInternetAccess: CheckInternetAccess
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        networkCheck()
        setAdapter()

        binding?.loading?.animateVisibility(false)
        binding?.loading?.visibility = View.VISIBLE
        binding?.sendButtonChatLog?.setOnClickListener {
            message = binding?.edittextChatLog?.text.toString()
            mainViewModel.isLoading.observe(this){
                binding?.loading?.animateVisibility(it)
            }
            val msg = message.trim()
            if(binding?.isConnected?.visibility == View.GONE){
                if (msg != ""){
                    userChat(msg)
                    mainViewModel.getBotResponse(msg)
                    binding?.edittextChatLog?.setText("")
                }
            }else{
                Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.getAllMessages().observe(this){
            adapter.setList(it)
            binding?.recyclerviewChat?.scrollToPosition(adapter.itemCount-1)
        }

    }
    private fun userChat(message: String){
        mainViewModel.insert(
            Message(message, "user",DateHelper.getCurrentDate(),0)
        )
    }
    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }
    private fun setAdapter(){
        adapter = ChatAdapter()
        binding?.recyclerviewChat?.layoutManager = LinearLayoutManager(this)
        binding?.recyclerviewChat?.adapter = adapter
        mainViewModel = obtainViewModel(this)
    }

    fun networkCheck(){
        checkInternetAccess = CheckInternetAccess(application)
        checkInternetAccess.observe(this){isConnected->
            if(isConnected){
                binding?.isConnected?.visibility = View.GONE
            }else{
                binding?.isConnected?.visibility = View.VISIBLE
            }
        }
    }

    fun View.animateVisibility(isVisible: Boolean, duration: Long = 1000) {
        ObjectAnimator
            .ofFloat(this, View.ALPHA, if (isVisible) 1f else 0f)
            .setDuration(duration)
            .start()
    }



}