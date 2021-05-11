package com.example.justtodoit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {
    fun myReceiver() {

    }

    override fun onReceive(context: Context, intent: Intent) {
        var intent1 = Intent(context, MyNewIntentService::class.java)
        context.startService(intent1)
    }
}