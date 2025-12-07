package presentation.screens.user

import androidx.lifecycle.ViewModel
import domain.manager.LanguageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserState(
    val userName: String = "Angelina",
    val profileImage: String = "https://media.istockphoto.com/id/672627304/de/foto/portr%C3%A4t-von-einem-sch%C3%B6nen-jungen-m%C3%A4dchen.jpg?s=1024x1024&w=is&k=20&c=KHY65mMTxbgth-vJ6I7aAyq2dQCRhaoZnK1NYTwO1uY=",
    val isFaceIdEnabled: Boolean = true
)

class UserViewModel(
    private val languageManager: LanguageManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserState())
    val uiState = _uiState.asStateFlow()

    fun toggleFaceId(isEnabled: Boolean) {
        _uiState.update { it.copy(isFaceIdEnabled = isEnabled) }
    }

    fun onToggleLanguage() {
        languageManager.toggleLanguage()
    }

    fun onSignOut() {
        // handle sign out logic
    }
}