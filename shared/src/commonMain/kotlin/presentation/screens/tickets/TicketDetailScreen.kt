package presentation.screens.tickets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.Ticket
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.LocalDate
import presentation.components.TicketShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    ticket: Ticket,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ticket.movieTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        contentColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            TicketContent(ticket)
        }
    }
}

@Composable
fun TicketContent(ticket: Ticket) {
    // split point for the notch (approx 70% down)
    val notchOffsetY = 420.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = TicketShape(cornerRadius = 16.dp, notchRadius = 12.dp, notchYAxis = notchOffsetY),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // top section: movie info
            Row(modifier = Modifier.fillMaxWidth()) {
                KamelImage(
                    resource = asyncPainterResource(data = "https://image.tmdb.org/t/p/w500${ticket.posterUrl}"),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = ticket.movieTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${ticket.duration} • ${ticket.genre}", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // location row
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(ticket.cinemaName, fontWeight = FontWeight.SemiBold, color = Color.Black)
                            Text(ticket.location, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // middle section: info grid
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoItem(
                    icon = Icons.Default.CalendarToday,
                    label = "Date",
                    value = ticket.date.formatToString(),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(24.dp))
                InfoItem(
                    icon = Icons.Default.AccessTime,
                    label = "Time",
                    value = ticket.time,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoItem(
                    icon = Icons.Default.EventSeat,
                    label = "Seats",
                    value = ticket.selectedSeats.joinToString(", ") { "${it.row}${it.number}" },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(24.dp))
                InfoItem(
                    icon = null,
                    label = "Price",
                    value = "€${ticket.totalPrice}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // dashed line separator
            DashedLine()

            Spacer(modifier = Modifier.height(24.dp))

            // bottom section: barcode
            Text("Show this code at entrance", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Fake barcode generator
            BarcodeView(modifier = Modifier.fillMaxWidth().height(60.dp))

            Spacer(modifier = Modifier.height(4.dp))
            Text("Booking Number: ${ticket.bookingNumber}", style = MaterialTheme.typography.labelMedium, color = Color.Black)
        }
    }
}

// helper function for date formatting
private fun LocalDate.formatToString(): String {
    val day = this.dayOfMonth.toString().padStart(2, '0')
    val month = this.monthNumber.toString().padStart(2, '0')
    val year = this.year.toString()
    return "$day.$month.$year"
}

@Composable
fun InfoItem(icon: ImageVector?, label: String, value: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon, null, tint = Color.Black, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(value, fontWeight = FontWeight.Bold, color = Color.Black, maxLines = 3, lineHeight = 18.sp)
        }
    }
}

@Composable
fun DashedLine() {
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = Color.LightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
            strokeWidth = 2f
        )
    }
}

@Composable
fun BarcodeView(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val barWidth = size.width / 100
        var currentX = 0f
        repeat(100) {
            val isBlack = kotlin.random.Random.nextBoolean()
            if (isBlack) {
                val thickness = if (kotlin.random.Random.nextBoolean()) barWidth * 2 else barWidth / 2
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(currentX, 0f),
                    size = Size(thickness, size.height)
                )
            }
            currentX += barWidth
        }
    }
}