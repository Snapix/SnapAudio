package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.audio.AudioEngine

class AudioForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        // Audio engine initialization happens in MainActivity, but we ensure it stays alive
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "SNAPAUDIO_CHANNEL")
            .setContentTitle("SnapAudio Engine")
            .setContentText("Spatial Audio is actively processing.")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
            
        startForeground(1, notification)
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "SNAPAUDIO_CHANNEL",
                "SnapAudio Engine Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Not bound
    }
}
