package com.example.justtodoit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView

class FAQSettingsActivity : AppCompatActivity() {
    var ques1Pressed = false
    var ques2Pressed = false
    var ques3Pressed = false
    var ques4Pressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        setContentView(R.layout.activity_f_a_q_settings)
    }

    fun plus(view: View) {
        startActivity(Intent(this, MembershipActivity::class.java))
        finish()
    }
    fun add(view: View) {
        startActivity(Intent(this, AddTaskActivity::class.java))
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

    fun back(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }


    fun expand2(view: View) {
        var answer2 = findViewById<TextView>(R.id.expand2Text)
        var answer3 = findViewById<TextView>(R.id.expand3Text)
        var answer4 = findViewById<TextView>(R.id.expand4Text)
        var button2 = findViewById<ImageButton>(R.id.expandIcon2)
        var button3 = findViewById<ImageButton>(R.id.expandIcon3)
        var button4 = findViewById<ImageButton>(R.id.expandIcon4)

        if (!ques2Pressed){
            answer2.text="For any queries contact us here: errors.logical@gmail.com."
            ques2Pressed=true
            ques1Pressed=false
            ques3Pressed=false
            ques4Pressed=false
            answer3.text=""
            answer4.text=""
            button2.setImageResource(R.drawable.un_expand_icon_foreground)
            button3.setImageResource(R.drawable.expand_icon_foreground)
            button4.setImageResource(R.drawable.expand_icon_foreground)
        }
        else if(ques2Pressed){
            answer2.text=""
            ques2Pressed=false
            button2.setImageResource(R.drawable.expand_icon_foreground)
        }

    }

    fun expand3(view: View) {
        var answer2 = findViewById<TextView>(R.id.expand2Text)
        var answer3 = findViewById<TextView>(R.id.expand3Text)
        var answer4 = findViewById<TextView>(R.id.expand4Text)
        var button2 = findViewById<ImageButton>(R.id.expandIcon2)
        var button3 = findViewById<ImageButton>(R.id.expandIcon3)
        var button4 = findViewById<ImageButton>(R.id.expandIcon4)

        if (!ques3Pressed){
            answer3.text="The sign out button is located at the bottom of the settings main menu."
            ques3Pressed=true
            ques1Pressed=false
            ques2Pressed=false
            ques4Pressed=false
            answer2.text=""
            answer4.text=""
            button3.setImageResource(R.drawable.un_expand_icon_foreground)
            button2.setImageResource(R.drawable.expand_icon_foreground)
            button4.setImageResource(R.drawable.expand_icon_foreground)
        }
        else if(ques3Pressed){
            answer3.text=""
            ques3Pressed=false
            button3.setImageResource(R.drawable.expand_icon_foreground)
        }

    }

    fun expand4(view: View) {
        var answer2 = findViewById<TextView>(R.id.expand2Text)
        var answer3 = findViewById<TextView>(R.id.expand3Text)
        var answer4 = findViewById<TextView>(R.id.expand4Text)
        var button2 = findViewById<ImageButton>(R.id.expandIcon2)
        var button3 = findViewById<ImageButton>(R.id.expandIcon3)
        var button4 = findViewById<ImageButton>(R.id.expandIcon4)

        if (!ques4Pressed){
            answer4.text="In the Add Task/Event screen, you can select the date of your task/event from the scrollable calendar."+
                    " You can choose the due date for any day of the week due and set the view type to weekly and select any date from that week to see it."
            ques4Pressed=true
            ques1Pressed=false
            ques2Pressed=false
            ques3Pressed=false
            answer2.text=""
            answer3.text=""
            button4.setImageResource(R.drawable.un_expand_icon_foreground)
            button2.setImageResource(R.drawable.expand_icon_foreground)
            button3.setImageResource(R.drawable.expand_icon_foreground)
        }
        else if(ques4Pressed){
            answer4.text=""
            ques4Pressed=false
            button4.setImageResource(R.drawable.expand_icon_foreground)
        }

    }




}