package di

import data.remote.ApiServiceImpl
import data.remote.httpClient
import data.repository.MovieRepositoryImpl
import domain.manager.LanguageManager
import domain.repository.MovieRepository
import domain.repository.TicketRepository
import domain.repository.TicketRepositoryImpl
import kotlinx.datetime.LocalDate
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
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
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule)
    }