package com.selfjudge.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.selfjudge.ui.screens.auth.AuthScreen
import com.selfjudge.ui.screens.main.MainScreen
import com.selfjudge.utils.rememberAuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelfJudgeApp() {
    val navController = rememberNavController()
    val authState = rememberAuthState()

    if (authState.isLoading) {
        // Show loading screen
        return
    }

    NavHost(
        navController = navController,
        startDestination = if (authState.isAuthenticated) "main" else "auth"
    ) {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        
        composable("main") {
            MainScreen(
                onSignOut = {
                    navController.navigate("auth") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}