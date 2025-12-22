package domain.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import presentation.theme.AppStrings
import presentation.theme.DeStrings
import presentation.theme.EnStrings

enum class AppLanguage(val displayName: String) {
    ENGLISH("English"),
    GERMAN("Deutsch")
}

class LanguageManager {
    // keep track of Enum
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage = _currentLanguage.asStateFlow()

    // keep track of actual string object
    private val _appStrings = MutableStateFlow<AppStrings>(EnStrings)
    val appStrings = _appStrings.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language

        // switch the object provided to UI
        _appStrings.value = when (language) {
            AppLanguage.ENGLISH -> EnStrings
            AppLanguage.GERMAN -> DeStrings
        }
    }
}