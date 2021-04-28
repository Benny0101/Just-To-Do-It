package com.example.justtodoit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
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
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
            if (task.isSuccessful){
                startActivity(Intent(this,LoginActivity::class.java))
            }
            else {
                println(task.exception?.message)
            }
        }
    }


}