package com.alarmmanager.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.alarmmanager.R

class MyJobService : Service() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        startTimer()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startForegroundService() {
        val channelId = "ForegroundServiceChannel"
        val notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Foreground Service",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Running Alarm Service")
            .setContentText("Will open the app in 15 minutes")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            launchApp()
        }, 15 * 60 * 1000) // 15 minutes
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun launchApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent?.action = android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
//        startActivity(intent)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "MyApp::WakeLock"
        )
        wakeLock.acquire(15 * 60 * 1000L) // Wake up screen for 10 minutes

        startActivity(intent)
        wakeLock.release()

        Log.d("MyForegroundService", "Launching app after 15 minutes")
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
/*

class MyJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("com.alarmmanager.Service.MyJobService", "Job started at: ${System.currentTimeMillis()}")

        // Simulating background work
        Thread {
            Thread.sleep(15 * 60 * 1000) // Simulate 15 minutes of work

            // After 15 minutes, launch DetailActivity
            val intent = Intent(applicationContext, SplashActivity::class.java)
            // Make sure the activity is launched even if the app is in the background
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)

            Log.d("MyJobService", "Job finished, launching detail screen!")
            jobFinished(params, false) // Job finished
        }.start()

        return true // Work is running on a separate thread
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("com.alarmmanager.Service.MyJobService", "Job stopped before completion")
        return true // Retry job if it was interrupted
    }
}
*/
