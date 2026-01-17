package presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.router.stack.push
import domain.manager.LanguageManager
import org.koin.compose.koinInject
import presentation.components.BottomNavBar
import presentation.screens.auth.AuthScreen
import presentation.screens.confirmation.ConfirmationScreen
import presentation.screens.detail.MovieDetailScreen
import presentation.screens.home.HomeScreen
import presentation.screens.movies.MoviesScreen
import presentation.screens.user.UserScreen
import presentation.screens.seats.SeatSelectionScreen
import presentation.screens.shows.ShowsScreen
import presentation.screens.splash.SplashScreen
import presentation.screens.tickets.TicketDetailScreen
import presentation.screens.tickets.TicketsScreen
import presentation.theme.LocalStrings

@Composable
fun RootScreen(
    child: RootComponent.Child,
    component: RootComponent
) {
    // inject the LanguageManager
    val languageManager: LanguageManager = koinInject()

    // observe current strings
    val currentStrings by languageManager.appStrings.collectAsState()

    CompositionLocalProvider(LocalStrings provides currentStrings) {
        Scaffold(
            bottomBar = {
                if (
                    child is RootComponent.Child.Home ||
                    child is RootComponent.Child.Movies ||
                    child is RootComponent.Child.Tickets ||
                    child is RootComponent.Child.User
                ) {
                    BottomNavBar(
                        stack = component.childStack,
                        onTabClick = { tab -> component.onTabClick(tab) }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                // display active child's UI
                when (child) {
                    is RootComponent.Child.Home -> HomeScreen(
                        viewModel = child.viewModel,
                        onNavigateToMovies = { initialTab -> component.onNavigateToMovies(initialTab) },
                        onMovieClick = { movieId -> component.onMovieClick(movieId) }
                    )

                    is RootComponent.Child.Movies -> MoviesScreen(
                        child.viewModel,
                        onMovieClick = { movieId -> component.onMovieClick(movieId) }
                    )

                    is RootComponent.Child.Tickets -> TicketsScreen(
                        viewModel = child.viewModel,
                        onTicketClick = { ticketId -> component.onNavigateToTicketDetail(ticketId) }
                    )

                    is RootComponent.Child.TicketDetail -> TicketDetailScreen(
                        ticket = child.ticket,
                        onBackClick = { component.onBackClicked() }
                    )

                    is RootComponent.Child.User -> UserScreen(
                        viewModel = child.viewModel,
                        onEditProfileClick = { /* TODO */ },
                        onNavigateToAuth = {
                            component.onNavigateToAuth()
                        }
                    )

                    is RootComponent.Child.Detail -> MovieDetailScreen(
                        viewModel = child.viewModel,
                        onBackClick = { component.onBackClicked() },
                        onBookNowClick = { movieId, movieTitle ->
                            component.onNavigateToShows(movieId, movieTitle)
                        }
                    )

                    is RootComponent.Child.Shows -> ShowsScreen(
                        viewModel = child.viewModel,
                        onBackClick = { component.onBackClicked() },
                        onShowtimeClick = { cinemaName, time ->
                            component.onNavigateToSeatSelection(
                                movieId = child.movieId,
                                movieTitle = child.viewModel.uiState.value.movieTitle,
                                cinema = cinemaName,
                                date = child.viewModel.uiState.value.selectedDate!!,
                                time = time
                            )
                        }
                    )

                    is RootComponent.Child.SeatSelection -> SeatSelectionScreen(
                        viewModel = child.viewModel,
                        movieTitle = child.movieTitle,
                        onBackClick = { component.onBackClicked() },
                        onNavigateToConfirmation = { ticketId ->
                            if (ticketId == "REQUIRE_LOGIN") {
                                // if event is special screen (user not logged in), go to Auth
                                component.onNavigateToAuth()
                            } else {
                                component.onNavigateToConfirmation(ticketId)
                            }
                        }
                    )

                    is RootComponent.Child.Confirmation -> ConfirmationScreen(
                        ticketId = child.ticketId,
                        onViewBookingClick = { component.onNavigateToTickets() }
                    )

                    is RootComponent.Child.Auth -> AuthScreen(
                        viewModel = child.viewModel,
                        onAuthSuccess = { component.onAuthSuccessOrSkip() },
                        onSkip = { component.onAuthSuccessOrSkip() }
                    )

                    RootComponent.Child.Splash -> SplashScreen()
                }
            }
        }
    }
}