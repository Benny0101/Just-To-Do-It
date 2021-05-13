package com.example.justtodoit

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyNewIntentService : IntentService("MyNewIntentService") {
    private final var NOTIFICATION_ID = 3

    override fun onCreate(){
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {
        val channelId = "channel_id"
        val notifyIntent = Intent(this, HomePageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0)
        var name = AddTaskActivity.nameRef
        name = name.substring(0,1).toUpperCase()+name.substring(1,name.length)
        var type = AddTaskActivity.typeRef
        var builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.add_icon_foreground)
            .setContentTitle("Just To Do It")
            .setContentText("$type $name deadline")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationCompat = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        with (managerCompat) {
            notify(101, notificationCompat)
        }
    }
}
