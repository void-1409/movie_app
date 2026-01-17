package presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// state to hold text inputs and loading status
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",  // for sign-up
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface AuthEvent {
    data object AuthSuccess: AuthEvent
}

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _authEvent = Channel<AuthEvent>()
    val authEvent = _authEvent.receiveAsFlow()

    fun onEmailChange(text: String) {
        _uiState.update { it.copy(email = text, error = null) }
    }

    fun onPasswordChange(text: String) {
        _uiState.update { it.copy(password = text, error = null) }
    }

    fun onNameChange(text: String) {
        _uiState.update { it.copy(password = text, error = null) }
    }

    fun onSignInClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.signIn(_uiState.value.email, _uiState.value.password)
            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = { _authEvent.send(AuthEvent.AuthSuccess) },
                onFailure = { _uiState.update { state -> state.copy(error = it.message) } }
            )
        }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.signUp(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.fullName
            )
            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = { _authEvent.send(AuthEvent.AuthSuccess) },
                onFailure = { _uiState.update { state -> state.copy(error = it.message) } }
            )
        }
    }
}