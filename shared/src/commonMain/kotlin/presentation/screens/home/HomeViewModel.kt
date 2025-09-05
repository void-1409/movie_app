package presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.Movie
import domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch { // use viewModelScope to launch a coroutine
            _uiState.update { it.copy(isLoading = true) }

            try {
                // launch all calls in parallel
                val trendingDeferred = async { movieRepository.getTrendingMovies() }
                val nowPlayingDeferred = async { movieRepository.getNowPlayingMovies() }
                val upcomingDeferred = async { movieRepository.getUpComingMovies() }

                // wait for all to complete
                val (trendingMovies, nowPlayingMovies, upcomingMovies) = awaitAll(
                    trendingDeferred,
                    nowPlayingDeferred,
                    upcomingDeferred
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        trendingMovies = trendingMovies.take(5),
                        nowPlayingMovies = nowPlayingMovies.take(6),
                        upcomingMovies = upcomingMovies.take(6)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error fetching movies: ${e.message}"
                    )
                }
            }
        }
    }
}