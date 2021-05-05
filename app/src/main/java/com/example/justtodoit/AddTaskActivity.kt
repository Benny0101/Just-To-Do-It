package com.example.justtodoit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Integer.parseInt
import java.text.DateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var time: String
    var type = "Task"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        val spinner = findViewById<Spinner>(R.id.spinner)
        var items = arrayOf("0:00","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00",
                "12:00","1300","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00")
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
            var day = date2.dayOfMonth.toString()
            var month = (date2.month+1).toString()
            if (day.length == 1) day= "0$day"
            if (month.length == 1) month = "0$month"
            if (name2!="") {
                var date_due2 = "$day/$month/${date2.year} $time"
                var details = TaskDetails(descr, dateFormatter.format(Date()).toString(), date_due2, type)
                myRef.child(auth.currentUser.uid).child(name2).setValue(details)
                error.text = "Task Added"
                name.text.clear()
            }
            else if (name2 == "") {
                error.text = "Task Needs a Name"
            } else {
                error.text = "Invalid date"
            }
        } catch (e: IllegalArgumentException) {
            error.text = "Invalid date"
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