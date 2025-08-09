package com.selfjudge.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selfjudge.data.repository.SelfJudgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel : ViewModel() {
    private val repository = SelfJudgeRepository()
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        val isAuthenticated = repository.getCurrentUser() != null
        _uiState.value = _uiState.value.copy(isAuthenticated = isAuthenticated)
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun signIn() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Compila tutti i campi")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            repository.signIn(_uiState.value.email, _uiState.value.password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                    
                    // Initialize user on backend
                    repository.bootstrap()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Errore di autenticazione"
                    )
                }
        }
    }

    fun signUp() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Compila tutti i campi")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            repository.signUp(_uiState.value.email, _uiState.value.password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                    
                    // Initialize user on backend
                    repository.bootstrap()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Errore di registrazione"
                    )
                }
        }
    }
}