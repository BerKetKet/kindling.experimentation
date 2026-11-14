package app.opusreader.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Reference scale for 1080x2340 @ 420 dpi (density = 2.625)
 * All sizes in the specification and reference images are measured in pixels (px)
 * and converted to dp as: dp = px / 2.625f
 */
const val REF_DENSITY = 2.625f

inline val Int.refPx: Dp get() = (this / REF_DENSITY).dp
inline val Float.refPx: Dp get() = (this / REF_DENSITY).dp
inline val Double.refPx: Dp get() = (this.toFloat() / REF_DENSITY).dp

inline val Int.refSp: TextUnit get() = (this / REF_DENSITY).sp
inline val Float.refSp: TextUnit get() = (this / REF_DENSITY).sp
