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
                    onNavigateToMovies = { component.onTabClick(MainNavTab.MOVIES) },
                    onMovieClick = { movieId -> component.onMovieClick(movieId) }
                )
                is RootComponent.Child.Movies -> MoviesScreen()
                is RootComponent.Child.Tickets -> TicketsScreen()
                is RootComponent.Child.Profile -> ProfileScreen()
                is RootComponent.Child.Detail -> MovieDetailScreen(child.movieId)
            }
        }
    }
}