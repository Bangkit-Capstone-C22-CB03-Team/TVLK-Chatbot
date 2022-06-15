package com.bangkit.capstone.ui
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel : MainViewModel
    private lateinit var message: String
    private lateinit var adapter: ChatAdapter
    private lateinit var checkInternetAccess: CheckInternetAccess
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private var  category : Int by Delegates.notNull()
    private var isAccessbility : Boolean by Delegates.notNull()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        category = intent.getIntExtra(CATEGORY, 0)
        isAccessbility = intent.getBooleanExtra(ACCESSBILITY, true)

        if(isAccessbility){
            binding?.buttonsContainer?.isVisible = false
            binding?.blindContainer?.isVisible = true
        }else{
            binding?.buttonsContainer?.isVisible = true
            binding?.blindContainer?.isVisible = false
        }



        //Set Custom Action Bar
        supportActionBar?.show()
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_toolbar)


        setViewModel()
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
                if (msg!=""){
                    userChat(msg)
                    mainViewModel.getBotResponse(msg, category,isAccessbility)
                    binding?.edittextChatLog?.setText("")
                }else{
                    if (isAccessbility){
                        mainViewModel.speak("Tidak boleh kosong")
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

        if(isAccessbility){
            mainViewModel.textMessage.observe(this){
                speak(it)
            }
        }

        binding?.askButton?.setOnClickListener {
            mainViewModel.displaySpeechRecognizer()
        }

        binding?.backButton?.setOnClickListener {
            moveToCategory()
        }

        mainViewModel.getAllMessages().observe(this){
            adapter.setList(it)
            binding?.recyclerviewChat?.scrollToPosition(adapter.itemCount-1)
        }

    }

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

    }

    private fun setViewModel(){
        mainViewModel = obtainViewModel(this)
        mainViewModel.initialForSpeech(textToSpeechEngine,startForResult)
    }

    private fun networkCheck(){
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


    //Accessibility
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                .let { text-> text?.get(0) }

            if(spokenText!=null){
                userChat(spokenText)
                mainViewModel.getBotResponse(spokenText, category,isAccessbility)
            }
        }
    }

    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) textToSpeechEngine.language = Locale("in_ID")
        }
    }

    private fun speak(msg: String){
        mainViewModel.speak(msg)
    }


    companion object {
        const val CATEGORY = "category"
        const val ACCESSBILITY = "ACCESSBILITY"
    }



}