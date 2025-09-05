package domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val rating: Double,
    val durationMinutes: Int
)