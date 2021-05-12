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

        val notSwitch = findViewById<Switch>(R.id.notSwitch)
        notSwitch.isChecked = notificationsOption

        notSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener() { compoundButton: CompoundButton, b: Boolean ->
            notificationsOption = b
        })

        val spinner = findViewById<Spinner>(R.id.spinReminder)
        val items = arrayOf(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items)

        spinner.adapter = adapter
        spinner.setSelection(reminderIndex)

        reminderInterval = spinner.selectedItem as Int

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                reminderInterval = spinner.selectedItem as Int
                reminderIndex = spinner.selectedItemPosition
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
