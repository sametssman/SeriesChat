package com.sametsisman.ornekproje1.view.feed

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import com.sametsisman.ornekproje1.view.kayitgiris.MainActivity
import com.sametsisman.ornekproje1.view.kayitgiris.SignInActivity
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
   // private val movieAdapter = MovieAdapter(movieArrayList)
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
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
        //recyclerview.layoutManager = LinearLayoutManager(context)
        //recyclerview.adapter = movieAdapter

        auth = Firebase.auth

        button2.setOnClickListener {
            auth.signOut()
            val intent = Intent(this.context,SignInActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cikis){
            auth.signOut()
            val intent = Intent(this.context, SignInActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}