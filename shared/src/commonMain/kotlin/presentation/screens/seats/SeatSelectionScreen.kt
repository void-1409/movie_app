package presentation.screens.seats

import androidx.compose.animation.core.ArcAnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.Seat
import domain.model.SeatStatus

@Composable
fun SeatSelectionScreen(
    viewModel: SeatSelectionViewModel,
    movieTitle: String,
    onBackClick: () -> Unit,
    onNavigateToConfirmation: (ticketId: String) -> Unit
) {
    // listen for navigation events from viewmodel
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { ticketId ->
            onNavigateToConfirmation(ticketId)
        }
    }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SeatSelectionTopBar(title = movieTitle, onBackClick = onBackClick) },
        bottomBar = {
            BottomBar(
                price = uiState.totalPrice,
                onBuyTicketsClick = { viewModel.onBuyTicketsClicked() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CinemaScreenView()
            Spacer(modifier = Modifier.height(24.dp))
            SeatLayout(
                seats = uiState.seats,
                onSeatClick = { viewModel.onSeatSelected(it) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            SeatLegend()
        }
    }
}

@Composable
private fun SeatSelectionTopBar(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
           Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CinemaScreenView() {
    Canvas(
        modifier = Modifier.fillMaxWidth().height(50.dp)
    ) {
        val path = Path().apply {
            moveTo(0f, size.height)
            quadraticTo(size.width / 2, -size.height / 2, size.width, size.height)
        }
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(Color.Blue.copy(alpha = 0.4f), Color.Transparent)
            )
        )
    }
}

@Composable
private fun SeatLayout(seats: List<Seat>, onSeatClick: (Seat) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(10),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(seats, key = { it.id }) { seat ->
            SeatView(seat = seat, onClick = { onSeatClick(seat) })
        }
    }
}

@Composable
private fun SeatView(seat: Seat, onClick: () -> Unit) {
    val backgroundColor = when (seat.status) {
        SeatStatus.AVAILABLE -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        SeatStatus.RESERVED -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        SeatStatus.SELECTED -> MaterialTheme.colorScheme.primary.copy()
    }
    val contentColor = when (seat.status) {
        SeatStatus.RESERVED -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        SeatStatus.SELECTED -> MaterialTheme.colorScheme.onPrimary.copy()
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            seat.id,
            fontSize = 10.sp,
            color = contentColor
        )
    }
}

@Composable
private fun SeatLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), text = "Available")
        LegendItem(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), text = "Reserved")
        LegendItem(color = MaterialTheme.colorScheme.primary.copy(), text = "Selected")
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(16.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, fontSize = 12.sp)
    }
}

@Composable
private fun BottomBar(price: Float, onBuyTicketsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                "Total",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "€${price.formatPrice()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Button(
            onClick = onBuyTicketsClick,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Buy Ticket(s)",
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

// simple function to format price to two decimal places
private fun Float.formatPrice(): String {
    val intPart = this.toInt()
    val decimalPart = ((this - intPart) * 100).toInt()
    val decimalString = decimalPart.toString().padStart(2, '0')
    return "$intPart.$decimalString"
}