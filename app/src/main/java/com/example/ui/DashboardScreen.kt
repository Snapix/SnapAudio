package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen() {
    val bass by AudioEngine.bassStrength.collectAsState()
    val spatial by AudioEngine.virtualizerStrength.collectAsState()
    val waveform by AudioEngine.waveformParams.collectAsState()

    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while(true) {
            time += 0.1f
            AudioEngine.updateSimulatedVisualizer(time)
            delay(16) // roughly 60fps
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Active Source Header
        GlassCard(modifier = Modifier.fillMaxWidth().height(80.dp)) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("ACTIVE SOURCE", color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Global Mix (Session 0)", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                }
                CurrentStatusIndicator()
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Visualizer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(32.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(32.dp))
                .background(GlassSurface)
                .padding(24.dp)
        ) {
            VisualizerCanvas(waveform = waveform)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            GlassStatCard(
                title = "BASS PUNCH",
                value = "${(bass * 100).toInt()}%",
                modifier = Modifier.weight(1f)
            )
            GlassStatCard(
                title = "SPATIAL FIELD",
                value = "${(spatial * 100).toInt()}%",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CurrentStatusIndicator() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(CyanAccent))
        Spacer(modifier = Modifier.width(8.dp))
        Text("PROCESSING", color = TextSecondary, fontSize = 12.sp, letterSpacing = 1.sp)
    }
}

@Composable
fun VisualizerCanvas(waveform: FloatArray) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val path = Path()
        
        val maxPoints = waveform.size
        if (maxPoints > 0) {
            for (i in waveform.indices) {
                val x = (i.toFloat() / (maxPoints - 1)) * width
                val y = height / 2f + (waveform[i] * height / 2f)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            
            // Draw gradient glow
            drawPath(
                path = path,
                brush = Brush.verticalGradient(listOf(CyanAccent.copy(alpha = 0.8f), BlueAccent.copy(alpha = 0.2f))),
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )
            
            // Add blurred glow matching path roughly
            drawPath(
                path = path,
                color = CyanAccent.copy(alpha = 0.3f),
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(GlassSurface)
            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
    ) {
        content()
    }
}

@Composable
fun GlassStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier.height(100.dp)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, color = CyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Light)
        }
    }
}
