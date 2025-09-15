package presentation.screens.detail

import data.remote.dto.CastDto
import data.remote.dto.CrewDto
import data.remote.dto.MovieDetailDto

data class MovieDetailState(
    val movie: MovieDetailDto? = null,
    val cast: List<CastDto> = emptyList(),
    val crew: List<CrewDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)