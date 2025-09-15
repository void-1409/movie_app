package data.remote

import com.cinemate.shared.ApiKey
import data.remote.dto.MovieDetailDto
import data.remote.dto.MovieResponse
import domain.model.Movie
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

// contract for our API service
interface ApiService {
    suspend fun getTrendingMovies(page: Int = 1): MovieResponse
    suspend fun getNowPlayingMovies(page: Int = 1): MovieResponse
    suspend fun getUpcomingMovies(page: Int = 1): MovieResponse
    suspend fun getMovieById(movieId: Int): MovieDetailDto
}

class ApiServiceImpl(private val client: io.ktor.client.HttpClient) : ApiService {

    // makes HTTP GET request to /trending/movie/week endpoint
    override suspend fun getTrendingMovies(page: Int): MovieResponse {
        return try {
            client.get("trending/movie/week") {
                parameter("api_key", ApiKey.TMDB_API_KEY)
                parameter("page", page)
            }.body()
        } catch (e: Exception) {
            println("API Error: ${e.message}")
            MovieResponse(page = 0, results = emptyList(), totalPages = 0, totalResults = 0)
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): MovieResponse {
        return try {
            client.get("movie/now_playing") {
                parameter("api_key", ApiKey.TMDB_API_KEY)
                parameter("page", page)
                parameter("region", "DE")
            }.body()
        } catch (e: Exception) {
            println("API Error: ${e.message}")
            MovieResponse(page = 0, results = emptyList(), totalPages = 0, totalResults = 0)
        }
    }

    override suspend fun getUpcomingMovies(page: Int): MovieResponse {
        return try {
            client.get("movie/upcoming") {
                parameter("api_key", ApiKey.TMDB_API_KEY)
                parameter("page", page)
                parameter("region", "DE")
            }.body()
        } catch (e: Exception) {
            println("API Error: ${e.message}")
            MovieResponse(page = 0, results = emptyList(), totalPages = 0, totalResults = 0)
        }
    }

    override suspend fun getMovieById(movieId: Int): MovieDetailDto {
        return client.get("movie/$movieId") {
            parameter("api_key", ApiKey.TMDB_API_KEY)
        }.body()
    }
}