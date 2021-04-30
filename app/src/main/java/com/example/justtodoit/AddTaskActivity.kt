package com.example.justtodoit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
    }

    fun home(view: View) {
        startActivity(Intent(this,HomePageActivity::class.java))
        finish()
    }
    fun settings(view: View) {
        startActivity(Intent(this,SettingsActivity::class.java))
        finish()
    }
}