package com.selfjudge.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.selfjudge.R
import com.selfjudge.ui.screens.battle.BattleScreen
import com.selfjudge.ui.screens.judge.JudgeScreen
import com.selfjudge.ui.screens.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(
        Triple(Icons.Default.Gavel, stringResource(R.string.judge), "judge"),
        Triple(Icons.Default.SportsKabaddi, stringResource(R.string.battle), "battle"),
        Triple(Icons.Default.Person, stringResource(R.string.profile), "profile")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, (icon, title, route) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = title) },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "judge",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("judge") {
                JudgeScreen()
            }
            composable("battle") {
                BattleScreen()
            }
            composable("profile") {
                ProfileScreen(onSignOut = onSignOut)
            }
        }
    }
}