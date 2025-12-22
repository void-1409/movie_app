package presentation.theme

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

object DeStrings : AppStrings {
    override val tabHome = "Start"
    override val tabMovies = "Entdecken"
    override val tabTickets = "Tickets"
    override val tabUser = "Profil"

    override val trending = "Trends"
    override val nowPlaying = "Jetzt im Kino"
    override val upcomingMovies = "Demnächst"
    override val searchText = "Filme, Kinos suchen..."

    override val engChip = "Englisch"
    override val gerChip = "Deutsch"

    override val storyline = "Handlung"
    override val cast = "Besetzung"
    override val director = "Regie"
    override val upcomingShows = "Heutige Vorstellungen"
    override val bookNow = "Jetzt Buchen"

    override val available = "Frei"
    override val reserved = "Reserviert"
    override val selected = "Ausgewählt"
    override val total = "Gesamt"
    override val buyTickets = "Ticket(s) Kaufen"

    override val bookingConfirmed = "Buchung bestätigt!"
    override val yourTicketId = "Deine Ticket-ID lautet"
    override val viewBooking = "Meine Buchungen ansehen"

    override val myTickets = "Meine Tickets"
    override val noTickets = "No Tickets Yet"
    override val upNext = "Als Nächstes"
    override val later = "Später"
    override val date = "Datum"
    override val time = "Zeit"
    override val seats = "Plätze"
    override val price = "Preis"
    override val showCode = "Code bitte am Einlass vorzeigen"
    override val bookingNumber = "Buchungsnummer"

    override val greeting = "Hallo"
    override val editProfile = "Profil bearbeiten"
    override val bookingHistory = "Buchungsverlauf"
    override val changeLanguage = "Sprache ändern"
    override val selectLanguage = "Sprache auswählen"
    override val paymentMethods = "Zahlungsmethoden"
    override val faceId = "Face ID / Touch ID"
    override val helpCenter = "Hilfezentrum"
    override val signOut = "Abmelden"

    // logic for dates
    override fun getShortDayName(date: LocalDate): String {
        return when (date.dayOfWeek) {
            DayOfWeek.MONDAY -> "MO"
            DayOfWeek.TUESDAY -> "DI"
            DayOfWeek.WEDNESDAY -> "MI"
            DayOfWeek.THURSDAY -> "DO"
            DayOfWeek.FRIDAY -> "FR"
            DayOfWeek.SATURDAY -> "SA"
            DayOfWeek.SUNDAY -> "SO"
            else -> date.dayOfWeek.name.take(2).uppercase()
        }
    }

    override fun getShortMonthName(date: LocalDate): String {
        return when (date.month) {
            Month.JANUARY -> "JAN"
            Month.FEBRUARY -> "FEB"
            Month.MARCH -> "MÄR"
            Month.APRIL -> "APR"
            Month.MAY -> "MAY"
            Month.JUNE -> "JUN"
            Month.JULY -> "JUL"
            Month.AUGUST -> "AUG"
            Month.SEPTEMBER -> "SEP"
            Month.OCTOBER -> "OKT"
            Month.NOVEMBER -> "NOV"
            Month.DECEMBER -> "DEZ"
            else -> date.month.name.take(3)
        }
    }
}
