package com.example.nothing.notification

import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class NotificationListener : NotificationListenerService() {
    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(NotificationListener::class.java.simpleName, "onCreate: yes!")
        context = applicationContext
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i(NotificationListener::class.java.simpleName, "onListenerConnected: yes!")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.i(NotificationListener::class.java.simpleName, "onListenerDisconnected: ")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.i(TAG, "********** onNotificationPosted")
        Log.i(NotificationListener::class.java.simpleName, "onNotificationPosted: $sbn")
        myListener?.setValue("Post: " + sbn.packageName, sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i(TAG, "********** onNotificationRemoved")
        Log.i(
            TAG,
            "ID :" + sbn.id + " \t " + sbn.notification.tickerText + " \t " + sbn.packageName
        )
        myListener?.setValue("Remove: " + sbn.packageName, sbn)
    }

    fun setListener(myListener: MyListener?) {
        NotificationListener.myListener = myListener
    }

    companion object {
        private val TAG = this.javaClass.simpleName
        var myListener: MyListener? = null
    }

    interface MyListener {
        fun setValue(str: String, sbn: StatusBarNotification)
    }
}