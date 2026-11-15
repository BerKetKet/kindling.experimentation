package app.opusreader.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val OpusDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF101112),
    primaryContainer = Color(0xFF1C1D1E),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFFA8ABAD),
    onSecondary = Color(0xFF101112),
    surface = Color(0xFF232323),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF1C1D1E),
    onSurfaceVariant = Color(0xFFA8ABAD),
    background = Color(0xFF101112),
    onBackground = Color(0xFFFFFFFF),
    outline = Color(0xFF404040),
    outlineVariant = Color(0xFF3B3C3D)
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
    navigationBarColor: Color? = null,
    statusBarColor: Color? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) OpusDarkColorScheme else OpusLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val topCol = statusBarColor ?: if (darkTheme) Color(0xFF101112) else OpusLightTopBar
            val botCol = navigationBarColor ?: if (darkTheme) Color(0xFF141414) else OpusLightBottomBar
            window.statusBarColor = topCol.toArgb()
            window.navigationBarColor = botCol.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = OpusTypography,
            content = content
        )
    }
}
