package presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.model.Movie
import presentation.components.MovieCard
import presentation.screens.movies.MovieTab
import presentation.theme.LocalStrings

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToMovies: (MovieTab) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // string for changing languages
    val strings = LocalStrings.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)   // space between sections
    ) {
        // --- Trending Section ---
        item {
            SectionHeader(strings.trending)
            Spacer(modifier = Modifier.height(8.dp))

            // Display movies
            if (uiState.trendingMovies.isNotEmpty()) {
                val listState: LazyListState = rememberLazyListState()

                // this effect runs when composable is first deployed
                LaunchedEffect(Unit) {
                    // Start in middle of "infinite" scroll for that effect
                    val initialIndex = Int.MAX_VALUE / 2
                    val offset = initialIndex % uiState.trendingMovies.size
                    listState.scrollToItem(initialIndex - offset, 0)
                }
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = Int.MAX_VALUE,  // virtually infinite list
                        key = { index ->
                            val movieIndex = index % uiState.trendingMovies.size
                            uiState.trendingMovies[movieIndex].id.toString() + index.toString()
                        }
                    ) { index ->
                        val movieIndex = index % uiState.trendingMovies.size
                        val movie = uiState.trendingMovies[movieIndex]
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) }
                        )
                    }
                }
            }
        }

        // --- Explore Movies Section
        item {
            SectionHeader(
                strings.nowPlaying,
                onArrowClick = { onNavigateToMovies(MovieTab.NOW_PLAYING) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.nowPlayingMovies) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onMovieClick(movie.id) }
                    )
                }
            }
        }

        // --- Upcoming Movies Section
        item {
            SectionHeader(
                title = strings.upcomingMovies,
                onArrowClick = { onNavigateToMovies(MovieTab.COMING_SOON) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.upcomingMovies) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onMovieClick(movie.id) }
                    )
                }
            }
        }
    }
}

// reusable section header with optional navigation arrow
@Composable
private fun SectionHeader(
    title: String,
    onArrowClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))  // Pushes the arrow to the end
        if (onArrowClick != null) {
            IconButton(onClick = onArrowClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View More"
                )
            }
        }
    }
}