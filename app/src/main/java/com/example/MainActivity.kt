package com.example

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.audio.AudioEngine
import com.example.service.AudioForegroundService
import com.example.ui.SnapAudioNavigation
import com.example.ui.theme.SnapAudioTheme
import com.example.ui.theme.DarkBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize the AudioFX engine globally
        AudioEngine.initialize()

        val serviceIntent = Intent(this, AudioForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        setContent {
            SnapAudioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    SnapAudioNavigation()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioEngine.release()
    }
}
