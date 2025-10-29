package domain.model

import kotlinx.datetime.LocalDate

data class Ticket(
    val id: String,
    val movieTitle: String,
    val cinemaName: String,
    val date: LocalDate,
    val time: String,
    val selectedSeats: List<Seat>,
    val totalPrice: Float
)