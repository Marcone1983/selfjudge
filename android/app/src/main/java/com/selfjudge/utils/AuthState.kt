package com.selfjudge.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun rememberAuthState(): AuthState {
    var user by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            user = auth.currentUser
            isLoading = false
        }
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    return AuthState(
        user = user,
        isLoading = isLoading,
        isAuthenticated = user != null
    )
}

data class AuthState(
    val user: FirebaseUser?,
    val isLoading: Boolean,
    val isAuthenticated: Boolean
)