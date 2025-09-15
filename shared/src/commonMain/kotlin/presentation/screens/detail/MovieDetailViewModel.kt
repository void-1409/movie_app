package presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Int,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchMovieDetail()
    }

    private fun fetchMovieDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // fetch details and credits concurrently
                val movieDetailDeferred = async { movieRepository.getMovieById(movieId) }
                val movieCreditsDeferred = async { movieRepository.getMovieCredits(movieId) }

                val movie = movieDetailDeferred.await()
                val credits = movieCreditsDeferred.await()

                // filtering cast and crew
                val topCast = credits.cast.take(6)      // top 6 members
                val importantCrew = credits.crew.filter {
                    it.job == "Director" || it.job == "Producer" || it.job == "Screenplay"
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        movie = movie,
                        cast = topCast,
                        crew = importantCrew
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}