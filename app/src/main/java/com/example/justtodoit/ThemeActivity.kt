package com.example.justtodoit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ThemeActivity : AppCompatActivity() {

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        val themeKey = "currentTheme"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(
                "ThemePref",
                Context.MODE_PRIVATE
        )

        when (sharedPreferences.getString(themeKey, "light")) {
            "light" -> theme.applyStyle(R.style.OverlayThemeLight, true)
            "pastel" -> theme.applyStyle(R.style.OverlayThemePastel, true)
            "blue" -> theme.applyStyle(R.style.OverlayThemeBlue, true)
            "red" -> theme.applyStyle(R.style.OverlayThemeRed, true)
        }

        setContentView(R.layout.activity_theme)

        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        var plusStatus = prefs.getBoolean("Status", false)
        var ad = findViewById<TextView>(R.id.textViewAd)
        if (plusStatus) {
            ad.text = ""
        } else {
            ad.text = "Ad"
        }
    }

    fun lightTheme(view: View) {
        sharedPreferences.edit().putString(themeKey, "light").apply()
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }

    fun pastelTheme(view: View) {
        sharedPreferences.edit().putString(themeKey, "pastel").apply()
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }

    fun confirm(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }

    fun home(view: View) {
        startActivity(Intent(this, HomePageActivity::class.java))
        finish()
    }

    fun settings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }

    fun add(view: View) {
        startActivity(Intent(this, AddTaskActivity::class.java))
        finish()
    }

    fun plus(view: View) {
        startActivity(Intent(this, MembershipActivity::class.java))
        finish()
    }
}