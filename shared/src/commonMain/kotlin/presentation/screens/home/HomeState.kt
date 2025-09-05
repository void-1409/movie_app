package presentation.screens.home

import domain.model.Movie
import io.kamel.core.Resource

data class HomeState(
    val trendingMovies: List<Movie> = emptyList(),
    val nowPlayingMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)