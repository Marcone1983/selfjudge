package com.selfjudge.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selfjudge.data.model.Credits
import com.selfjudge.data.repository.SelfJudgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userEmail: String? = null,
    val credits: Credits? = null,
    val showPurchaseDialog: Boolean = false,
    val isLoading: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val repository = SelfJudgeRepository()
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Get user email
            val user = repository.getCurrentUser()
            _uiState.value = _uiState.value.copy(userEmail = user?.email)
            
            // Bootstrap to get credits (doesn't give extra credits if already done)
            repository.bootstrap()
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        credits = response.credits,
                        isLoading = false
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
        }
    }

    fun showPurchaseDialog() {
        _uiState.value = _uiState.value.copy(showPurchaseDialog = true)
    }

    fun hidePurchaseDialog() {
        _uiState.value = _uiState.value.copy(showPurchaseDialog = false)
    }

    fun signOut() {
        repository.signOut()
    }
}