package presentation.screens.shows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate

@Composable
fun ShowsScreen(
    viewModel: ShowsViewModel,
    onBackClick: () -> Unit,
    onShowtimeClick: (cinemaName: String, time: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // top bar with back arrow and title
        ShowsTopBar(
            title = uiState.movieTitle,
            onBackClick = onBackClick
        )

        // main content area
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Date Selector
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.dates) { date ->
                        DateChip(
                            date = date,
                            isSelected = date == uiState.selectedDate,
                            onClick = { viewModel.onDateSelected(date) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Cinema and ShowTimes
            items(uiState.showtimes) { cinemaShowtimes ->
                Column {
                    Text(
                        text = cinemaShowtimes.cinemaName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // FlowRow to arrange items horizontally and wraps to next line if needed
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        cinemaShowtimes.showtimes.forEach { time ->
                            ShowTimeChip(
                                time = time,
                                onClick = { onShowtimeClick(cinemaShowtimes.cinemaName, time) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ShowsTopBar(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DateChip(date: LocalDate, isSelected: Boolean, onClick: () -> Unit) {
    val dayOfWeek = date.dayOfWeek.name.take(3).replaceFirstChar { it.uppercase() }
    val month = date.month.name.take(3).replaceFirstChar { it.uppercase() }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dayOfWeek,
                fontSize = 12.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = month,
                fontSize = 12.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}

@Composable
private fun ShowTimeChip(time: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(time)
    }
}