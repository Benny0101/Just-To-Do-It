package com.example.justtodoit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
            }.addOnFailureListener { incorrect.text="VERY WRONG" }

        }
        catch (e: Exception){
            incorrect.text="WRONG"
        }
    }

    fun register(view: View) {
        startActivity(Intent(this,RegisterActivity::class.java))
        finish()
    }


}