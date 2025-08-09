package com.selfjudge.ui.screens.battle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.selfjudge.R
import com.selfjudge.data.model.Judgment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleScreen(
    viewModel: BattleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.battle)) }
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
            // Artist input
            OutlinedTextField(
                value = uiState.artist,
                onValueChange = viewModel::updateArtist,
                label = { Text(stringResource(R.string.artist_name)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Text A
                OutlinedTextField(
                    value = uiState.textA,
                    onValueChange = viewModel::updateTextA,
                    label = { Text(stringResource(R.string.text_a)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp),
                    enabled = !uiState.isLoading,
                    maxLines = 8
                )

                // Text B
                OutlinedTextField(
                    value = uiState.textB,
                    onValueChange = viewModel::updateTextB,
                    label = { Text(stringResource(R.string.text_b)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp),
                    enabled = !uiState.isLoading,
                    maxLines = 8
                )
            }

            // Battle button
            Button(
                onClick = { viewModel.startBattle() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && 
                    uiState.artist.isNotBlank() && 
                    uiState.textA.isNotBlank() && 
                    uiState.textB.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text(stringResource(R.string.start_battle))
                }
            }

            // Error message
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Battle results
            uiState.result?.let { result ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.battle_results),
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        // Winner announcement
                        val winnerText = when (result.winner) {
                            "A" -> "🏆 Vincitore: Testo A (${result.averageA}/10)"
                            "B" -> "🏆 Vincitore: Testo B (${result.averageB}/10)"
                            else -> "🤝 ${stringResource(R.string.draw)} (${result.averageA}/10 vs ${result.averageB}/10)"
                        }
                        
                        Text(
                            text = winnerText,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        LazyColumn(
                            modifier = Modifier.height(400.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                Text(
                                    text = "Testo A - ${result.averageA}/10",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            items(result.judgeA.judgments) { judgment ->
                                JudgmentCard(judgment)
                            }
                            
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Testo B - ${result.averageB}/10",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            items(result.judgeB.judgments) { judgment ->
                                JudgmentCard(judgment)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JudgmentCard(judgment: Judgment) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = judgment.persona,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${judgment.score}/10",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = judgment.comment,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}