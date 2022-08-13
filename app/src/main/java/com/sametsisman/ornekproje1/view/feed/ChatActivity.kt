package com.sametsisman.ornekproje1.view.feed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.adapter.ChatAdapter
import com.sametsisman.ornekproje1.view.model.ChatMessage
import com.sametsisman.ornekproje1.view.model.Room
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.user_list_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var receiverRoom : Room
    private lateinit var senderId :String
    private lateinit var chatMessages: ArrayList<ChatMessage>
    private lateinit var chatAdapter : ChatAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var username : String


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
                        val changeUsername = changes.document.getString("username")
                        val changemessage = changes.document.getString("message")
                        val changeDateTime = getReadableDateTime(changes.document.getDate("dateTime")!!)
                        val changeDateObject = changes.document.getDate("dateTime")
                        val chatMessage = ChatMessage(changeSenderId,changeReceiverId!!,changeUsername!!,changemessage!!,changeDateTime,changeDateObject!!)
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
        message.put("username",username)
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
        firestore.collection("usersss").document(senderId)
            .get()
            .addOnSuccessListener {
                username = it.getString("username")!!
            }
        receiverNameText.text = receiverRoom.roomName
        Glide.with(this)
            .load(receiverRoom.roomImageUrl)
            .into(chatImageView)

        inputMessage.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    sendMessage()
                    return true
                }
                return false
            }
        })

        receiverNameText.setOnClickListener {
            val intent = Intent(this,DetailActivity::class.java)
            intent.putExtra("id",receiverRoom.id.toInt())
            startActivity(intent)
        }
    }

    private fun getReadableDateTime(date : Date) : String{
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a",Locale.getDefault()).format(date)
    }


}