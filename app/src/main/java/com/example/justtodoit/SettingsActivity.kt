package com.example.justtodoit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
    }

    fun home(view: View) {
        startActivity(Intent(this,HomePageActivity::class.java))
        finish()
    }

    fun add(view: View) {
        startActivity(Intent(this,AddTaskActivity::class.java))
        finish()
    }

    fun plus(view: View) {
        startActivity(Intent(this, MembershipActivity::class.java))
        finish()
    }

    fun signOut(view: View) {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}