package com.example.justtodoit

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.justtodoit.ThemeActivity.Companion.sharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    private val CHANNEL_ID = "channel_id"
    private val notificationId = 101

    companion object{
        private const val RC_SIGN_IN = 120
    }
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onStart() {
        super.onStart()
        /**Checks if a google user is signed in*/
        //auth.signOut()

        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        createNotificationChannel()

        /** Configure Google Sign In */
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Firebase Auth Instance
        auth = FirebaseAuth.getInstance()

        var signInBtn = findViewById<SignInButton>(R.id.sign_in_button)
        signInBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        var incorrect = findViewById<TextView>(R.id.textView4)
        try {
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val exception = task.exception
                if (task.isSuccessful) {
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.id)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        Log.w("LoginActivity", "Google sign in failed", e)
                        throw Exception(e.message)
                    }
                } else {
                    Log.w("LoginActivity", exception.toString())
                    throw Exception(exception.toString())
                    incorrect.text="Gmail Sign In Failed"
                }
            }
        }
        catch (e: Exception){
            incorrect.text="Gmail Sign In Failed"
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginActivity", "signInWithCredential:success")
                    database = FirebaseDatabase.getInstance()
                    myRef = database.getReference("users")
                    auth= FirebaseAuth.getInstance()
                    var type = "user"
                    var details = RegisterActivity.userDetails(auth.currentUser.email, type)
                    myRef.child(auth.currentUser.uid).setValue(details)
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                    throw Exception("Not Working?")
                }
            }
    }


    fun confirm(view: View){
        var email = findViewById<EditText>(R.id.editEmail)
        var password = findViewById<EditText>(R.id.editPassword)
        auth= FirebaseAuth.getInstance()
        loginUser(email.text.toString(),password.text.toString())
    }

    private fun loginUser(email: String, password: String){

        var incorrect = findViewById<TextView>(R.id.textView4)
        try {
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                startActivity(Intent(this, HomePageActivity::class.java))
                finish()
            }.addOnFailureListener { incorrect.text="Invalid Credentials" }

        }
        catch (e: Exception){
            incorrect.text="Enter Email & Password"
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            channel.enableVibration(true)

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun register(view: View) {
        startActivity(Intent(this,RegisterActivity::class.java))
        finish()
    }

    private fun signOut() {
        googleSignInClient.signOut()
    }
}
