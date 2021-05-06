package com.example.justtodoit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomePageActivity : AppCompatActivity() {
    var mode="Day"
    var shown = "View Tasks & Events"
    var status="Active"
    lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
        myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid)
        myRef.orderByChild("date_due").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val selected = date.parse(date_due)
                for (data: DataSnapshot in snapshot.children) {
                    if (shown == "View Tasks & Events") {
                        if (mode == "Day") {
                            if (data.child("date_due").value.toString().substring(0, 10) == date_due) {
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Week") {
                            var added = Calendar.getInstance()
                            var current = Calendar.getInstance()
                            current.time = selected
                            added.time = selected
                            added.add(Calendar.DAY_OF_YEAR, +7)
                            current.add(Calendar.DAY_OF_YEAR, 0)
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            if (due <= added.time && due >= current.time) {
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Month") {
                            var added = Calendar.getInstance()
                            var current = Calendar.getInstance()
                            current.time = selected
                            added.time = selected
                            added.add(Calendar.DAY_OF_YEAR, +31)
                            current.add(Calendar.DAY_OF_YEAR, 0)
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            if (due <= added.time && due >= current.time) {
                                list.add(data.key.toString())
                            }
                        }
                    } else if (shown == "View Tasks") {
                        if (mode == "Day") {
                            if (data.child("date_due").value.toString().substring(0, 10) == date_due && data.child("type").value.toString() == "Task") {
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Week" && data.child("type").value.toString() == "Task") {
                            var added = Calendar.getInstance()
                            var current = Calendar.getInstance()
                            current.time = selected
                            added.time = selected
                            added.add(Calendar.DAY_OF_YEAR, +7)
                            current.add(Calendar.DAY_OF_YEAR, 0)
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            if (due <= added.time && due >= current.time) {
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Month" && data.child("type").value.toString() == "Task") {
                            var added = Calendar.getInstance()
                            var current = Calendar.getInstance()
                            current.time = selected
                            added.time = selected
                            added.add(Calendar.DAY_OF_YEAR, +31)
                            current.add(Calendar.DAY_OF_YEAR, 0)
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            if (due <= added.time && due >= current.time) {
                                list.add(data.key.toString())
                            }
                        }
                    } else if (shown == "View Events") {
                        if (mode == "Day") {
                            if (data.child("date_due").value.toString().substring(0, 10) == date_due && data.child("type").value.toString() == "Event") {
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Week" && data.child("type").value.toString() == "Event") {
                            var added = Calendar.getInstance()
                            var current = Calendar.getInstance()
                            current.time = selected
                            added.time = selected
                            added.add(Calendar.DAY_OF_YEAR, +7)
                            current.add(Calendar.DAY_OF_YEAR, 0)
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            if (due <= added.time && due >= current.time) {
                                list.add(data.key.toString())
                            }
                        } else if (mode == "Month" && data.child("type").value.toString() == "Event") {
                            var added = Calendar.getInstance()
                            var current = Calendar.getInstance()
                            current.time = selected
                            added.time = selected
                            added.add(Calendar.DAY_OF_YEAR, +31)
                            current.add(Calendar.DAY_OF_YEAR, 0)
                            val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                            if (due <= added.time && due >= current.time) {
                                list.add(data.key.toString())
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
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
        var items = arrayOf("View Tasks & Events", "View Tasks", "View Events")
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
            var viewButton = conView.findViewById<Button>(R.id.viewTaskButton)
            var edit = conView.findViewById<ImageButton>(R.id.editTaskButton)
            var delete = conView.findViewById<ImageButton>(R.id.deleteTaskButton)
            var  auth = FirebaseAuth.getInstance()
            text2?.text = data[position]
            var  myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid).child(data[position])
            viewButton.setOnClickListener {
                var buffer = StringBuffer()
                var type="Event"
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var checkbox = CheckBox(context)
                        var builder = AlertDialog.Builder(context)
                        for (data: DataSnapshot in snapshot.children) {
                            buffer.append("${data.key.toString()}: ${data.value.toString()}\n")
                            if (data.value.toString() == "Task") {
                                type="Task"
                                var layoutParams = ConstraintLayout.LayoutParams(
                                        ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
                                checkbox.layoutParams = layoutParams
                                checkbox.text = "Task Complete"
                                builder.setView(checkbox)
                            }
                        }
                        builder.setCancelable(true)
                        builder.setTitle("$type ${data[position]} Details:")
                        builder.setNegativeButton(R.string.done_string, null)
                        builder.setMessage(buffer.toString())
                        builder.show()
                    }


                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
            edit.setOnClickListener{
                var editBuilder = AlertDialog.Builder(context)
                var vieww = inflater.inflate(R.layout.activity_edit_task, parent, false)
                editBuilder.setView(vieww)
                editBuilder.setCancelable(true)
                editBuilder.setTitle("${data[position]} Edit:")
                editBuilder.setNegativeButton(R.string.done_string, null)
                editBuilder.setMessage("")
                editBuilder.show()
                var date = vieww.findViewById<DatePicker>(R.id.datePickerEdit)
                var descr = vieww.findViewById<EditText>(R.id.editDescr)
                var dateButton = vieww.findViewById<Button>(R.id.dateButton)
                var descrButton = vieww.findViewById<Button>(R.id.descrButton)
                dateButton.setOnClickListener{
                    var day = date.dayOfMonth.toString()
                    var month = (date.month+1).toString()
                    if (day.length == 1) day= "0$day"
                    if (month.length == 1) month = "0$month"
                    var date_due = "$day/$month/${date.year}"
                    myRef.child("date_due").setValue(date_due)
                }
                descrButton.setOnClickListener {
                    myRef.child("descr").setValue(descr.text.toString())
                }
            }

            delete.setOnClickListener {
                var builder = AlertDialog.Builder(context)
                var myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid)
                myRef.child(data[position]).removeValue()
                builder.setCancelable(true)
                builder.setTitle("${data[position]} Deleted")
                builder.setNegativeButton(R.string.done_string, null)
                builder.show()
            }
            return conView

        }

    }


}

