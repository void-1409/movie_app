package domain.repository

import domain.model.Movie

interface MovieRepository {
    suspend fun getTrendingMovies(): List<Movie>
    suspend fun getNowPlayingMovies(): List<Movie>
    suspend fun getUpComingMovies(): List<Movie>
}