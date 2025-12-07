package domain.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import presentation.theme.AppStrings
import presentation.theme.DeStrings
import presentation.theme.EnStrings

enum class AppLanguage {
    ENGLISH, GERMAN
}

class LanguageManager {
    // keep track of Enum
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage = _currentLanguage.asStateFlow()

    // keep track of actual string object
    private val _appStrings = MutableStateFlow<AppStrings>(EnStrings)
    val appStrings = _appStrings.asStateFlow()

    fun toggleLanguage() {
        val newLang = if (_currentLanguage.value == AppLanguage.ENGLISH) AppLanguage.GERMAN else AppLanguage.ENGLISH
        _currentLanguage.value = newLang

        // switch the object provided to UI
        _appStrings.value = if (newLang == AppLanguage.ENGLISH) EnStrings else DeStrings
    }
}