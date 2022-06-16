package com.bangkit.capstone.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.R
import com.bangkit.capstone.database.Message
import com.bangkit.capstone.helper.DiffCallback

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){
    private val listChat = ArrayList<Message>()
    private val BOT = 0
    private val USER = 1
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
        val chatBubble: View = view.findViewById(R.id.chat_bubble)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if (viewType == BOT){
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
        holder.chatBubble.contentDescription = chat.msg
    }

    override fun getItemCount(): Int {
        return listChat.size
    }

    override fun getItemViewType(position: Int): Int {
        if(listChat[position].role == "user"){
            return USER
        }else{
            return BOT
        }
    }
}