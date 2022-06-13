package com.bangkit.capstone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert
    fun insert(message: Message)

    @Query("SELECT * From Message ORDER BY date ASC ")
    fun getAllMessages() : LiveData<List<Message>>

    @Query("DELETE FROM Message")
    fun deleteAllMessages()
}