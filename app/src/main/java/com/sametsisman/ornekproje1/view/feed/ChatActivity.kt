package com.sametsisman.ornekproje1.view.feed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.adapter.ChatAdapter
import com.sametsisman.ornekproje1.view.model.ChatMessage
import com.sametsisman.ornekproje1.view.model.Room
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var receiverRoom : Room
    private lateinit var senderId :String
    private lateinit var chatMessages: ArrayList<ChatMessage>
    private lateinit var chatAdapter : ChatAdapter
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        init()

        sendButton.setOnClickListener {
            sendMessage()
        }

        firestore.collection("rooms").document(receiverRoom.id).collection("chat").addSnapshotListener { value, error ->
            if (error != null){
                Log.d("SnapshotListenerError",error.message.toString())
            }
            if (value != null){
                val count = chatMessages.size
                for(changes in value.documentChanges){
                    if (changes.type == DocumentChange.Type.ADDED){

                        val changeSenderId = changes.document.getString("senderId")!!
                        val changeReceiverId = changes.document.getString("receiverId")
                        val changemessage = changes.document.getString("message")
                        val changeDateTime = getReadableDateTime(changes.document.getDate("dateTime")!!)
                        val changeDateObject = changes.document.getDate("dateTime")
                        val chatMessage = ChatMessage(changeSenderId,changeReceiverId!!,changemessage!!,changeDateTime,changeDateObject!!)
                        chatMessages.add(chatMessage)
                    }
                }
                val list = chatMessages.sortedBy { it.dateObject }
                chatAdapter = ChatAdapter(list,senderId)
                recyclerview_chat.adapter = chatAdapter

                if (count == 0){
                    chatAdapter.notifyDataSetChanged()
                }else{
                    chatAdapter.notifyItemRangeInserted(chatMessages.size,chatMessages.size)
                    recyclerview_chat.smoothScrollToPosition(chatMessages.size-1)
                }
            }
        }
    }


    private fun sendMessage(){
        val message = hashMapOf<String,Any>()
        message.put("senderId",senderId)
        message.put("receiverId",receiverRoom.id)
        message.put("message",inputMessage.text.toString())
        message.put("dateTime", Calendar.getInstance().time)
        firestore.collection("rooms").document(receiverRoom.id).collection("chat").add(message)
        inputMessage.text = null
    }

    private fun init(){
        firestore = Firebase.firestore
        receiverRoom = intent.extras!!.get("receiverUser") as Room
        chatMessages = ArrayList()
        senderId = getSharedPreferences("preferences", MODE_PRIVATE).getString("senderId","")!!
        receiverNameText.text = receiverRoom.roomName
    }

    private fun getReadableDateTime(date : Date) : String{
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a",Locale.getDefault()).format(date)
    }


}