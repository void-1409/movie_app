package domain.manager

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import presentation.theme.AppStrings
import presentation.theme.DeStrings
import presentation.theme.EnStrings

enum class AppLanguage(val displayName: String) {
    ENGLISH("English"),
    GERMAN("Deutsch")
}

class LanguageManager(private val settings: Settings) {

    // load saved language on startup
    private val savedLanguageName = settings.getString("APP_LANGUAGE_KEY", AppLanguage.ENGLISH.name)

    private val initialLanguage = try {
        AppLanguage.valueOf(savedLanguageName)
    } catch (e: Exception) {
        AppLanguage.ENGLISH
    }

    // keep track of Enum
    private val _currentLanguage = MutableStateFlow(initialLanguage)
    val currentLanguage = _currentLanguage.asStateFlow()

    // keep track of actual string object
    private val _appStrings = MutableStateFlow<AppStrings>(
        if (initialLanguage == AppLanguage.GERMAN) DeStrings else EnStrings
    )
    val appStrings = _appStrings.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language

        // switch the object provided to UI
        _appStrings.value = when (language) {
            AppLanguage.ENGLISH -> EnStrings
            AppLanguage.GERMAN -> DeStrings
        }

        // save to disk when changed
        settings["APP_LANGUAGE_KEY"] = language.name
    }
}