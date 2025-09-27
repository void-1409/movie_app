package presentation.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.Movie
import domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieState())
    val uiState = _uiState.asStateFlow()

    init {
        // load initial list of movies for the default tab
        loadMovies()
    }

    fun onTabSelected(tab: MovieTab) {
        // update the state with the new tab and reload the movies
        _uiState.update { it.copy(selectedTab = tab) }
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // fetch movies based on current tab
                val movies = when (_uiState.value.selectedTab) {
                    MovieTab.NOW_PLAYING -> movieRepository.getNowPlayingMovies()
                    MovieTab.COMING_SOON -> movieRepository.getUpComingMovies()
                }
                _uiState.update { it.copy(isLoading = false, movies = movies) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onFilterToggled(filter: String) {
        _uiState.update { currentState ->
            val newFilters = currentState.selectedFilters.toMutableList()
            if (newFilters.contains(filter)) {
                newFilters.remove(filter)
            } else {
                newFilters.add(filter)
            }
            currentState.copy(selectedFilters = newFilters)
        }
    }
}