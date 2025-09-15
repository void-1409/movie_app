package presentation.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.remote.dto.CastDto
import data.remote.dto.CrewDto
import data.remote.dto.MovieDetailDto
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import presentation.components.CreditChip

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            // show button only if movie has loaded
            if (uiState.movie != null) {
                Button(
                    onClick = { /* TODO: Handle Booking */ },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Book Now", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center   // center the button
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            when {
                uiState.isLoading -> {
                    // show a loading indicator
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    // show error message
                    Text(
                        "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.movie != null -> {
                    // show main content
                    MovieDetailContent(
                        movie = uiState.movie!!,
                        cast = uiState.cast,
                        crew = uiState.crew,
                        onBackClick = onBackClick
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    movie: MovieDetailDto,
    cast: List<CastDto>,
    crew: List<CrewDto>,
    onBackClick: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Back Arrow and Title
        item {
            Box(contentAlignment = Alignment.TopStart) {
                // Movie Poster
                KamelImage(
                    resource = asyncPainterResource(data = "https://image.tmdb.org/t/p/w500${movie.posterPath}"),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )

                // Back button
                IconButton(onClick = onBackClick, modifier = Modifier.padding(8.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }

        // Movie Info Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                // movie details
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.tagline.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Storyline",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium
                )

                // cast and crew
                Spacer(modifier = Modifier.height(24.dp))
                Text("Cast", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(cast) { castMember ->
                        CreditChip(name = castMember.name, imageUrl = castMember.profilePath)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Crew", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(crew) { crewMember ->
                        CreditChip(
                            name = crewMember.name,
                            job = crewMember.job,
                            imageUrl = crewMember.profilePath
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}