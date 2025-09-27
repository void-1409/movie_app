package presentation.screens.seats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import kotlinx.datetime.LocalDate

@Composable
fun SeatSelectionScreen(
    movieTitle: String,
    cinema: String,
    date: LocalDate,
    time: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Seat Selection for:")
        Text("Movie: $movieTitle")
        Text("Cinema: $cinema")
        Text("Date: $date")
        Text("Time: $time")
    }
}