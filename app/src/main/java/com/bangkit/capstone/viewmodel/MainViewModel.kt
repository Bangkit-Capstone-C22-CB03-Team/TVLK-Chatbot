package com.bangkit.capstone.viewmodel

import android.app.Application
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.database.Message
import com.bangkit.capstone.database.MessageRepository
import com.bangkit.capstone.helper.DateHelper
import com.bangkit.capstone.network.ApiConfig
import com.bangkit.capstone.network.BotResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel(application: Application) : ViewModel() {

    private lateinit var textToSpeechEngine: TextToSpeech
    private lateinit var startForResult: ActivityResultLauncher<Intent>


    private val mMessageRepository: MessageRepository = MessageRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _textMessage = MutableLiveData<String>()
    val textMessage: LiveData<String> = _textMessage


    fun getBotResponse(message:String, categid:Int){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getResponse(message,  categid)
        client.enqueue(object : Callback<BotResponse>{
            override fun onResponse(call: Call<BotResponse>, response: Response<BotResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    val msg = response.body()!!.botResponse
                    insert(Message(msg,"bot",DateHelper.getCurrentDate(),0))
                    _textMessage.value = msg
                    Log.d("spekingtok", "${textMessage.value}")

                }
            }

            override fun onFailure(call: Call<BotResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("error get bot response", t.message.toString())
                insert(Message("Bot sedang error silahkan tunggu","bot",DateHelper.getCurrentDate(),0))
            }

        }
        )
    }

    fun insert(message: Message){
        mMessageRepository.insert(message)
    }

    fun deleteAll(){
        mMessageRepository.deleteAll()
    }


    fun getAllMessages(): LiveData<List<Message>> = mMessageRepository.getAllMessages()


    fun initialForSpeech(
        engine: TextToSpeech, launcher: ActivityResultLauncher<Intent>
    ) = viewModelScope.launch {
        textToSpeechEngine = engine
        startForResult = launcher
    }

    fun displaySpeechRecognizer() {
        startForResult.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("in_ID"))
            putExtra(RecognizerIntent.EXTRA_PROMPT, Locale("Bicara sekarang"))
        })
    }

    fun speak(text: String) = viewModelScope.launch{
        textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


}