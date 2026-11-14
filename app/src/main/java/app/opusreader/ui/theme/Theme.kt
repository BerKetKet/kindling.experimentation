package app.opusreader.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val OpusDarkColorScheme = darkColorScheme(
    primary = OpusTextPrimary,
    onPrimary = OpusBackground,
    primaryContainer = OpusSurfaceVariant,
    onPrimaryContainer = OpusTextPrimary,
    secondary = OpusTextSecondary,
    onSecondary = OpusBackground,
    surface = OpusSurface,
    onSurface = OpusTextPrimary,
    surfaceVariant = OpusSurfaceVariant,
    onSurfaceVariant = OpusTextSecondary,
    background = OpusBackground,
    onBackground = OpusTextPrimary,
    outline = OpusBorder,
    outlineVariant = OpusDivider
)

@Composable
fun OpusReaderTheme(
    darkTheme: Boolean = true, // Dark theme first, matching Kindle Android dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = OpusDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = OpusTopBar.toArgb()
            window.navigationBarColor = OpusBottomBar.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OpusTypography,
        content = content
    )
}
