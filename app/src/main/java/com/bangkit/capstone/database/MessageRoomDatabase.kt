package com.bangkit.capstone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1)
abstract class MessageRoomDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): MessageRoomDatabase {
            if (INSTANCE == null) {
                synchronized(MessageRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MessageRoomDatabase::class.java, "message_database")
                        .build()
                }
            }
            return INSTANCE as MessageRoomDatabase
        }
    }
}