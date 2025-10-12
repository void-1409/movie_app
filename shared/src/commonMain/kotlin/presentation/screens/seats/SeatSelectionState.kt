package presentation.screens.seats

import domain.model.Seat

data class SeatSelectionState (
    val seats: List<Seat> = emptyList(),
    val selectedSeats: List<Seat> = emptyList(),
    val totalPrice: Float = 0.0f
)