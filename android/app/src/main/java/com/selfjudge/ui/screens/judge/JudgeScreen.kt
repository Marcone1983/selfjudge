package com.selfjudge.ui.screens.judge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.selfjudge.R
import com.selfjudge.data.model.Judgment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JudgeScreen(
    viewModel: JudgeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.judge)) }
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

            // Lyrics input
            OutlinedTextField(
                value = uiState.lyrics,
                onValueChange = viewModel::updateLyrics,
                label = { Text(stringResource(R.string.your_lyrics)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                enabled = !uiState.isLoading,
                maxLines = 10
            )

            // Evaluate button
            Button(
                onClick = { viewModel.evaluate() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && uiState.artist.isNotBlank() && uiState.lyrics.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text(stringResource(R.string.evaluate))
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

            // Results
            uiState.result?.let { result ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.evaluation_results),
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Text(
                            text = "Media: ${result.average}/10",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        LazyColumn(
                            modifier = Modifier.height(300.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(result.judgments) { judgment ->
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