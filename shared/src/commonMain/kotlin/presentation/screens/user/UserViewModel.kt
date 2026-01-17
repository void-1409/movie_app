package presentation.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.manager.AppLanguage
import domain.manager.LanguageManager
import domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserState(
    val userName: String = "Guest",
    val profileImage: String = "",
    val isFaceIdEnabled: Boolean = true,
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isGuest: Boolean = true
)

class UserViewModel(
    private val languageManager: LanguageManager,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            languageManager.currentLanguage.collect { lang ->
                _uiState.update { it.copy(currentLanguage = lang) }
            }
        }
        // load user profile data
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val email = authRepository.getCurrentUserEmail()
            val name = authRepository.getCurrentUserName()

            if (email == null) {
                // GUEST MODE
                _uiState.update {
                    it.copy(
                        isGuest = true,
                        userName = "Guest",
                        profileImage = "https://img.icons8.com/?size=100&id=1TofO1XiGI5l&format=png&color=000000"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isGuest = false,
                        userName = name ?: "User",
                        profileImage = "https://img.icons8.com/?size=100&id=1TofO1XiGI5l&format=png&color=000000"   // TODO: fetch from db
                    )
                }
            }
        }
    }

    // login/logout button
    fun onAuthButtonClick(navigateToAuth: () -> Unit) {
        viewModelScope.launch {
            if (!_uiState.value.isGuest) {
                authRepository.signOut()
            }
            // redirect to Auth screen
            navigateToAuth()
        }
    }

    fun toggleFaceId(isEnabled: Boolean) {
        _uiState.update { it.copy(isFaceIdEnabled = isEnabled) }
    }

    fun onLanguageSelected(language: AppLanguage) {
        languageManager.setLanguage(language)
    }
}