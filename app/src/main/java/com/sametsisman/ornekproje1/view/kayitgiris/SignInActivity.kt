package com.sametsisman.ornekproje1.view.kayitgiris

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.sametsisman.ornekproje1.view.feed.FeedActivity
import com.sametsisman.ornekproje1.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*
import kotlin.collections.ArrayList


class SignInActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var callbackManager : CallbackManager
    private lateinit var firestore : FirebaseFirestore
    private lateinit var userUuidList : ArrayList<String>
    private var isAlreadyRegistered = false

    private companion object{
        private const val RC_SIGN_IN = 107
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        optionsConstraintLayout.visibility = View.VISIBLE
        signInButton.visibility = View.GONE
        textFieldEmail1.visibility = View.GONE
        textFieldEmail1EditText.visibility = View.GONE
        textFieldPassword1EditText.visibility = View.GONE
        textFieldPassword1.visibility = View.GONE

        auth = Firebase.auth
        firestore = Firebase.firestore
        userUuidList = ArrayList()
        checkUser()


        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("2579575489-600hvaohur6pmsufgttse67r0ami1qeo.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,signInOptions)

        loginWithGoogleBtn.setOnClickListener {
            googleSignIn()
        }

        loginWithEmailBtn.setOnClickListener {
            goSignIn(it)
        }

        signInButton.setOnClickListener {
            signIn()
        }

        callbackManager = CallbackManager.Factory.create()


        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult) {
                println("yes")
                //handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                // App code
                println("cancel")
            }

            override fun onError(error: FacebookException) {
                // App code
                Log.d(TAG, "facebookRegisterCallbackError:" + error.message)
                println("no")
            }
        })

        loginWithFacebookBtn.setOnClickListener {
            facebookSignIn()
        }

    }


    private fun facebookSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(this@SignInActivity,Arrays.asList("email,public_profile"))
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //saveUser()
                    val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
                    preferences.edit().putString("senderId",auth.uid).apply()
                    updateUI(auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    saveUser()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
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

    private fun saveUser(){
        val user = auth.currentUser
        val email = user!!.email
        val id = user.uid
        firestore.collection("usersss")
            .get()
            .addOnSuccessListener {
                val documents = it.documents
                for (document in documents){
                    val uid = document.get("id") as String
                    if (uid == id){
                        isAlreadyRegistered = true
                        val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
                        preferences.edit().putString("senderDocumentId",document.id).apply()
                    }
                }

                if (!isAlreadyRegistered){
                    val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
                    preferences.edit().putString("senderId",id).apply()
                    FirebaseMessaging.getInstance().token.addOnSuccessListener {
                        val token = it
                        val userr = hashMapOf<String,Any>()
                        userr.put("email",email!!)
                        userr.put("id",id)
                        userr.put("fcmToken",token)
                        firestore.collection("usersss").add(userr).addOnSuccessListener {
                            userUuidList.clear()
                            updateUI(user)
                        }.addOnFailureListener {
                            Toast.makeText(applicationContext, it.getLocalizedMessage(), Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
                    preferences.edit().putString("senderId",id).apply()
                    userUuidList.clear()
                    updateUI(user)
                }
            }
    }

    fun goSignUp(view : View){
        val signUpIntent = Intent(this, SignUpActivity::class.java)
        startActivity(signUpIntent)
    }

    fun goSignIn(view: View){
        optionsConstraintLayout.visibility = View.GONE
        signInButton.visibility = View.VISIBLE
        textFieldEmail1.visibility = View.VISIBLE
        textFieldEmail1EditText.visibility = View.VISIBLE
        textFieldPassword1EditText.visibility = View.VISIBLE
        textFieldPassword1.visibility = View.VISIBLE
    }


    private fun signIn(){
        val email = textFieldEmail1EditText.text.toString()
        val password = textFieldPassword1EditText.text.toString()

        if(email.equals("") || password.equals("") ){
            Toast.makeText(applicationContext, "Username or password cannot be empty.", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                saveUser()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.getLocalizedMessage(), Toast.LENGTH_LONG).show()

            }
        }
    }
}