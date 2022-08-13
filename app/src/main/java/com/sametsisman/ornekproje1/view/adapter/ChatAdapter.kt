package com.sametsisman.ornekproje1.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sametsisman.ornekproje1.databinding.ItemMessageBinding
import com.sametsisman.ornekproje1.databinding.ItemReceivedMessageBinding
import com.sametsisman.ornekproje1.view.model.ChatMessage

class ChatAdapter(val chatMessage: List<ChatMessage>, val senderId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val VIEW_TYPE_SENT = 1
    val VIEW_TYPE_RECEIVED = 2

    class SentMessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root){
        fun setData(chatMessage: ChatMessage){
            binding.textMessage.text = chatMessage.message
            binding.textDateTime.text = chatMessage.dateTime
        }
    }

    class ReceivedMessageViewHolder(val binding: ItemReceivedMessageBinding) : RecyclerView.ViewHolder(binding.root){
        fun setData(chatMessage: ChatMessage){
            binding.textReceivedMessage.text = chatMessage.message
            binding.textReceivedDateTime.text = chatMessage.dateTime
            binding.textReceivedUsername.text = chatMessage.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SENT){
            return SentMessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            return ReceivedMessageViewHolder(ItemReceivedMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            (holder as SentMessageViewHolder).setData(chatMessage[position])
        }else{
            (holder as ReceivedMessageViewHolder).setData(chatMessage[position])        }
    }

    override fun getItemCount(): Int {
        return chatMessage.size
    }

    override fun getItemViewType(position: Int): Int {
        if (chatMessage[position].senderId == senderId){
            return VIEW_TYPE_SENT
        }else{
            return VIEW_TYPE_RECEIVED
        }
    }
}