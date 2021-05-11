package com.example.justtodoit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Integer.parseInt
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.milliseconds

class AddTaskActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var time: String
    var type = "Task"

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

        setContentView(R.layout.activity_add_task)
        val spinner = findViewById<Spinner>(R.id.spinner)
        var items = arrayOf("0:00","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00",
                "12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00")
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,items)
        spinner.adapter=adapter
        spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                time = spinner.selectedItem.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun home(view: View) {
        startActivity(Intent(this, HomePageActivity::class.java))
        finish()
    }

    fun plus(view: View) {
        startActivity(Intent(this, MembershipActivity::class.java))
        finish()
    }

    fun settings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }

    fun create(view: View) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("userTasks")
        var error = findViewById<TextView>(R.id.taskError)
        var descr = findViewById<TextView>(R.id.taskDescr).text.toString()
        var name = findViewById<EditText>(R.id.editName)
        var date2 = findViewById<DatePicker>(R.id.datePicker)
        var name2 = findViewById<EditText>(R.id.editName).text.toString()
        var dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK)
        try {
            var duplicate =myRef.child(auth.currentUser.uid).child(name2)
            println(duplicate)
            var day = date2.dayOfMonth.toString()
            var month = (date2.month+1).toString()
            if (day.length == 1) day= "0$day"
            if (month.length == 1) month = "0$month"
            if (name2!="") {
                var date_due = "$day/$month/${date2.year} $time"
                var details = TaskDetails(descr, dateFormatter.format(Date()).toString(), date_due, type)
                myRef.child(auth.currentUser.uid).child(name2).setValue(details)
                var timestamp= SimpleDateFormat("dd/mm/yyyy HH").parse(date_due)
                myRef.child(auth.currentUser.uid).child(name2).child("date_due_timestamp").setValue(timestamp.time)
                if (type=="Task"){
                    myRef.child(auth.currentUser.uid).child(name2).child("status").setValue("Active")
                    //Not an error in this case
                    error.text = "Task Added"
                }
                if (type=="Event"){
                    //Not an error in this case
                    error.text = "Event Added"
                }
                name.text.clear()

                if (NotificationActivity.notificationsOption) {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.MINUTE, 60 - NotificationActivity.reminderInterval)
                    calendar.set(Calendar.HOUR_OF_DAY, timestamp.hours - 1)
                    calendar.set(Calendar.DATE, timestamp.date)

                    val notifyIntent = Intent(this, MyReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(this, 3, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                    val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }

            }
            else if (name2 == "") {
                error.text = "Task Needs a Name"
            }
        } catch (e: IllegalArgumentException) {
            error.text = "Invalid Entry"
        }
    }

    class TaskDetails(descr: String, date_set: String, date_due: String, type:String) {
        var descr = descr
        var date_set = date_set
        var date_due=date_due
        var type=type
    }

    fun type(view: View) {
        var check = findViewById<RadioButton>(R.id.radioButton)
        if (type=="Event"){
            check.isChecked = false
            type="Task"
        }
        if (check.isChecked) {
            type = "Event"
        }
    }
}