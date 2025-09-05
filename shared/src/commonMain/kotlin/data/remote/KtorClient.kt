package data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    // plugin that allows Ktor to automatically parse
    // JSON responses into our data class
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    // runs for every single request we make
    defaultRequest {
        // base url for TMDB API calls
        url("https://api.themoviedb.org/3/")
    }
}