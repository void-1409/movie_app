package presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.manager.AppLanguage
import domain.manager.LanguageManager
import domain.repository.AuthRepository
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// state to hold text inputs and loading status
data class AuthUiState(
    val isSignUp: Boolean = false,  // toggle between login/signup
    val email: String = "",
    val password: String = "",
    val fullName: String = "",      // for sign-up
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH
)

sealed interface AuthEvent {
    data object AuthSuccess: AuthEvent
    data object Skipped : AuthEvent
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val languageManager: LanguageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _authEvent = Channel<AuthEvent>()
    val authEvent = _authEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            languageManager.currentLanguage.collect { lang ->
                _uiState.update { it.copy(currentLanguage = lang) }
            }
        }
    }

    fun toggleMode() {
        _uiState.update { it.copy(isSignUp = !it.isSignUp, error = null) }
    }

    fun onEmailChange(text: String) {
        _uiState.update { it.copy(email = text, error = null) }
    }

    fun onPasswordChange(text: String) {
        _uiState.update { it.copy(password = text, error = null) }
    }

    fun onNameChange(text: String) {
        _uiState.update { it.copy(fullName = text, error = null) }
    }

    fun onSubmit() {
        if (_uiState.value.isSignUp) signUp() else signIn()
    }

    private fun signIn() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.signIn(_uiState.value.email, _uiState.value.password)
                .fold(
                    onSuccess = {
                        _uiState.update { it.copy(isLoading = false) }
                        _authEvent.send(AuthEvent.AuthSuccess)
                    },
                    onFailure = { err ->
                        _uiState.update { it.copy(isLoading = false, error = err.message) }
                    }
                )
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.signUp(_uiState.value.email, _uiState.value.password, _uiState.value.fullName)
                .fold(
                    onSuccess = {
                        _uiState.update { it.copy(isLoading = false) }
                        _authEvent.send(AuthEvent.AuthSuccess)
                    },
                    onFailure = { err ->
                        _uiState.update { it.copy(isLoading = false, error = err.message) }
                    }
                )
        }
    }

    fun onSkipClicked() {
        viewModelScope.launch {
            _authEvent.send(AuthEvent.Skipped)
        }
    }

    fun onLanguageSelected(language: AppLanguage) {
        languageManager.setLanguage(language)
    }
}