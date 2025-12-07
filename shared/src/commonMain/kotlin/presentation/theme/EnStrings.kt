package presentation.theme

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

object EnStrings : AppStrings {
    override val tabHome = "Home"
    override val tabMovies = "Movies"
    override val tabTickets = "Tickets"
    override val tabUser = "User"

    override val upNext = "Up Next"
    override val later = "Later"
    override val bookNow = "Book Now"

    override val myTickets = "My Tickets"
    override val noTickets = "No tickets yet"
    override val date = "Date"
    override val time = "Time"
    override val seats = "Seats"
    override val price = "Price"
    override val bookingId = "Booking ID"
    override val paid = "PAID"

    override val greeting = "Hi"
    override val editProfile = "Edit Profile"
    override val bookingHistory = "Booking History"
    override val changeLanguage = "Change Language"
    override val paymentMethods = "Payment Methods"
    override val faceId = "Face ID / Touch ID"
    override val helpCenter = "Help Center"
    override val signOut = "Sign Out"

    // logic for dates
    private val days = mapOf(
        DayOfWeek.MONDAY to "Mon",
        DayOfWeek.TUESDAY to "Tue",
        DayOfWeek.WEDNESDAY to "Wed",
        DayOfWeek.THURSDAY to "Thu",
        DayOfWeek.FRIDAY to "Fri",
        DayOfWeek.SATURDAY to "Sat",
        DayOfWeek.SUNDAY to "Sun",
    )

    private val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Spe", "Oct", "Nov", "Dec"
    )

    override fun formatDate(date: LocalDate): String {
        val dayName = days[date.dayOfWeek] ?: ""
        val monthName = months[date.monthNumber - 1]
        val dayNum = date.dayOfMonth.toString().padStart(2, '0')
        return "$dayName, $dayNum $monthName ${date.year}"
    }

    override fun formatPrice(value: Float): String = "€$value"
}
