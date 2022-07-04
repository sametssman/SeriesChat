package com.sametsisman.ornekproje1.view.kayitgiris

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
    }

    fun signUp(view : View){
        val email = textFieldEmailEditText.text.toString()
        val password = textFieldPasswordEditText.text.toString()

        if(email.equals("") || password.equals("") ){
            Toast.makeText(applicationContext, "Username or password cannot be empty.", Toast.LENGTH_LONG).show()

        } else {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                val intent = Intent(this, MainActivity::class.java)
                Toast.makeText(applicationContext, "Registration Successful.", Toast.LENGTH_LONG).show()
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.getLocalizedMessage(), Toast.LENGTH_LONG).show()

            }
        }
    }
}