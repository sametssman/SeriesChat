package com.sametsisman.ornekproje1.view.feed

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.kayitgiris.MainActivity
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        auth = Firebase.auth
        homeFragment = HomeFragment()
        profileFragment = ProfileFragment()

        bottomView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            if (item.itemId == R.id.home) {
                fragmentAyarla(homeFragment)
            } else if (item.itemId == R.id.profile) {
                fragmentAyarla(profileFragment)
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

