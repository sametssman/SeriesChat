package com.sametsisman.ornekproje1.view.kayitgiris

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.sametsisman.ornekproje1.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        firestore = Firebase.firestore

        signUpButton.setOnClickListener {
            signUp()
        }

        textFieldPasswordEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    signUp()
                    return true
                }
                return false
            }
        })
    }

    private fun signUp(){
        val email = textFieldEmailEditText.text.toString()
        val password = textFieldPasswordEditText.text.toString()
        val username = textFieldUsernameEditText.text.toString()

        val query = firestore.collection("usersss").whereEqualTo("username",username)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val uname = document.getString("username")
                    if (uname!! == username) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Username already exists.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
            if (task.getResult().size() == 0) {

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(
                        applicationContext,
                        "Username or password cannot be empty.",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                        auth.currentUser!!.sendEmailVerification()
                        Toast.makeText(
                            applicationContext,
                            "Registration Successful. We have sent a verification link to your email." +
                                    "\n(Please check your spam box.)",
                            Toast.LENGTH_LONG
                        ).show()
                        val id = auth.currentUser!!.uid
                        FirebaseMessaging.getInstance().token.addOnSuccessListener {
                            val token = it
                            val userr = hashMapOf<String, Any>()
                            userr.put("email", email)
                            userr.put("username", username)
                            userr.put("id", id)
                            userr.put("fcmToken", token)
                            firestore.collection("usersss").document(id).set(userr)
                                .addOnSuccessListener {
                                    auth.signOut()
                                    val intent =
                                        Intent(this@SignUpActivity, SignInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }.addOnFailureListener {
                                Toast.makeText(
                                    applicationContext,
                                    it.getLocalizedMessage(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            applicationContext,
                            it.getLocalizedMessage(),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
        }


    }
}