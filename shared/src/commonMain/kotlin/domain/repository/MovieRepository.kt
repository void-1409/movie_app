package domain.repository

import data.remote.dto.MovieDetailDto
import domain.model.Movie

interface MovieRepository {
    suspend fun getTrendingMovies(): List<Movie>
    suspend fun getNowPlayingMovies(): List<Movie>
    suspend fun getUpComingMovies(): List<Movie>
    suspend fun getMovieById(movieId: Int): MovieDetailDto
}