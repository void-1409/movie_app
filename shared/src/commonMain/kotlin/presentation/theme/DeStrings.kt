package presentation.theme

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

object DeStrings : AppStrings {
    override val tabHome = "Start"
    override val tabMovies = "Filme"
    override val tabTickets = "Tickets"
    override val tabUser = "Profil"

    override val upNext = "Als Nächstes"
    override val later = "Später"
    override val bookNow = "Jetzt Buchen"

    override val myTickets = "Meine Tickets"
    override val noTickets = "Noch keine Tickets"
    override val date = "Datum"
    override val time = "Zeit"
    override val seats = "Plätze"
    override val price = "Preis"
    override val bookingId = "Buchungsnummer"
    override val paid = "BEZAHLT"

    override val greeting = "Hallo"
    override val editProfile = "Profil bearbeiten"
    override val bookingHistory = "Buchungsverlauf"
    override val changeLanguage = "Sprache ändern"
    override val paymentMethods = "Zahlungsmethoden"
    override val faceId = "Face ID / Touch ID"
    override val helpCenter = "Hilfezentrum"
    override val signOut = "Abmelden"

    // logic for dates
    private val days = mapOf(
        DayOfWeek.MONDAY to "Mo.",
        DayOfWeek.TUESDAY to "Di.",
        DayOfWeek.WEDNESDAY to "Mi.",
        DayOfWeek.THURSDAY to "Do.",
        DayOfWeek.FRIDAY to "Fr.",
        DayOfWeek.SATURDAY to "Sa.",
        DayOfWeek.SUNDAY to "So.",
        )

    private val months = listOf(
        "Jan.", "Feb.", "März", "Apr.", "Mai", "Juni",
        "Juli", "Aug.", "Sept.", "Okt.", "Nov.", "Dez."
    )

    override fun formatDate(date: LocalDate): String {
        val dayName = days[date.dayOfWeek] ?: ""
        val monthName = months[date.monthNumber - 1]
        val dayNum = date.dayOfMonth.toString().padStart(2, '0')
        return "$dayName, $dayNum $monthName ${date.year}"
    }

    override fun formatPrice(value: Float): String = "$value €"
}
