package presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.datetime.LocalDate

interface AppStrings {
    // --- Tabs ---
    val tabHome: String
    val tabMovies: String
    val tabTickets: String
    val tabUser: String

    // --- Home / Movies ---
    val upNext: String
    val later: String
    val bookNow: String

    // --- Tickets ---
    val myTickets: String
    val noTickets: String
    val date: String
    val time: String
    val seats: String
    val price: String
    val bookingId: String
    val paid: String

    // --- User Screen ---
    val greeting: String
    val editProfile: String
    val bookingHistory: String
    val changeLanguage: String
    val paymentMethods: String
    val faceId: String
    val helpCenter: String
    val signOut: String

    // --- Dynamic Formatting ---
    fun formatDate(date: LocalDate): String
    fun formatPrice(value: Float): String
}

// global accessor (Default to English initially)
val LocalStrings = staticCompositionLocalOf<AppStrings> { EnStrings }