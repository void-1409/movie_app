package presentation.theme

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

object EnStrings : AppStrings {
    override val tabHome = "Home"
    override val tabMovies = "Movies"
    override val tabTickets = "Tickets"
    override val tabUser = "User"

    override val trending = "Trending"
    override val nowPlaying = "In Cinemas"
    override val upcomingMovies = "Coming Soon"
    override val searchText = "Search for Movies, Cinemas..."

    override val engChip = "English"
    override val gerChip = "German"

    override val storyline = "Storyline"
    override val cast = "Cast"
    override val director = "Director"
    override val upcomingShows = "Upcoming Shows"
    override val bookNow = "Book Now"

    override val available = "Available"
    override val reserved = "Reserved"
    override val selected = "Selected"
    override val total = "Total"
    override val buyTickets = "Buy Ticket(s)"

    override val bookingConfirmed = "Booking Confirmed!"
    override val yourTicketId = "Your ticket ID is"
    override val viewBooking = "View My Bookings"

    override val myTickets = "My Tickets"
    override val noTickets = "No Tickets Yet"
    override val upNext = "Up Next"
    override val later = "Later"
    override val date = "Date"
    override val time = "Time"
    override val seats = "Seats"
    override val price = "Price"
    override val showCode = "Please show this code at entrance"
    override val bookingNumber = "Booking Number"

    override val greeting = "Hi"
    override val editProfile = "Edit Profile"
    override val bookingHistory = "Booking History"
    override val changeLanguage = "Change Language"
    override val selectLanguage = "Select Language"
    override val paymentMethods = "Payment Methods"
    override val faceId = "Face ID / Touch ID"
    override val helpCenter = "Help Center"
    override val signOut = "Sign Out"

    override fun getShortDayName(date: LocalDate): String {
        return date.dayOfWeek.name.take(3).uppercase()
    }

    override fun getShortMonthName(date: LocalDate): String {
        return date.month.name.take(3).uppercase()
    }
}
