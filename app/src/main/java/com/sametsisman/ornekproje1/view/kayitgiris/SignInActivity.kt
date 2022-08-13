package com.sametsisman.ornekproje1.view.kayitgiris

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.view.feed.FeedActivity
import com.sametsisman.ornekproje1.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlin.collections.ArrayList


class SignInActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var userUuidList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        firestore = Firebase.firestore
        userUuidList = ArrayList()
        checkUser()

        textFieldPassword1EditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    signIn()
                    return true
                }
                return false
            }
        })

        signInButton.setOnClickListener {
            signIn()
        }

        signUpText.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        forgotPassword.setOnClickListener {
            val editText = EditText(it.context)
            editText.textAlignment = View.TEXT_ALIGNMENT_CENTER
            AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Enter your email")
                .setView(editText)
                .setNeutralButton("Send", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if (editText.text.toString().isNotEmpty() and Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()){
                            auth.sendPasswordResetEmail(editText.text.toString()).addOnSuccessListener {
                                Toast.makeText(this@SignInActivity,"Check your email to reset your password",Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this@SignInActivity,"Try Again! Something wrong happened!",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@SignInActivity,"Please provide valid email.",Toast.LENGTH_SHORT).show()
                        }

                    }

                }).show()
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            startActivity(Intent(this, FeedActivity::class.java))
            finish()
        }

    }

    fun checkUser(){
        val curretUser = auth.currentUser

        if(curretUser != null){
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn(){
        val email = textFieldEmail1EditText.text.toString()
        val password = textFieldPassword1EditText.text.toString()

        if(email.equals("") || password.equals("") ){
            Toast.makeText(applicationContext, "Username or password cannot be empty.", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val user = auth.currentUser
                val id = user!!.uid
                val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
                preferences.edit().putString("senderId",id).apply()
                if (user.isEmailVerified){
                    updateUI(user)
                }else{
                    AlertDialog.Builder(this)
                        .setTitle("Attention")
                        .setMessage("You must verify your e-mail address.")
                        .setNeutralButton("Resend verification link",object : DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                user.sendEmailVerification()
                            }

                        }).show()
                    auth.signOut()
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.getLocalizedMessage(), Toast.LENGTH_LONG).show()

            }
        }
    }
}