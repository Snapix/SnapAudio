package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.ui.theme.*

@Composable
fun SpatialScreen() {
    val spatial by AudioEngine.virtualizerStrength.collectAsState()
    
    var selectedMode by remember { mutableStateOf("16D") }
    var selectedPath by remember { mutableStateOf("Orbit") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("SPATIAL AUDIO ENGINE", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Light, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(32.dp))

        // Mode Selector
        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(GlassSurface).padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("8D", "9D", "16D", "32D").forEach { mode ->
                val isSelected = selectedMode == mode
                TextButton(
                    onClick = { selectedMode = mode },
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(if (isSelected) CyanAccent.copy(alpha=0.2f) else androidx.compose.ui.graphics.Color.Transparent)
                ) {
                    Text(mode, color = if (isSelected) CyanAccent else TextSecondary, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Field Depth Control
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("FIELD EXPANSION", color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Slider(
                    value = spatial,
                    onValueChange = { AudioEngine.setSpatialIntensity(it) },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = BlueAccent)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Path Selector
        Text("ORBITAL PATH", color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, modifier = Modifier.align(Alignment.Start).padding(start = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val paths = listOf(
                Pair("Spherical Orbit", "Standard 360 rotation"),
                Pair("Figure Eight", "Complex panning curve"),
                Pair("Bounce", "Elastic room reflection")
            )
            paths.forEach { path ->
                val isSelected = selectedPath == path.first
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if(isSelected) GlassBorder else GlassSurface,
                    onClick = { selectedPath = path.first }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(path.first, color = if (isSelected) CyanAccent else TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Text(path.second, color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
