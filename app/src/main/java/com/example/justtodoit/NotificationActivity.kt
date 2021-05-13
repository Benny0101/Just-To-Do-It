package com.example.justtodoit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class NotificationActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

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

        setContentView(R.layout.activity_notification)

        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        var plusStatus = prefs.getBoolean("Status", false)
        var ad = findViewById<TextView>(R.id.settingsAd)
        if (plusStatus) {
            ad.text = ""
        } else {
            ad.text = "Ad"
        }

        val notSwitch = findViewById<Switch>(R.id.notSwitch)
        notSwitch.isChecked = notificationsOption

        notSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener() { compoundButton: CompoundButton, b: Boolean ->
            notificationsOption = b
        })

        val spinner = findViewById<Spinner>(R.id.spinReminder)
        val items = arrayOf("1 Hour", "2 Hours", "3 Hours", "4 Hours", "5 Hours", "6 Hours", "7 Hours", "8 Hours", "9 Hours", "10 Hours", "11 Hours", "12 Hours", "1 Day", "2 Days", "1 Week", "2 Weeks", "3 Weeks", "1 Month")
        val values = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 24, 48, 168, 336, 504, 768)
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items)

        spinner.adapter = adapter
        spinner.setSelection(reminderIndex)

        reminderInterval = values[reminderIndex]

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                reminderIndex = spinner.selectedItemPosition
                reminderInterval = values[reminderIndex]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //throw Exception("UHHSHDUAHSDHUAUSDH::: " + spinnerVal)
    }

    fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        notificationsOption = isChecked
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

    fun back(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }

    companion object {
        var notificationsOption : Boolean = true
        var reminderIndex = 0
        var reminderInterval : Int = 0
    }

}
