package com.alarmmanager.other

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.alarmmanager.SplashActivity

class MyForeService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Start the service in the foreground
        startForegroundService()

        // Simulate a delay of 15 minutes
        Thread {
            Thread.sleep(15 * 60 * 1000) // Simulate 15 minutes of delay

            // After the delay, launch SplashActivity
            val splashIntent = Intent(this, SplashActivity::class.java)
            splashIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(splashIntent)

            // Stop the service after the task is completed
            stopSelf()
        }.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "foreground_service_channel"

        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Foreground Service Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification to run the service in the foreground
        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Background Task Running")
            .setContentText("The app is running background tasks.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        // Start the service in the foreground
        startForeground(1, notification)
    }
}
