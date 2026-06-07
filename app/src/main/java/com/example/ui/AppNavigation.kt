package com.example.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.*

@Composable
fun SnapAudioNavigation() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = DarkSurfaceVariant.copy(alpha = 0.95f)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == "dashboard",
                    onClick = { navController.navigate("dashboard") },
                    icon = { Icon(Icons.Default.Speed, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = CyanAccent, unselectedIconColor = TextSecondary, indicatorColor = GlassSurface)
                )
                NavigationBarItem(
                    selected = currentRoute == "eq",
                    onClick = { navController.navigate("eq") },
                    icon = { Icon(Icons.Default.Equalizer, contentDescription = "Equalizer") },
                    label = { Text("EQ & Bass") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = CyanAccent, unselectedIconColor = TextSecondary, indicatorColor = GlassSurface)
                )
                NavigationBarItem(
                    selected = currentRoute == "spatial",
                    onClick = { navController.navigate("spatial") },
                    icon = { Icon(Icons.Default.GraphicEq, contentDescription = "Spatial") },
                    label = { Text("Spatial") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = CyanAccent, unselectedIconColor = TextSecondary, indicatorColor = GlassSurface)
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(padding)
        ) {
            composable("dashboard") { DashboardScreen() }
            composable("eq") { EqScreen() }
            composable("spatial") { SpatialScreen() }
        }
    }
}
