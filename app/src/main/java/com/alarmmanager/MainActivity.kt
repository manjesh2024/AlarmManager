package com.alarmmanager


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStartService)
        val btnStop = findViewById<Button>(R.id.btnStopService)



        btnStart.setOnClickListener {
            MyForegroundService.startService(this)
        }

        btnStop.setOnClickListener {
            MyForegroundService.stopService(this)
        }
    }
}