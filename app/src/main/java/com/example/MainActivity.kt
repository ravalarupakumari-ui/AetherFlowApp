package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.FloatingGlassNavBar
import com.example.ui.components.NavDestination
import com.example.ui.screens.*
import com.example.ui.theme.AetherFlowTheme
import com.example.viewmodel.AetherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AetherViewModel = viewModel()
            val isDark by viewModel.isDarkTheme.collectAsState()
            val isLoggedIn by viewModel.isLoggedIn.collectAsState()
            val currentRoute by viewModel.currentRoute.collectAsState()

            AetherFlowTheme(darkTheme = isDark) {
                // Background Gradient overlay
                val backgroundBrush = if (isDark) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF090D16),
                            Color(0xFF1E1B4B)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFC),
                            Color(0xFFE2E8F0),
                            Color(0xFFF1F5F9)
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundBrush)
                ) {
                    if (!isLoggedIn) {
                        AuthScreen(
                            onLoginSuccess = { email -> viewModel.login(email) }
                        )
                    } else {
                        Scaffold(
                            containerColor = Color.Transparent,
                            bottomBar = {
                                FloatingGlassNavBar(
                                    currentRoute = currentRoute,
                                    onNavigate = { dest -> viewModel.setRoute(dest.route) }
                                )
                            }
                        ) { innerPadding ->
                            Crossfade(
                                targetState = currentRoute,
                                animationSpec = tween(durationMillis = 300),
                                label = "screenCrossfade",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) { route ->
                                when (route) {
                                    "dashboard" -> DashboardScreen(viewModel = viewModel)
                                    "automation" -> AutomationScreen(viewModel = viewModel)
                                    "integrations" -> IntegrationsScreen(viewModel = viewModel)
                                    "apps" -> AppsHubScreen(viewModel = viewModel)
                                    "revenue" -> RevenueScreen(viewModel = viewModel)
                                    "profile" -> ProfileScreen(viewModel = viewModel)
                                    else -> DashboardScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
