package com.example.justtodoit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomePageActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }













    fun settings(view: View) {
        startActivity(Intent(this,SettingsActivity::class.java))
        finish()
    }

}