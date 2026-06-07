package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.ui.theme.*

@Composable
fun EqScreen() {
    val bass by AudioEngine.bassStrength.collectAsState()
    val bands by AudioEngine.eqBands.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("EQUALIZER", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Light, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(32.dp))

        // Bass Engine
        GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("BASS ENGINE", color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Slider(
                    value = bass,
                    onValueChange = { AudioEngine.setBass(it) },
                    valueRange = 0f..3f,
                    colors = SliderDefaults.colors(
                        thumbColor = CyanAccent,
                        activeTrackColor = BlueAccent,
                        inactiveTrackColor = GlassBorder
                    )
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Flat", color = TextSecondary, fontSize = 12.sp)
                    Text("+300%", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Multi-band EQ
        Text("10-BAND FREQUENCY CONTROL", color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, modifier = Modifier.align(Alignment.Start).padding(start = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(bands) { index, band ->
                EqBandSlider(
                    freq = "${band.first}Hz",
                    level = band.second,
                    onLevelChange = { AudioEngine.setEqBand(index.toShort(), it.toInt().toShort()) }
                )
            }
        }
    }
}

@Composable
fun EqBandSlider(freq: String, level: Int, onLevelChange: (Float) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(freq, color = TextSecondary, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = level.toFloat(),
            onValueChange = onLevelChange,
            valueRange = -1500f..1500f,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = BlueAccent,
                activeTrackColor = CyanAccent.copy(alpha=0.5f),
                inactiveTrackColor = GlassSurface
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("${level / 100}dB", color = TextPrimary, fontSize = 11.sp)
    }
}
