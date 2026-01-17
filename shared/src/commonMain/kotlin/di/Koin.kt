package di

import com.cinemate.shared.ApiKey
import data.remote.ApiServiceImpl
import data.remote.httpClient
import data.repository.AuthRepositoryImpl
import data.repository.MovieRepositoryImpl
import domain.manager.LanguageManager
import domain.repository.AuthRepository
import domain.repository.MovieRepository
import domain.repository.TicketRepository
import domain.repository.TicketRepositoryImpl
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.datetime.LocalDate
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import presentation.screens.auth.AuthViewModel
import presentation.screens.detail.MovieDetailViewModel
import presentation.screens.home.HomeViewModel
import presentation.screens.movies.MoviesViewModel
import presentation.screens.seats.SeatSelectionViewModel
import presentation.screens.shows.ShowsViewModel
import presentation.screens.tickets.TicketsViewModel
import presentation.screens.user.UserViewModel

val appModule = module {
    // provide a singleton instance of our HttpClient
    single { httpClient }
    // provide a singleton instance of ApiServiceImpl, giving it to HttpClient
    single<data.remote.ApiService> { ApiServiceImpl(get()) }
    // provide a singleton instance of MovieRepositoryImpl, giving it to ApiService
    single<MovieRepository> { MovieRepositoryImpl(get()) }

    single<TicketRepository> { TicketRepositoryImpl }

    single { LanguageManager() }

    // supabase client
    single {
        createSupabaseClient(
            supabaseUrl = ApiKey.SUPABASE_URL,
            supabaseKey = ApiKey.SUPABASE_KEY
        ) {
            // install the auth module (GoTrue)
            install(Auth)

            // install the database module (postgrest)
            install(Postgrest) {
                serializer = KotlinXSerializer()
            }
        }
    }

    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // provide a new instance of HomeViewModel every time its requested
    factory { HomeViewModel(get()) }

    factory { (movieId: Int) -> MovieDetailViewModel(movieId = movieId, movieRepository = get()) }

    factory { MoviesViewModel(get()) }

    factory { (movieTitle: String) -> ShowsViewModel(movieTitle = movieTitle) }

    factory { (movieId: Int, cinema: String, date: LocalDate, time: String) ->
        SeatSelectionViewModel(
            movieId = movieId,
            cinema = cinema,
            date = date,
            time = time,
            ticketRepository = get(),
            movieRepository = get()
        )
    }

    factory { TicketsViewModel(get()) }

    factory { UserViewModel(get()) }

    factory { AuthViewModel(get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule)
    }