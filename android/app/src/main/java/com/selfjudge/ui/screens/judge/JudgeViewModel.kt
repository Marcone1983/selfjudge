package com.selfjudge.ui.screens.judge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selfjudge.data.model.JudgeResponse
import com.selfjudge.data.repository.SelfJudgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JudgeUiState(
    val artist: String = "",
    val lyrics: String = "",
    val isLoading: Boolean = false,
    val result: JudgeResponse? = null,
    val errorMessage: String? = null
)

class JudgeViewModel : ViewModel() {
    private val repository = SelfJudgeRepository()
    
    private val _uiState = MutableStateFlow(JudgeUiState())
    val uiState: StateFlow<JudgeUiState> = _uiState.asStateFlow()

    fun updateArtist(artist: String) {
        _uiState.value = _uiState.value.copy(artist = artist, errorMessage = null)
    }

    fun updateLyrics(lyrics: String) {
        _uiState.value = _uiState.value.copy(lyrics = lyrics, errorMessage = null)
    }

    fun evaluate() {
        if (_uiState.value.artist.isBlank() || _uiState.value.lyrics.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Compila tutti i campi")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, result = null)
            
            repository.judge(_uiState.value.artist, _uiState.value.lyrics)
                .onSuccess { result ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        result = result
                    )
                }
                .onFailure { exception ->
                    val errorMessage = when {
                        exception.message?.contains("402") == true -> "Crediti insufficienti"
                        exception.message?.contains("400") == true -> "Testo non consentito"
                        else -> exception.message ?: "Errore durante la valutazione"
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
        }
    }
}