package com.sametsisman.ornekproje1.view.feed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var messageFragment: MessageFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        auth = Firebase.auth
        homeFragment = HomeFragment()
        profileFragment = ProfileFragment()
        messageFragment = MessageFragment()
        searchFragment = SearchFragment()
        fragmentAyarla(homeFragment)

        bottomView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            if (item.itemId == R.id.home) {
                fragmentAyarla(homeFragment)
            } else if (item.itemId == R.id.profile) {
                fragmentAyarla(profileFragment)
            } else if (item.itemId == R.id.search){
                fragmentAyarla(searchFragment)
            } else if (item.itemId == R.id.messagee){
                fragmentAyarla(messageFragment)
            }
            false
        })

    }


    fun fragmentAyarla(fragment: Fragment?) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment!!)
        fragmentTransaction.commit()
    }



}

