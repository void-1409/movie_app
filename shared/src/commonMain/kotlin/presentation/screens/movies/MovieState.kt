package presentation.screens.movies

import domain.model.Movie

data class  MovieState(
    val movies: List<Movie> = emptyList(),
    val selectedTab: MovieTab = MovieTab.NOW_PLAYING,
    val selectedFilters: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)