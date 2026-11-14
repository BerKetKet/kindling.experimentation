package app.opusreader.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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

private val OpusLightColorScheme = lightColorScheme(
    primary = OpusLightTextPrimary,
    onPrimary = OpusLightBackground,
    primaryContainer = OpusLightSurface,
    onPrimaryContainer = OpusLightTextPrimary,
    secondary = OpusLightTextSecondary,
    onSecondary = OpusLightBackground,
    surface = OpusLightSurface,
    onSurface = OpusLightTextPrimary,
    background = OpusLightBackground,
    onBackground = OpusLightTextPrimary,
    outline = OpusLightDivider,
    outlineVariant = OpusLightDivider
)

@Composable
fun OpusReaderTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) OpusDarkColorScheme else OpusLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = (if (darkTheme) OpusTopBar else OpusLightTopBar).toArgb()
            window.navigationBarColor = (if (darkTheme) OpusBottomBar else OpusLightBottomBar).toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OpusTypography,
        content = content
    )
}
