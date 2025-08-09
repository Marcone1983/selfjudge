package com.selfjudge.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.selfjudge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User info card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = uiState.userEmail ?: "Caricamento...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Credits card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.credits),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.solo_credits, uiState.credits?.solo ?: 0),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(R.string.battle_credits, uiState.credits?.pvp ?: 0),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    
                    if ((uiState.credits?.solo ?: 0) == 0 && (uiState.credits?.pvp ?: 0) == 0) {
                        Button(
                            onClick = { viewModel.showPurchaseDialog() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.buy_credits))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Sign out button
            OutlinedButton(
                onClick = {
                    viewModel.signOut()
                    onSignOut()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.sign_out))
            }
        }
    }

    // Purchase dialog (simple placeholder)
    if (uiState.showPurchaseDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hidePurchaseDialog() },
            title = { Text("Acquista Crediti") },
            text = { Text("Feature disponibile prossimamente!") },
            confirmButton = {
                TextButton(onClick = { viewModel.hidePurchaseDialog() }) {
                    Text("OK")
                }
            }
        )
    }
}