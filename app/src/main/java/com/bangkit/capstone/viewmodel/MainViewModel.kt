package com.bangkit.capstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.capstone.database.Message
import com.bangkit.capstone.database.MessageRepository
import com.bangkit.capstone.helper.DateHelper
import com.bangkit.capstone.network.ApiConfig
import com.bangkit.capstone.network.BotResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : ViewModel() {

    private val mMessageRepository: MessageRepository = MessageRepository(application)

    fun getBotResponse(message:String){
        val client = ApiConfig.getApiService().getResponse(message = message)
        client.enqueue(object : Callback<BotResponse>{
            override fun onResponse(call: Call<BotResponse>, response: Response<BotResponse>) {
                if (response.isSuccessful){
                    val msg = response.body()!!.botResponse
                    insert(Message(msg,"bot",DateHelper.getCurrentDate(),0))
                }
            }

            override fun onFailure(call: Call<BotResponse>, t: Throwable) {
                Log.d("error get bot response", t.message.toString())
            }

        }
        )
    }

    fun insert(message: Message){
        mMessageRepository.insert(message)
    }

    fun getAllMessages(): LiveData<List<Message>> = mMessageRepository.getAllMessages()
}