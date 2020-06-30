package com.example.nothing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nothing.audiofocus.AudioFocusService
import org.zeromq.ZContext


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ZContext()
    }

    override fun onStart() {
        super.onStart()
        startForegroundService(Intent(this, AudioFocusService::class.java))
    }

    override fun onStop() {
        super.onStop()
//        stopService(Intent(this, AudioFocusService::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopService(Intent(this, AudioFocusService::class.java))
    }
}
