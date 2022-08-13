package com.sametsisman.ornekproje1.view.kayitgiris

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import kotlinx.android.synthetic.main.fragment_connection.*

class ConnectionFragment : Fragment() {
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        connectButton.setOnClickListener {
            val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager)
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null){
                if(auth.currentUser != null){
                    findNavController().navigate(R.id.action_connectionFragment_to_feedActivity)
                }else{
                    findNavController().navigate(R.id.action_connectionFragment_to_signInActivity)
                }
            }
        }

    }
}