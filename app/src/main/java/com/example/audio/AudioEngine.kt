package com.example.audio

import android.media.audiofx.BassBoost
import android.media.audiofx.EnvironmentalReverb
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import android.media.audiofx.Visualizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.sin

object AudioEngine {
    private const val TAG = "AudioEngine"
    private var sessionId: Int = 0 // Global session

    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null
    private var reverb: EnvironmentalReverb? = null
    private var visualizer: Visualizer? = null

    // State flows for UI binding
    private val _bassStrength = MutableStateFlow(0f)
    val bassStrength = _bassStrength.asStateFlow()

    private val _virtualizerStrength = MutableStateFlow(0f)
    val virtualizerStrength = _virtualizerStrength.asStateFlow()

    private val _waveformParams = MutableStateFlow(FloatArray(128))
    val waveformParams = _waveformParams.asStateFlow()
    
    private val _eqBands = MutableStateFlow<List<Pair<Int, Int>>>(emptyList()) // CenterFreq to Level
    val eqBands = _eqBands.asStateFlow()

    fun initialize() {
        try {
            equalizer = Equalizer(0, sessionId).apply { enabled = true }
            bassBoost = BassBoost(0, sessionId).apply { enabled = true }
            virtualizer = Virtualizer(0, sessionId).apply { enabled = true }
            reverb = EnvironmentalReverb(0, sessionId).apply { enabled = true }
            
            // Mocking initial EQ values based on equalizer capabilities
            val bands = mutableListOf<Pair<Int, Int>>()
            equalizer?.let { eq ->
                val numBands = eq.numberOfBands
                for (i in 0 until numBands) {
                    bands.add(Pair(eq.getCenterFreq(i.toShort()) / 1000, eq.getBandLevel(i.toShort()).toInt()))
                }
            }
            if (bands.isEmpty()) { // Fallback if effect not supported
                bands.addAll(listOf(60 to 0, 230 to 0, 910 to 0, 3600 to 0, 14000 to 0))
            }
            _eqBands.value = bands
            
            // Note: Visualizer needs RECORD_AUDIO permission. It might fail without it.
            // visualizer = Visualizer(sessionId).apply { ... }
        } catch (e: Exception) {
            Log.e(TAG, "AudioFX initialization failed. Check permissions.", e)
        }
    }

    fun setBass(strength: Float) { // 0.0 to 3.0 (0% to 300%)
        _bassStrength.value = strength
        try {
            bassBoost?.setStrength((strength * 333).toInt().toShort()) // Max strength is 1000
        } catch (e: Exception) {}
    }

    fun setSpatialIntensity(intensity: Float) { // 0.0 to 1.0
        _virtualizerStrength.value = intensity
        try {
            virtualizer?.setStrength((intensity * 1000).toInt().toShort())
        } catch (e: Exception) {}
    }
    
    fun setEqBand(bandIndex: Short, level: Short) {
        try {
            equalizer?.setBandLevel(bandIndex, level)
            val updated = _eqBands.value.toMutableList()
            if (bandIndex < updated.size) {
                updated[bandIndex.toInt()] = updated[bandIndex.toInt()].copy(second = level.toInt())
                _eqBands.value = updated
            }
        } catch (e: Exception) {}
    }

    // Mathematical mock for visualizer if real Visualizer fails due to policy
    fun updateSimulatedVisualizer(timeOut: Float) {
        val newWave = FloatArray(128)
        val basAmp = bassStrength.value * 2f + 1f
        for (i in newWave.indices) {
            // Complex mock wave indicating active audio processing based on bass + spatial
            val mod = sin(timeOut + i * 0.1f) * sin(timeOut * 0.5f + i * 0.03f)
            newWave[i] = mod * basAmp * 0.5f
        }
        _waveformParams.value = newWave
    }

    fun release() {
        equalizer?.release()
        bassBoost?.release()
        virtualizer?.release()
        reverb?.release()
        visualizer?.release()
    }
}
