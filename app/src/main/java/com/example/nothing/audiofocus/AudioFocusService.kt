package com.example.nothing.audiofocus

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_MUSIC
import android.media.AudioAttributes.CONTENT_TYPE_SPEECH
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.*
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nothing.MainActivity


class AudioFocusService : Service() {

    private val audioManager by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private val requestListener: OnAudioFocusChangeListener =
        OnAudioFocusChangeListener { focusChange ->
            val name = when (focusChange) {
                AUDIOFOCUS_GAIN,
                AUDIOFOCUS_GAIN_TRANSIENT,
                AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK,
                AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE -> {
                    // send success request to StarOs
                    "AUDIOFOCUS_GAIN"
                }

                AUDIOFOCUS_LOSS_TRANSIENT -> {
                    Log.i(AudioFocusService::class.java.simpleName, "lost transient")
//                    onStarOsRequestFocus(AUDIOFOCUS_GAIN)
                    "AUDIOFOCUS_LOSS_TRANSIENT"
                }

                AUDIOFOCUS_LOSS -> {
                    // someone want to take focus from StarOs
                    // onStarOsReleaseFocus()
                    "AUDIOFOCUS_LOSS"
                }

                AUDIOFOCUS_NONE -> "AUDIOFOCUS_NONE"
                else -> throw IllegalArgumentException("focusChange is $focusChange")
            }
            Log.i(AudioFocusService::class.java.simpleName, "focus changed to $name")
        }

    private lateinit var request: AudioFocusRequest

    private fun onStarOsRequestFocus(focusGain: Int) {
        request =
            AudioFocusRequest.Builder(focusGain)
                .setAcceptsDelayedFocusGain(true)
//                .setAudioAttributes(
//                    AudioAttributes.Builder().setContentType(CONTENT_TYPE_SPEECH).build()
//                )
                .setOnAudioFocusChangeListener(requestListener)
                .build()
        val result = audioManager.requestAudioFocus(request)
        Log.i(AudioFocusService::class.java.simpleName, "onStarOsRequestFocus result $result")
    }

    private fun onStarOsReleaseFocus() {
        val result = audioManager.abandonAudioFocusRequest(request)
        Log.i(AudioFocusService::class.java.simpleName, "onStarOsReleaseFocus: result $result")
    }

    override fun onCreate() {
        super.onCreate()
        onStarOsRequestFocus(AUDIOFOCUS_GAIN)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(AudioFocusService::class.java.simpleName, "onStartCommand: ")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AudioFocus starOs")
            .setSmallIcon(R.drawable.ic_btn_speak_now)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "channel id"
    }
}
