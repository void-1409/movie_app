package presentation.screens.seats

import androidx.lifecycle.ViewModel
import data.dummy.generateDummySeatLayout
import domain.model.Seat
import domain.model.SeatStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SeatSelectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SeatSelectionState())
    val uiState = _uiState.asStateFlow()

    companion object {
        private const val PRICE_PER_SEAT = 12.0f    // fixed seat price for now
    }

    init {
        _uiState.update { it.copy(seats = generateDummySeatLayout()) }
    }

    fun onSeatSelected(seat: Seat) {
        // ignore clicks on reserved seats
        if (seat.status == SeatStatus.RESERVED) {
            return
        }

        _uiState.update { currentState ->
            // select up to 10 seats
            if (currentState.selectedSeats.size >= 10 && seat.status == SeatStatus.AVAILABLE) {
                return@update currentState  // exit without making changes
            }
            val updateSeats = currentState.seats.map {
                if (it.id == seat.id) {
                    it.copy(status = if (seat.status == SeatStatus.AVAILABLE) SeatStatus.SELECTED else SeatStatus.AVAILABLE)
                } else {
                    it
                }
            }

            // get the list of selected seats
            val selectedSeats = updateSeats.filter { it.status == SeatStatus.SELECTED }

            currentState.copy(
                seats = updateSeats,
                selectedSeats = selectedSeats,
                totalPrice = selectedSeats.size * PRICE_PER_SEAT
            )
        }
    }
}