package com.example.justtodoit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_membership.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomePageActivity : AppCompatActivity() {
    companion object{
        var valid=false
        var edited=false
    }
    var mode="Day"
    var shown = "View All Tasks & Events"
    lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Streak System
        var sharedPref = getSharedPreferences("Streak", Context.MODE_PRIVATE)
        val c = Calendar.getInstance()
        val thisDay2 = c.get(Calendar.DAY_OF_YEAR)
        var thisDay = sharedPref.getInt("thisDay", thisDay2)
        val lastDay = sharedPref.getInt("lastDate", thisDay2)
        if (thisDay+1==thisDay2) {
            var counter = sharedPref.getInt("streak", 0)
            if (lastDay == thisDay + 1) {
                counter++
                sharedPref.edit().putInt("lastDate", thisDay2+1).apply()
                sharedPref.edit().putInt("streak", counter).apply()
                sharedPref.edit().putInt("thisDay", thisDay2).apply()
            } else {
                sharedPref.edit().putInt("lastDate", thisDay2).apply()
                sharedPref.edit().putInt("streak", 1).apply()
                sharedPref.edit().putInt("thisDay", thisDay2+1).apply()
            }
        }

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

        setContentView(R.layout.activity_home)
        val prefs = getSharedPreferences("Plus", Context.MODE_PRIVATE)
        var plusStatus = prefs.getBoolean("Status", false)
        var ad = findViewById<TextView>(R.id.textView6)
        if (plusStatus) {
            ad.text = ""
        } else {
            ad.text = "Ad"
        }

        val streakText = findViewById<TextView>(R.id.streakNumber)
        var sharedPreferences = getSharedPreferences("Streak", Context.MODE_PRIVATE)
        streakText.text = "Streak: " + sharedPreferences.getInt("streak", 0)

        val spinner = findViewById<Spinner>(R.id.spinner2)
        var items = arrayOf("Day", "Week", "Month")
        val spinnerAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items)
        spinner.adapter=spinnerAdapter
        spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mode = spinner.selectedItem.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    fun confirm(view: View) {
        edited=false
        var date = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK)
        var selectedDate = findViewById<DatePicker>(R.id.datePicker2)
        var day = selectedDate.dayOfMonth.toString()
        var month = (selectedDate.month+1).toString()
        if (day.length == 1) day= "0$day"
        if (month.length == 1) month = "0$month"
        var date_due = "$day/$month/${selectedDate.year}"
        var list = ArrayList<String>()
        var adapter = CustAdapter(this, list)
        var listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter
        auth = FirebaseAuth.getInstance()
        var myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid)
        myRef.orderByChild("date_due_timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val selected = date.parse(date_due)
                for (data: DataSnapshot in snapshot.children) {
                    if (shown == "View All Tasks & Events") {
                        if (mode == "Day") {
                            if (data.child("date_due").value.toString().substring(0, 10) == date_due) {
                                valid=true
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Week") {
                            var selectedTime = Calendar.getInstance()
                            var itemTime = Calendar.getInstance()
                            selectedTime.time = selected
                            var selectedDate = selectedTime.get((Calendar.WEEK_OF_YEAR)+1)
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            itemTime.time = due
                            var itemDate = itemTime.get((Calendar.WEEK_OF_YEAR)+1)
                            if (selectedDate==itemDate) {
                                valid=true
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Month") {
                            var selectedTime = Calendar.getInstance()
                            var itemTime = Calendar.getInstance()
                            selectedTime.time = selected
                            var selectedDate = selectedTime.get((Calendar.MONTH))
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            itemTime.time = due
                            var itemDate = itemTime.get((Calendar.MONTH))
                            if (selectedDate==itemDate) {
                                valid=true
                                list.add(data.key.toString())
                            }
                        }
                    }  else if (shown == "View Events") {
                        if (mode == "Day") {
                            if (data.child("date_due").value.toString().substring(0, 10) == date_due && data.child("type").value.toString() == "Event") {
                                valid=true
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Week" && data.child("type").value.toString() == "Event") {
                            var selectedTime = Calendar.getInstance()
                            var itemTime = Calendar.getInstance()
                            selectedTime.time = selected
                            var selectedDate = selectedTime.get((Calendar.WEEK_OF_YEAR)+1)
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            itemTime.time = due
                            var itemDate = itemTime.get((Calendar.WEEK_OF_YEAR)+1)
                            if (selectedDate==itemDate) {
                                valid=true
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Month" && data.child("type").value.toString() == "Event") {
                            var selectedTime = Calendar.getInstance()
                            var itemTime = Calendar.getInstance()
                            selectedTime.time = selected
                            var selectedDate = selectedTime.get((Calendar.MONTH))
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            itemTime.time = due
                            var itemDate = itemTime.get((Calendar.MONTH))
                            if (selectedDate==itemDate) {
                                valid=true
                                list.add(data.key.toString())
                            }
                        }
                    }
                    else if (shown == "View Active Tasks") {
                        if (mode == "Day") {
                            if (data.child("date_due").value.toString().substring(0, 10) == date_due && data.child("type").value.toString() == "Task") {
                                if (data.child("status").value.toString()=="Active") {
                                    valid = true
                                    list.add(data.key.toString())
                                }
                            }
                        } else if (mode == "Week" && data.child("type").value.toString() == "Task") {
                            if (data.child("status").value.toString()=="Active") {
                                var selectedTime = Calendar.getInstance()
                                var itemTime = Calendar.getInstance()
                                selectedTime.time = selected
                                var selectedDate = selectedTime.get((Calendar.WEEK_OF_YEAR) + 1)
                                val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                                itemTime.time = due
                                var itemDate = itemTime.get((Calendar.WEEK_OF_YEAR) + 1)
                                if (selectedDate == itemDate) {
                                    list.add(data.key.toString())
                                    valid=true
                                }
                            }
                        } else if (mode == "Month" && data.child("type").value.toString() == "Task") {
                            if (data.child("status").value.toString() == "Active") {
                                var selectedTime = Calendar.getInstance()
                                var itemTime = Calendar.getInstance()
                                selectedTime.time = selected
                                var selectedDate = selectedTime.get((Calendar.MONTH))
                                val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                                itemTime.time = due
                                var itemDate = itemTime.get((Calendar.MONTH))
                                if (selectedDate == itemDate) {
                                    valid=true
                                    list.add(data.key.toString())
                                }
                            }
                        }
                    }
                    else if (shown == "View Done Tasks") {
                        if (mode == "Day") {
                            if (data.child("date_due").value.toString().substring(0, 10) == date_due && data.child("type").value.toString() == "Task") {
                                if (data.child("status").value.toString()=="Done") {
                                    valid = true
                                    list.add(data.key.toString())
                                }
                            }
                        } else if (mode == "Week" && data.child("type").value.toString() == "Task") {
                            if (data.child("status").value.toString()=="Done") {
                                var selectedTime = Calendar.getInstance()
                                var itemTime = Calendar.getInstance()
                                selectedTime.time = selected
                                var selectedDate = selectedTime.get((Calendar.WEEK_OF_YEAR) + 1)
                                val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                                itemTime.time = due
                                var itemDate = itemTime.get((Calendar.WEEK_OF_YEAR) + 1)
                                if (selectedDate == itemDate) {
                                    list.add(data.key.toString())
                                    valid=true
                                }
                            }
                        } else if (mode == "Month" && data.child("type").value.toString() == "Task") {
                            if (data.child("status").value.toString() == "Done") {
                                var selectedTime = Calendar.getInstance()
                                var itemTime = Calendar.getInstance()
                                selectedTime.time = selected
                                var selectedDate = selectedTime.get((Calendar.MONTH))
                                val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                                itemTime.time = due
                                var itemDate = itemTime.get((Calendar.MONTH))
                                if (selectedDate == itemDate) {
                                    list.add(data.key.toString())
                                    valid=true
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged()
                invalid()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun invalid() {
        try{
        if (!valid && !edited) {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("No Tasks/Events Scheduled")
            builder.setNegativeButton(R.string.done_string, null)
            builder.show()
        }
            else{
                valid=false
        }
        }
        catch(e: Exception){
        }
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

    fun filter(view: View) {
        var builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Filter View:")
        var input = EditText(this)
        var spinner = Spinner(this)
        var items = arrayOf("View All Tasks & Events",  "View Events", "View Active Tasks", "View Done Tasks")
        val spinnerAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items)
        spinner.adapter=spinnerAdapter
        spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                shown = spinner.selectedItem.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        var layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = layoutParams
        spinner.setSelection(items.indexOf(shown))
        builder.setView(spinner)
        builder.setNegativeButton(R.string.done_string, null)
        builder.show()
    }


    class CustAdapter : BaseAdapter{
        constructor(context: Context, data: ArrayList<String>){
            this.context=context
            this.data=data
        }
        var context: Context
        var data = ArrayList<String>()
        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater: LayoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var conView = inflater.inflate(R.layout.activity_task, parent, false)
            var text2 = conView?.findViewById<TextView>(R.id.taskView2)
            var text3 = conView?.findViewById<TextView>(R.id.taskView3)
            var viewButton = conView.findViewById<Button>(R.id.viewTaskButton)
            var edit = conView.findViewById<ImageButton>(R.id.editTaskButton)
            var delete = conView.findViewById<ImageButton>(R.id.deleteTaskButton)
            var auth = FirebaseAuth.getInstance()
            var name = data[position].substring(0,1).toUpperCase()+data[position].substring(1,data[position].length)
            text2?.text = name
            var  myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid).child(data[position])
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data: DataSnapshot in snapshot.children) {
                        if (data.key.toString()=="date_due") {
                            text3?.text = "Due: ${data.value.toString()}"
                        }
                        if (data.value.toString()=="Event"){
                            viewButton.text="View Event"
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

            viewButton.setOnClickListener {
                var buffer = StringBuffer()
                var done=false
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var checkbox = CheckBox(context)
                        var builder = AlertDialog.Builder(context)
                        for (data: DataSnapshot in snapshot.children) {
                            var key = data.key.toString()
                            if (key=="date_due" || key=="date_set"){
                                key=key.replace("_"," ")
                            }
                            if (key=="descr"){
                                key += "iption"
                            }
                            var formatKey = key.substring(0,1).toUpperCase()+key.substring(1,key.length)
                            if (data.key.toString()!="date_due_timestamp") {
                                buffer.append("${formatKey}: ${data.value.toString()}\n\n")
                            }
                            if (data.value.toString()=="Done") done=true
                            else if (data.value.toString()=="Active") done = false
                            if (data.value.toString() == "Task") {
                                var layoutParams = ConstraintLayout.LayoutParams(
                                        ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
                                checkbox.layoutParams = layoutParams
                                checkbox.text = "Task Complete?"
                                if (done){
                                    checkbox.text = "Task Incomplete?"
                                }
                                builder.setView(checkbox)
                            }
                        }
                        checkbox.setOnClickListener {
                            if (checkbox.isChecked){
                                if (checkbox.text=="Task Complete?") {
                                    myRef.child("status").setValue("Done")
                                }
                                else if (checkbox.text=="Task Incomplete?"){
                                    myRef.child("status").setValue("Active")
                                }
                            }
                            if (!checkbox.isChecked){
                                if (checkbox.text=="Task Complete?") {
                                    myRef.child("status").setValue("Active")
                                }
                                else if (checkbox.text=="Task Incomplete?"){
                                    myRef.child("status").setValue("Done")
                                }
                            }
                        }
                        builder.setCancelable(true)
                        var name = data[position].substring(0,1).toUpperCase()+data[position].substring(1,data[position].length)
                        builder.setTitle("$name Details:\n")
                        builder.setNegativeButton(R.string.done_string, null)
                        builder.setMessage(buffer.toString())
                        builder.show()
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

            edit.setOnClickListener {
                var editBuilder = AlertDialog.Builder(context)
                var conView2 = inflater.inflate(R.layout.activity_edit_task, parent, false)
                editBuilder.setView(conView2)
                editBuilder.setCancelable(true)
                var dataName = data[position].substring(0,1).toUpperCase()+data[position].substring(1,data[position].length)
                editBuilder.setTitle("$dataName Edit:")
                editBuilder.setNegativeButton(R.string.done_string, null)
                editBuilder.show()
                var time ="0:00"
                var date = conView2.findViewById<DatePicker>(R.id.datePickerEdit)
                var descr = conView2.findViewById<EditText>(R.id.editDescr)
                var name = conView2.findViewById<EditText>(R.id.editName)
                var dateButton = conView2.findViewById<Button>(R.id.dateButton)
                var descrButton = conView2.findViewById<Button>(R.id.descrButton)
                var nameButton = conView2.findViewById<Button>(R.id.nameButton)
                val spinner = conView2.findViewById<Spinner>(R.id.spinnerEdit)
                var items = arrayOf("0:00","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00",
                        "12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00")
                val adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item,items)
                spinner.adapter=adapter
                spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        time = spinner.selectedItem.toString()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
                var temp = data[position]
                var builder2 = AlertDialog.Builder(context)

                dateButton.setOnClickListener {
                    edited=true
                    var myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid).child(temp)
                    var day = date.dayOfMonth.toString()
                    var month = (date.month + 1).toString()
                    if (day.length == 1) day = "0$day"
                    if (month.length == 1) month = "0$month"
                    var date_due = "$day/$month/${date.year} $time"
                    myRef.child("date_due").setValue(date_due)
                    var timestamp= SimpleDateFormat("dd/mm/yyyy HH").parse(date_due)
                    myRef.child("date_due_timestamp").setValue(timestamp.time)
                    builder2.setCancelable(true)
                    builder2.setTitle("$temp Date Edited")
                    builder2.setNegativeButton(R.string.done_string, null)
                    builder2.show()
                }

                descrButton.setOnClickListener {
                    edited=true
                    var myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid).child(temp)
                    myRef.child("descr").setValue(descr.text.toString())
                    builder2.setCancelable(true)
                    builder2.setTitle("$temp Description Edited")
                    builder2.setNegativeButton(R.string.done_string, null)
                    builder2.show()
                }

                nameButton.setOnClickListener {
                    var myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid).child(temp)
                    var items = ArrayList<String>()
                    var type=false
                    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (data: DataSnapshot in snapshot.children) {
                                if (data.key.toString()!="date_due_timestamp") {
                                    items.add(data.value.toString())
                                }
                                if (data.value.toString()=="Event"){
                                    type=true
                                }
                            }
                            if (name.text.toString()!="" && temp!=name.text.toString()){
                                edited=true
                                var myRef2 = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid)
                                myRef2.child(temp).removeValue()
                                if (!type) {
                                    var details = TaskDetails(items[0], items[1], items[2], items[3], items[4])
                                    myRef2.child(name.text.toString()).setValue(details)
                                } else if (type) {
                                    var details = EventDetails(items[0], items[1], items[2], items[3])
                                    myRef2.child(name.text.toString()).setValue(details)
                                }
                                var timestamp= SimpleDateFormat("dd/mm/yyyy HH").parse(items[0])
                                myRef2.child(name.text.toString()).child("date_due_timestamp").setValue(timestamp.time)
                                temp = name.text.toString()
                                builder2.setCancelable(true)
                                builder2.setTitle("New Name: $temp ")
                                builder2.setNegativeButton(R.string.done_string, null)
                                builder2.show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
                }

            }

            delete.setOnClickListener {
                var builder = AlertDialog.Builder(context)
                var myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid)
                edited=true
                myRef.child(data[position]).removeValue()
                builder.setCancelable(true)
                var dataName = data[position].substring(0,1).toUpperCase()+data[position].substring(1,data[position].length)
                builder.setTitle("$dataName Deleted")
                builder.setNegativeButton(R.string.done_string, null)
                builder.show()
            }
            return conView
        }
    }
}

class TaskDetails(s: String, s1: String, s2: String, s3: String, s4: String) {
    var date_due = s
    var date_set=s1
    var descr = s2
    var status=s3
    var type=s4
}

class EventDetails(s: String, s1: String, s2: String, s3: String) {
    var date_due = s
    var date_set=s1
    var descr = s2
    var type=s3
}

