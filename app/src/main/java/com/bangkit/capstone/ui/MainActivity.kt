package com.bangkit.capstone.ui
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.capstone.R
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.clear -> {
                mainViewModel.deleteAll()
                return true
            }
            else -> return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val category = intent.getIntExtra(CATEGORY, 0)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        //ini nanti buat accessbility yakin gas?
        binding?.buttonsContainer?.isVisible = false

        //Set Custom Action Bar
        supportActionBar?.show()
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_toolbar)



        networkCheck()
        setAdapter()

        binding?.loading?.animateVisibility(false)
        binding?.sendButtonChatLog?.setOnClickListener {
            binding?.loading?.visibility = View.VISIBLE
            message = binding?.edittextChatLog?.text.toString()
            mainViewModel.isLoading.observe(this){
                binding?.loading?.animateVisibility(it)
            }
            val msg = message.trim()
            if(binding?.isConnected?.visibility == View.GONE){
                if (msg =="exit"||msg=="quit"||msg=="keluar"){
                    moveToCategory()
                }else {
                    if (msg!=""){
                        userChat(msg)
                        mainViewModel.getBotResponse(msg, category)
                        binding?.edittextChatLog?.setText("")
                    }else{
                        Toast.makeText(this, "tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.connection_dialog_title))
                    .setContentText(getString(R.string.connection_dialog_content))
                    .setConfirmText(getString(R.string.connection_dialog_confirm))
                    .show()
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


    private fun moveToCategory() {
        val intent = Intent(this, CategoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val CATEGORY = "category"
    }



}