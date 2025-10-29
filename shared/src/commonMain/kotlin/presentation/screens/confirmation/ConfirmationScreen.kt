package presentation.screens.confirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationScreen(
    ticketId: String,
    onViewBookingClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // celebration animation here later
        Text("Booking Confirmed!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Your ticket ID is $ticketId")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onViewBookingClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View My Bookings", modifier = Modifier.padding(8.dp))
        }
    }
}