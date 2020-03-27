package com.example.nothing.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.nothing.R
import kotlinx.android.synthetic.main.activity_notification.*


class NotificationActivity : AppCompatActivity() {

    private val notificationListenerIntent by lazy {
        val i = Intent(this, NotificationListener::class.java)
        i
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        NotificationListener().setListener(object : NotificationListener.MyListener {
            override fun setValue(str: String, sbn: StatusBarNotification) {
                textView.append("\n $str")
                val notif = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                notif.cancel(sbn.id)
            }
        }).apply {
        }

        setUpNotificationPosting()
        requestNotificationsPermission()
    }

    private fun requestNotificationsPermission() {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun setUpNotificationPosting() {
        findViewById<Button>(R.id.btnCreateNotification).setOnClickListener {
            val mNotificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(
                this@NotificationActivity,
                default_notification_channel_id
            )
                .setContentTitle("My Notification")
                .setContentText("Notification Listener Service Example")
                .setTicker("Notification Listener Service Example")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .build()

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(notificationChannel)
            mNotificationManager.notify(System.currentTimeMillis().toInt(), builder)
        }
    }

    override fun onStart() {
        super.onStart()
        //startService(notificationListenerIntent)
    }

    override fun onStop() {
        super.onStop()
        //stopService(notificationListenerIntent)
    }


    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val default_notification_channel_id = "default"
    }
}