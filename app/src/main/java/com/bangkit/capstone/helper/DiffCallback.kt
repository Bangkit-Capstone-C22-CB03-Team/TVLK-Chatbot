package com.bangkit.capstone.helper

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.capstone.database.Message

class DiffCallback(private val mOldList: List<Message>, private val mNewList: List<Message>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].id == mNewList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldList[oldItemPosition]
        val newEmployee = mNewList[newItemPosition]
        return oldEmployee.id == newEmployee.id && oldEmployee.msg == newEmployee.msg
    }
}