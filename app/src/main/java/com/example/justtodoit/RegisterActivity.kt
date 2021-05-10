package com.example.justtodoit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth= FirebaseAuth.getInstance()
    }

    fun confirm(view: View) {
        var email = findViewById<EditText>(R.id.createEmail)
        var password = findViewById<EditText>(R.id.createPassword)
        register(email.text.toString(),password.text.toString())
    }

    private fun register(email: String, password: String) {
        var invalid = findViewById<TextView>(R.id.textView3)
        try{
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
            if (task.isSuccessful){
                database = FirebaseDatabase.getInstance()
                myRef = database.getReference("users")
                auth= FirebaseAuth.getInstance()
                var type = "user"
                var details = userDetails(email,type)
                myRef.child(auth.currentUser.uid).setValue(details)
                startActivity(Intent(this,LoginActivity::class.java))
            }
            else {
                invalid.text="Invalid Account"
            }
            }
        }
        catch (e: Exception){
            invalid.text="Enter Email and Password"
        }
    }

    fun back(view: View) {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    //sets columns names when added to realtime database
    class userDetails(email: String, type: String) {
        var email=email
        var type=type

    }


}