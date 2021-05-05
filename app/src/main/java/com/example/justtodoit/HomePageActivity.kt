package com.example.justtodoit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomePageActivity : AppCompatActivity() {
    var mode="Day"
    lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val spinner = findViewById<Spinner>(R.id.spinner2)
        var items = arrayOf("Day","Week")
        val spinnerAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,items)
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
        var date2 = date.format(Date()).toString()
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
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (data: DataSnapshot in snapshot.children) {
                    if (mode == "Day") {
                        if (data.child("date_due").value.toString().substring(0, 10) == date_due) {
                            list.add(data.key.toString())
                        }
                    }
                    else if (mode== "Week"){
                        val selected = date.parse(date_due)
                        var cal = date.calendar.get(Calendar.WEEK_OF_YEAR)
                        val due = date.parse(data.child("date_due").value.toString().substring(0, 10))
                        var cal2 = date.calendar.get(Calendar.WEEK_OF_YEAR)
                        if (cal==cal2){
                            list.add(data.key.toString())
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
            var button = conView.findViewById<Button>(R.id.viewTaskButton)
            text2?.text = data[position]
            button.setOnClickListener {
               var  auth = FirebaseAuth.getInstance()
                var buffer = StringBuffer()
                var  myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid).child(data[position])
                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var builder = AlertDialog.Builder(context)
                        for (data: DataSnapshot in snapshot.children) {
                            buffer.append("${data.key.toString()}: ${data.value.toString()}\n")
                        }
                        builder.setCancelable(true)
                        builder.setTitle("Task Details")
                        builder.setNegativeButton(R.string.back_string,null)
                        builder.setMessage(buffer.toString())
                        builder.show()
                    }


                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
            return conView

        }

    }

}