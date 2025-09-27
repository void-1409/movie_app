package presentation.screens.shows

import androidx.lifecycle.ViewModel
import data.dummy.dummyShowtimes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class ShowsViewModel(
    movieTitle: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShowsState())
    val uiState = _uiState.asStateFlow()

    init {
        // get current date and generate next 7 days
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val upcomingDates = List(7) { day ->
            today.plus(day, DateTimeUnit.DAY)
        }

        _uiState.update {
            it.copy(
                movieTitle = movieTitle,
                dates = upcomingDates,
                selectedDate = upcomingDates.first(),
                showtimes = dummyShowtimes
            )
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        // fetch new showtimes for selected date here
    }
}