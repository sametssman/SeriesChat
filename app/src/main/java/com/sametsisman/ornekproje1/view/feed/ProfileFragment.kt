package com.sametsisman.ornekproje1.view.feed

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.kayitgiris.SignInActivity
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId : String
    private lateinit var email : String
    private lateinit var username : String
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore
        profileLoading.visibility = View.VISIBLE
        profile_ui.visibility = View.GONE
        preferences = requireActivity().getSharedPreferences("preferences", AppCompatActivity.MODE_PRIVATE)

        userId = preferences.getString("senderId","")!!

        firestore.collection("usersss").document(userId)
            .get()
            .addOnSuccessListener {
                profileLoading.visibility = View.GONE
                profile_ui.visibility = View.VISIBLE
                email = it.getString("email")!!
                username = it.getString("username")!!
                profileEmailText.text = email
                profieUsernameText.text = username
            }


        button2.setOnClickListener {
            auth.signOut()
            val intent = Intent(this.context,SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}