package com.bangkit.capstone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.database.Message
import com.bangkit.capstone.helper.DiffCallback
import java.util.ArrayList

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){
    private val listChat = ArrayList<Message>()
    private val BOT_MESSAGE_TYPE = 0
    private val USER_MESSAGE_TYPE = 1
    fun setList(listChat: List<Message>){
        val diffCallback = DiffCallback(this.listChat, listChat)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listChat.clear()
        this.listChat.addAll(listChat)
        diffResult.dispatchUpdatesTo(this)
    }
    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMsg: TextView = view.findViewById(R.id.tv_msg)
        val tvTime: TextView = view.findViewById(R.id.tv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if (viewType == BOT_MESSAGE_TYPE){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.bot_message,parent,false)
            return ChatViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.message_bubble,parent,false)
            return ChatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = listChat[position]
        holder.tvMsg.text = chat.msg
        holder.tvTime.text = chat.date
    }

    override fun getItemCount(): Int {
        return listChat.size
    }

    override fun getItemViewType(position: Int): Int {
        if(listChat[position].role == "user"){
            return USER_MESSAGE_TYPE
        }else{
            return BOT_MESSAGE_TYPE
        }
    }
}