package com.sametsisman.ornekproje1.view.kayitgiris

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.feed.FeedActivity


class SplashFragment : Fragment() {

    private lateinit var  auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            if(auth.currentUser != null){
                findNavController().navigate(R.id.action_splashFragment_to_feedActivity)
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_signInActivity)
            }
            requireActivity().onBackPressed()
        },3000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)


    }
}