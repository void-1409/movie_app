package presentation.screens.confirmation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import presentation.theme.LocalStrings
import kotlin.random.Random

@Composable
fun ConfirmationScreen(
    ticketId: String,
    onViewBookingClick: () -> Unit
) {
    val strings = LocalStrings.current
    Box(modifier = Modifier.fillMaxSize()) {
        // animation (background)
        ConfettiEffect()

        // content (foreground)
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(strings.bookingConfirmed, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(strings.yourTicketId)
            Text(ticketId, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onViewBookingClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(strings.viewBooking, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun ConfettiEffect() {
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = LinearEasing
            )
        )
    }

    val confettis = remember {
        List(50) {
            Confetti(
                color = Color(listOf(0xFFE3A008, 0xFF4CAF50, 0xFF2196F3, 0xFFE91E63).random()),
                startX = (0..1000).random().toFloat(),
                startY = (-1000..0).random().toFloat(),
                speed = Random.nextDouble(0.8, 1.5).toFloat()
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val progress = animatable.value

        confettis.forEach { confetti ->

            val yPosition = confetti.startY + ((size.height * 2.5f) * progress * confetti.speed)

            if (yPosition < size.height + 50) {
                drawCircle(
                    color = confetti.color,
                    radius = 8f,
                    center = Offset(x = confetti.startX % size.width, y = yPosition)
                )
            }
        }
    }
}

data class Confetti(
    val color: Color,
    val startX: Float,
    val startY: Float,
    val speed: Float
)