package presentation.screens.detail

import data.remote.dto.MovieDetailDto

data class MovieDetailState(
    val movie: MovieDetailDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)