package com.selfjudge.ui.screens.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selfjudge.data.model.BattleResponse
import com.selfjudge.data.repository.SelfJudgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BattleUiState(
    val artist: String = "",
    val textA: String = "",
    val textB: String = "",
    val isLoading: Boolean = false,
    val result: BattleResponse? = null,
    val errorMessage: String? = null
)

class BattleViewModel : ViewModel() {
    private val repository = SelfJudgeRepository()
    
    private val _uiState = MutableStateFlow(BattleUiState())
    val uiState: StateFlow<BattleUiState> = _uiState.asStateFlow()

    fun updateArtist(artist: String) {
        _uiState.value = _uiState.value.copy(artist = artist, errorMessage = null)
    }

    fun updateTextA(textA: String) {
        _uiState.value = _uiState.value.copy(textA = textA, errorMessage = null)
    }

    fun updateTextB(textB: String) {
        _uiState.value = _uiState.value.copy(textB = textB, errorMessage = null)
    }

    fun startBattle() {
        if (_uiState.value.artist.isBlank() || _uiState.value.textA.isBlank() || _uiState.value.textB.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Compila tutti i campi")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, result = null)
            
            repository.battle(_uiState.value.artist, _uiState.value.textA, _uiState.value.textB)
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
                        else -> exception.message ?: "Errore durante la battaglia"
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
        }
    }
}