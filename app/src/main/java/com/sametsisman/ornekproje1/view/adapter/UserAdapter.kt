package com.sametsisman.ornekproje1.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.feed.ChatActivity
import com.sametsisman.ornekproje1.view.model.Room
import kotlinx.android.synthetic.main.user_list_row.view.*
import java.io.Serializable

class UserAdapter() : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    var roomList : ArrayList<Room> = ArrayList()

    class UserHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.user_list_row,parent,false)
        return UserHolder(view)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.view.userEmailTxt.text = roomList[position].roomName

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.view.context, ChatActivity::class.java)
            intent.putExtra("receiverUser",roomList[position] as Serializable)
            holder.view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int { return roomList.size }

    fun setList(arraylist : ArrayList<Room>){
        roomList = arraylist
        notifyDataSetChanged()
    }

}