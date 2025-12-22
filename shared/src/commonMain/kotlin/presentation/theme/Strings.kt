package presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.datetime.LocalDate

interface AppStrings {
    // --- Tabs ---
    val tabHome: String
    val tabMovies: String
    val tabTickets: String
    val tabUser: String

    // --- Homescreen ---
    val trending: String
    val nowPlaying: String
    val upcomingMovies: String
    val searchText: String

    // --- Explore Tab ---
    val engChip: String
    val gerChip: String

    // --- Movie Description ---
    val storyline: String
    val cast: String
    val director: String
    val upcomingShows: String
    val bookNow: String

    // --- Seat Selection ---
    val available: String
    val reserved: String
    val selected: String
    val total: String
    val buyTickets: String

    // --- Booking ---
    val bookingConfirmed: String
    val yourTicketId: String
    val viewBooking: String

    // --- Tickets ---
    val myTickets: String
    val noTickets: String
    val upNext: String
    val later: String
    val date: String
    val time: String
    val seats: String
    val price: String
    val showCode: String
    val bookingNumber: String

    // --- User Screen ---
    val greeting: String
    val editProfile: String
    val bookingHistory: String
    val changeLanguage: String
    val selectLanguage: String
    val paymentMethods: String
    val faceId: String
    val helpCenter: String
    val signOut: String

    // --- Dynamic Formatting ---
    fun getShortDayName(date: LocalDate): String
    fun getShortMonthName(date: LocalDate): String
}

// global accessor (Default to English initially)
val LocalStrings = staticCompositionLocalOf<AppStrings> { EnStrings }