package com.example.justtodoit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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
        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        var plusStatus = prefs.getBoolean("Status", false)
        var ad = findViewById<TextView>(R.id.plusAd)
        var notificationSet = findViewById<TextView>(R.id.textView24)
        var plusButton = findViewById<Button>(R.id.getPlusButton)
        if (plusStatus) {
            plusButton.visibility =  View.GONE
            plusValid.text = "⭐Plus Member!"
            ad.text = ""
            notificationSet.text="You can configure your Notifications"
        } else {
            plusValid.text = ""
            ad.text = "Ad"
            notificationSet.text="Get plus to configure your notifications"
        }

    }

    fun getPlus(view: View) {
        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        var plusStatus = prefs.getBoolean("Status", false)
        if (!plusStatus) {
            prefs.edit().putBoolean("Status", true).apply()
            val intent = intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
        }
    }

    fun blueTheme(view: View) {
        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        var plusStatus = prefs.getBoolean("Status", false)
        var error = findViewById<TextView>(R.id.textView23)
        var sharedPref = getSharedPreferences("Streak", Context.MODE_PRIVATE)
        var counter = sharedPref.getInt("streak", 0)
        if (plusStatus || counter>15) {
            ThemeActivity.sharedPreferences.edit().putString(ThemeActivity.themeKey, "blue").apply()
            val intent = intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
            error.text=""
        }
        else{
            error.text="Requires Plus Membership or a streak of 15"
        }
    }

    fun redTheme(view: View) {
        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        var plusStatus = prefs.getBoolean("Status", false)
        var error = findViewById<TextView>(R.id.textView23)
        if (plusStatus) {
            ThemeActivity.sharedPreferences.edit().putString(ThemeActivity.themeKey, "red").apply()
            val intent = intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
            error.text=""
        }
        else{
            error.text="Requires Plus Membership"
        }
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

    fun back(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }

}