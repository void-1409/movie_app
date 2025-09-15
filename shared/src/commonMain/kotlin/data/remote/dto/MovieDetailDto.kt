package data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailDto(
    val id: Int,
    val title: String,
    val overview: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    val genres: List<GenreDto>,
    val runtime: Int?,
    val tagline: String?
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)