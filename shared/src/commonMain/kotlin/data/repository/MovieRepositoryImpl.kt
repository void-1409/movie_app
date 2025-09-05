package data.repository

import data.remote.ApiService
import data.remote.dto.MovieResultDto
import domain.model.Movie
import domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val apiService: ApiService
) : MovieRepository {

    override suspend fun getTrendingMovies(): List<Movie> {
        val response = apiService.getTrendingMovies()
        return response.results.map { it.toDomain() }
    }

    override suspend fun getNowPlayingMovies(): List<Movie> {
        val response = apiService.getNowPlayingMovies()
        return response.results.map { it.toDomain() }
    }

    override suspend fun getUpComingMovies(): List<Movie> {
        val response = apiService.getUpcomingMovies()
        return response.results.map { it.toDomain() }
    }

    // mapper function to convert the raw DTO to our clean domain model
    private fun MovieResultDto.toDomain(): Movie {
        return Movie(
            id = this.id,
            title = this.title,
            posterUrl = "https://image.tmdb.org/t/p/w500${this.posterPath}",
            rating = this.voteAverage,
            durationMinutes = 130   // default placeholder for now
        )
    }
}