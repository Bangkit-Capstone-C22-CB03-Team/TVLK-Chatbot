package com.bangkit.capstone.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(

    @ColumnInfo(name = "msg")
    var msg: String,

    @ColumnInfo(name = "role")
    var role:String,

    @ColumnInfo(name="date")
    var date: String,

    @PrimaryKey(autoGenerate = true)
    var id: Int
)
