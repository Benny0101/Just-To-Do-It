package com.example.justtodoit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun confirm2(view: View) {
        database = FirebaseDatabase.getInstance()
        myRef = database.reference.child("test")
        println(myRef)
        println(database)

        myRef.setValue("Hello, World!")
    }
}