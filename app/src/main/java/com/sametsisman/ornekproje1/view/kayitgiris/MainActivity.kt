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
import com.google.firebase.ktx.Firebase
import com.sametsisman.ornekproje1.view.feed.FeedActivity
import com.sametsisman.ornekproje1.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var callbackManager : CallbackManager

    private companion object{
        private const val RC_SIGN_IN = 107
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        optionsConstraintLayout.visibility = View.VISIBLE
        signInButton.visibility = View.GONE
        textFieldEmail1.visibility = View.GONE
        textFieldEmail1EditText.visibility = View.GONE
        textFieldPassword1EditText.visibility = View.GONE
        textFieldPassword1.visibility = View.GONE

        auth = Firebase.auth
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

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

        loginWithFacebookBtn.setOnClickListener {
            facebookSignIn()
        }

    }


    private fun facebookSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(this@MainActivity,Arrays.asList("email","public_profile"))
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
                    val user = auth.currentUser
                    updateUI(user)
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
                    val user = auth.currentUser
                    updateUI(user)

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


    fun signIn(view: View){
        val email = textFieldEmail1EditText.text.toString()
        val password = textFieldPassword1EditText.text.toString()

        if(email.equals("") || password.equals("") ){
            Toast.makeText(applicationContext, "Username or password cannot be empty.", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                val intent = Intent(this, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.getLocalizedMessage(), Toast.LENGTH_LONG).show()

            }
        }
    }
}