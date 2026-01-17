package presentation.screens.seats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.dummy.generateDummySeatLayout
import domain.model.Seat
import domain.model.SeatStatus
import domain.model.Ticket
import domain.repository.AuthRepository
import domain.repository.MovieRepository
import domain.repository.TicketRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SeatSelectionViewModel(
    private val movieId: Int,
    private val cinema: String,
    private val date: LocalDate,
    private val time: String,
    private val ticketRepository: TicketRepository,
    private val movieRepository: MovieRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SeatSelectionState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

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

    @OptIn(ExperimentalUuidApi::class)
    fun onBuyTicketsClicked() {
        viewModelScope.launch {
            // 1. Check User Authentication
            val userEmail = authRepository.getCurrentUserEmail()
            if (userEmail == null) {
                _navigationEvent.emit("REQUIRE_LOGIN")
                return@launch
            }
            
            val currentState = _uiState.value
            if (currentState.selectedSeats.isEmpty()) return@launch

            val movie = movieRepository.getMovieById(movieId)

            // format duration
            val hours = movie.runtime?.div(60)
            val minutes = movie.runtime?.rem(60)
            val formattedDuration = "${hours}h ${minutes}m"
            val formattedGenre = movie.genres.joinToString { it.name }

            val newTicket = Ticket(
                id = Uuid.random().toString(),
                movieTitle = movie.title,
                posterUrl = movie.posterPath.toString(),
                genre = formattedGenre,
                duration = formattedDuration,
                cinemaName = cinema,
                location = "Deggendorf",
                date = date,
                time = time,
                selectedSeats = currentState.selectedSeats,
                totalPrice = currentState.totalPrice,
            )
            ticketRepository.addTicket(newTicket)

            // emit the id to trigger navigation
            _navigationEvent.emit(newTicket.id)
        }
    }
}