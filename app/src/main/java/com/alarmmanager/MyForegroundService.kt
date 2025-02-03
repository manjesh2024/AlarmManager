package com.alarmmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyForegroundService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaPlayer = MediaPlayer.create(this, R.raw.beep) // Load beep sound
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification("Service Running..."))

        // Start a background task
        serviceScope.launch {
            while (true) {
                // Simulating background work (e.g., downloading, syncing, etc.)
                mediaPlayer?.start() // Play beep

                println("Background task running...")
                delay(20000) // Simulate work every 20 Sec now
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel() // Cancel coroutines to avoid memory leaks
        mediaPlayer?.release() // Release MediaPlayer
        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "ForegroundServiceChannel"
        private const val NOTIFICATION_ID = 1

        fun startService(context: Context) {
            val intent = Intent(context, MyForegroundService::class.java)
            context.startService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, MyForegroundService::class.java)
            context.stopService(intent)
        }
    }
}