package presentation.screens.shows

import domain.model.CinemaShowtimes
import kotlinx.datetime.LocalDate

data class ShowsState(
    val movieTitle: String = "",
    val dates: List<LocalDate> = emptyList(),
    val selectedDate: LocalDate? = null,
    val showtimes: List<CinemaShowtimes> = emptyList()
)