package com.sametsisman.ornekproje1.view.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.adapter.UserAdapter
import com.sametsisman.ornekproje1.view.model.Room
import kotlinx.android.synthetic.main.fragment_message.*

class MessageFragment : Fragment() {
    private lateinit var roomList : ArrayList<Room>
    private lateinit var firestore: FirebaseFirestore
    private val userAdapter = UserAdapter()
    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomList = ArrayList()

        firestore = Firebase.firestore

        userId = requireActivity().getSharedPreferences("preferences", AppCompatActivity.MODE_PRIVATE).getString("senderId","")!!

        getRooms()

        userListRecyclerView.layoutManager = LinearLayoutManager(context)
        userListRecyclerView.adapter = userAdapter
    }

    private fun getRooms() {
        firestore.collection("usersss").document(userId).collection("registeredRooms")
            .get()
            .addOnSuccessListener {
                val documents = it.documents
                for (document in documents){
                    val roomName = document.getString("roomName")!!
                    val roomImageUrl = document.getString("roomImageUrl")!!
                    val id = document.id
                    val room = Room(roomName,roomImageUrl,id)
                    roomList.add(room)
                    val sortedList = ArrayList(roomList.sortedBy { it.roomName })
                    userAdapter.setList(sortedList)
                }
            }
    }
}