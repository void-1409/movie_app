package domain.model

import kotlinx.datetime.LocalDate

data class Ticket(
    val id: String,
    val movieTitle: String,
    val posterUrl: String,
    val genre: String,
    val duration: String,
    val cinemaName: String,
    val location: String,
    val date: LocalDate,
    val time: String,
    val selectedSeats: List<Seat>,
    val totalPrice: Float,
    val bookingNumber: String = (100000..999999).random().toString()
)