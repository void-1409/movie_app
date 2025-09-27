package presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import presentation.components.BottomNavBar
import presentation.screens.detail.MovieDetailScreen
import presentation.screens.home.HomeScreen
import presentation.screens.movies.MoviesScreen
import presentation.screens.profile.ProfileScreen
import presentation.screens.seats.SeatSelectionScreen
import presentation.screens.shows.ShowsScreen
import presentation.screens.tickets.TicketsScreen

@Composable
fun RootScreen(
    child: RootComponent.Child,
    component: RootComponent
) {
    Scaffold(
        bottomBar = {
            if (
                child is RootComponent.Child.Home ||
                child is RootComponent.Child.Movies ||
                child is RootComponent.Child.Tickets ||
                child is RootComponent.Child.Profile
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
                is RootComponent.Child.Tickets -> TicketsScreen()
                is RootComponent.Child.Profile -> ProfileScreen()
                is RootComponent.Child.Detail -> MovieDetailScreen(
                    viewModel = child.viewModel,
                    onBackClick = { component.onBackClicked() },
                    onBookNowClick = { movieTitle -> component.onNavigateToShows(movieTitle) }
                )
                is RootComponent.Child.Shows -> ShowsScreen(
                    viewModel = child.viewModel,
                    onBackClick = { component.onBackClicked() },
                    onShowtimeClick = { cinemaName, time ->
                        component.onNavigateToSeatSelection(
                            movieTitle = child.viewModel.uiState.value.movieTitle,
                            cinema = cinemaName,
                            date = child.viewModel.uiState.value.selectedDate!!,
                            time = time
                        )
                    }
                )
                is RootComponent.Child.SeatSelection -> SeatSelectionScreen(
                    child.movieTitle, child.cinema, child.date, child.time
                )
            }
        }
    }
}