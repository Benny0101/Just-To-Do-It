package com.example.justtodoit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_membership.*

class MembershipActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    companion object {
        val themeKey = "currentTheme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        var plusStatus = prefs.getBoolean("Status", false)


        // Add this to every activity if you wish to have the theme apply
        ThemeActivity.sharedPreferences = getSharedPreferences(
                "ThemePref",
                Context.MODE_PRIVATE
        )

        when (ThemeActivity.sharedPreferences.getString(ThemeActivity.themeKey, "light")) {
            "light" -> theme.applyStyle(R.style.OverlayThemeLight, true)
            "pastel" -> theme.applyStyle(R.style.OverlayThemePastel, true)
            "blue" -> theme.applyStyle(R.style.OverlayThemeBlue, true)
            "red" -> theme.applyStyle(R.style.OverlayThemeRed, true)
        }

        setContentView(R.layout.activity_membership)

        if (plusStatus == true) {
            button7.isEnabled = true
            button7.isClickable = true
            button8.isEnabled = true
            button8.isClickable = true
            getPlusButton.isEnabled = false
            getPlusButton.isClickable = false
            plusValid.text = "⭐"
        } else {
            button7.isEnabled = false
            button7.isClickable = false
            button8.isEnabled = false
            button8.isClickable = false
            plusValid.text = ""
        }

    }

    fun getPlus(view: View) {
        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("Status", true).apply()
    }

    fun blueTheme(view: View) {
        ThemeActivity.sharedPreferences.edit().putString(ThemeActivity.themeKey, "blue").apply()
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }

    fun redTheme(view: View) {
        ThemeActivity.sharedPreferences.edit().putString(ThemeActivity.themeKey, "red").apply()
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }


    fun home(view: View) {
        startActivity(Intent(this, HomePageActivity::class.java))
        finish()
    }

    fun add(view: View) {
        startActivity(Intent(this, AddTaskActivity::class.java))
        finish()
    }

    fun settings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }

}