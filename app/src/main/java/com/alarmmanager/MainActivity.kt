package com.alarmmanager

import com.alarmmanager.other.MyForeService
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alarmmanager.other.AlarmReceiver

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTime = findViewById<EditText>(R.id.edtTime)
        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)

//        com.alarmmanager.other.JobSchedulerHelper(this).scheduleJob()


        val serviceIntent = Intent(this, MyForeService::class.java)
        startService(serviceIntent)


//        btnSetAlarm.setOnClickListener {
//            val timeInput = etTime.text.toString()
//
//            if (timeInput.isEmpty() || timeInput.toIntOrNull() == null || timeInput.toInt() <= 0) {
//                Toast.makeText(this, "Please enter a valid time in minutes", Toast.LENGTH_SHORT).show()
//            } else {
//                val minutes = timeInput.toInt()
//
//                // 🔹 Check exact alarm permission
//                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    if (!alarmManager.canScheduleExactAlarms()) {
//                        requestExactAlarmPermission()
//                        return@setOnClickListener
//                    }
//                }
//
//                scheduleAlarm(minutes)
//            }
//        }
    }
    private fun scheduleAlarm(minutes: Int) {
        val alarmTime = System.currentTimeMillis() + minutes * 60 * 1000

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        }

        Toast.makeText(this, "Alarm set for $minutes minutes", Toast.LENGTH_SHORT).show()
    }
    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:$packageName"))
            startActivity(intent)
        }
    }
}