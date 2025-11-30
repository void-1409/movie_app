package presentation.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class TicketShape(
    private val cornerRadius: Dp,
    private val notchRadius: Dp,
    private val notchYAxis: Dp
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val notchRadiusPx = with(density) { notchRadius.toPx() }
        val notchYAxisPx = with(density) { notchYAxis.toPx() }

        // main rounded rectangle
        val roundedRect = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, size.width, size.height),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )
            )
        }

        // left notch
        val leftNotch = Path().apply {
            addOval(
                Rect(
                    left = -notchRadiusPx,
                    top = notchYAxisPx - notchRadiusPx,
                    right = notchRadiusPx,
                    bottom = notchYAxisPx + notchRadiusPx
                )
            )
        }

        // right notch
        val rightNotch = Path().apply {
            addOval(
                Rect(
                    left = size.width - notchRadiusPx,
                    top = notchYAxisPx - notchRadiusPx,
                    right = size.width + notchRadiusPx,
                    bottom = notchYAxisPx + notchRadiusPx
                )
            )
        }

        val path = Path.combine(PathOperation.Difference, roundedRect, leftNotch)
        return Outline.Generic(Path.combine(PathOperation.Difference, path, rightNotch))
    }
}