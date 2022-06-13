package com.bangkit.capstone.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MessageRepository(application: Application) {

    private val mMessageDao: MessageDao
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = MessageRoomDatabase.getDatabase(application)
        mMessageDao =db.messageDao()
    }

    fun getAllMessages(): LiveData<List<Message>> = mMessageDao.getAllMessages()

    fun insert(message: Message){
        executorService.execute { mMessageDao.insert(message) }
    }

    fun deleteAll(){
        executorService.execute { mMessageDao.deleteAllMessages() }
    }

}