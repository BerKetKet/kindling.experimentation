package app.opusreader.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.opusreader.ui.theme.OpusTextPrimary

/**
 * Kindle Filter Sliders icon: two horizontal lines with circular slider thumbs.
 */
@Composable
fun FilterSlidersIcon(
    tint: Color = OpusTextPrimary,
    modifier: Modifier = Modifier.size(24.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 2.dp.toPx()
        val r = 3.2.dp.toPx()

        val y1 = h * 0.33f
        val y2 = h * 0.67f
        val cx1 = w * 0.66f
        val cx2 = w * 0.34f

        // Top track (split around circle)
        drawLine(tint, Offset(w * 0.12f, y1), Offset(cx1 - r, y1), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(tint, Offset(cx1 + r, y1), Offset(w * 0.88f, y1), strokeWidth = stroke, cap = StrokeCap.Round)
        drawCircle(tint, radius = r, center = Offset(cx1, y1), style = Stroke(width = stroke))

        // Bottom track (split around circle)
        drawLine(tint, Offset(w * 0.12f, y2), Offset(cx2 - r, y2), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(tint, Offset(cx2 + r, y2), Offset(w * 0.88f, y2), strokeWidth = stroke, cap = StrokeCap.Round)
        drawCircle(tint, radius = r, center = Offset(cx2, y2), style = Stroke(width = stroke))
    }
}

/**
 * Kindle Sort Up/Down Arrows icon (↑↓).
 */
@Composable
fun SortArrowsIcon(
    tint: Color = OpusTextPrimary,
    modifier: Modifier = Modifier.size(24.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 2.dp.toPx()

        val xLeft = w * 0.33f
        val xRight = w * 0.67f
        val yTop = h * 0.18f
        val yBot = h * 0.82f
        val head = w * 0.14f

        // Left Up Arrow
        drawLine(tint, Offset(xLeft, yBot), Offset(xLeft, yTop), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(tint, Offset(xLeft - head, yTop + head), Offset(xLeft, yTop), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(tint, Offset(xLeft + head, yTop + head), Offset(xLeft, yTop), strokeWidth = stroke, cap = StrokeCap.Round)

        // Right Down Arrow
        drawLine(tint, Offset(xRight, yTop), Offset(xRight, yBot), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(tint, Offset(xRight - head, yBot - head), Offset(xRight, yBot), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(tint, Offset(xRight + head, yBot - head), Offset(xRight, yBot), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

/**
 * Kindle Bottom Nav Library icon: two vertical book spines + one leaning spine (||/).
 */
@Composable
fun KindleLibraryIcon(
    tint: Color,
    modifier: Modifier = Modifier.size(24.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()

        // Spine 1 (left upright rectangle)
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.16f, h * 0.18f),
            size = Size(w * 0.16f, h * 0.66f),
            cornerRadius = CornerRadius(2f, 2f),
            style = Stroke(width = stroke)
        )
        // Spine 2 (middle upright rectangle)
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.38f, h * 0.18f),
            size = Size(w * 0.16f, h * 0.66f),
            cornerRadius = CornerRadius(2f, 2f),
            style = Stroke(width = stroke)
        )
        // Spine 3 (leaning book on right)
        val path = Path().apply {
            moveTo(w * 0.62f, h * 0.22f)
            lineTo(w * 0.77f, h * 0.18f)
            lineTo(w * 0.90f, h * 0.80f)
            lineTo(w * 0.75f, h * 0.84f)
            close()
        }
        drawPath(path, color = tint, style = Stroke(width = stroke, join = StrokeJoin.Round))
    }
}

/**
 * Kindle Bottom Nav More icon: three bulleted horizontal lines.
 */
@Composable
fun KindleMoreListIcon(
    tint: Color,
    modifier: Modifier = Modifier.size(24.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 2.dp.toPx()
        val r = 1.6.dp.toPx()

        for (frac in listOf(0.26f, 0.50f, 0.74f)) {
            val y = h * frac
            drawCircle(tint, radius = r, center = Offset(w * 0.20f, y))
            drawLine(tint, Offset(w * 0.36f, y), Offset(w * 0.84f, y), strokeWidth = stroke, cap = StrokeCap.Round)
        }
    }
}

/**
 * Kindle Bound Notebook icon (used in Reader top chrome and More -> Notebooks).
 */
@Composable
fun KindleNotebookIcon(
    tint: Color = OpusTextPrimary,
    modifier: Modifier = Modifier.size(22.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()

        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.20f, h * 0.14f),
            size = Size(w * 0.60f, h * 0.72f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = stroke)
        )
        // Spine vertical line
        drawLine(
            color = tint,
            start = Offset(w * 0.36f, h * 0.14f),
            end = Offset(w * 0.36f, h * 0.86f),
            strokeWidth = stroke
        )
    }
}

/**
 * Kindle Share Box-Arrow icon (upward arrow out of open tray).
 */
@Composable
fun KindleShareBoxIcon(
    tint: Color = OpusTextPrimary,
    modifier: Modifier = Modifier.size(22.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()

        // Tray
        val tray = Path().apply {
            moveTo(w * 0.32f, h * 0.38f)
            lineTo(w * 0.20f, h * 0.38f)
            lineTo(w * 0.20f, h * 0.84f)
            lineTo(w * 0.80f, h * 0.84f)
            lineTo(w * 0.80f, h * 0.38f)
            lineTo(w * 0.68f, h * 0.38f)
        }
        drawPath(tray, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Up arrow
        val cx = w * 0.50f
        drawLine(tint, Offset(cx, h * 0.64f), Offset(cx, h * 0.14f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(tint, Offset(cx - w * 0.14f, h * 0.28f), Offset(cx, h * 0.14f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(tint, Offset(cx + w * 0.14f, h * 0.28f), Offset(cx, h * 0.14f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

/**
 * Kindle Margins preset icon (0 = narrow, 1 = medium, 2 = wide).
 */
@Composable
fun MarginLinesIcon(
    preset: Int,
    tint: Color = OpusTextPrimary,
    modifier: Modifier = Modifier.size(width = 36.dp, height = 22.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        val inset = when (preset) {
            0 -> w * 0.12f
            1 -> w * 0.24f
            else -> w * 0.34f
        }
        for (frac in listOf(0.22f, 0.48f, 0.74f)) {
            val y = h * frac
            drawLine(tint, Offset(inset, y), Offset(w - inset, y), strokeWidth = stroke, cap = StrokeCap.Round)
        }
    }
}

/**
 * Kindle Alignment icon (justified = 4 equal lines; left = alternating ragged lines).
 */
@Composable
fun AlignmentLinesIcon(
    justified: Boolean,
    tint: Color = OpusTextPrimary,
    modifier: Modifier = Modifier.size(width = 36.dp, height = 22.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        val xStart = w * 0.18f
        val endFracs = if (justified) listOf(0.82f, 0.82f, 0.82f, 0.82f) else listOf(0.82f, 0.66f, 0.82f, 0.60f)
        val yFracs = listOf(0.18f, 0.40f, 0.62f, 0.84f)
        for (i in 0..3) {
            val y = h * yFracs[i]
            drawLine(tint, Offset(xStart, y), Offset(w * endFracs[i], y), strokeWidth = stroke, cap = StrokeCap.Round)
        }
    }
}
