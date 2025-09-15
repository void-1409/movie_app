package data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCreditsDto(
    val id: Int,
    val cast: List<CastDto>,
    val crew: List<CrewDto>
)

@Serializable
data class CastDto(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?
)

@Serializable
data class CrewDto(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?,
    val job: String
)