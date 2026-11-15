package app.opusreader.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalIsDarkTheme = staticCompositionLocalOf { true }

val isOpusDarkTheme: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalIsDarkTheme.current

// Dark & Light Adaptive Tokens (sampled from 1080x2340 @ 2.625 density references)
val OpusBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF101112) else Color(0xFFFFFFFF)

val OpusHomeBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF141414) else Color(0xFFFFFFFF)

val OpusSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF232323) else Color(0xFFF7F7F7)

val OpusSurfaceVariant: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF1C1D1E) else Color(0xFFEEEEEE)

val OpusElevatedCard: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF18191B) else Color(0xFFF5F5F5)

val OpusBottomBar: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF141414) else Color(0xFFF2F2F2)

val OpusTopBar: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF101112) else Color(0xFFFFFFFF)

// Sheet surfaces
val OpusContentsSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF232323) else Color(0xFFFFFFFF)

val OpusDisplaySettingsSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF2C2C2E) else Color(0xFFF5F5F5)

val OpusDisplaySettingsFooterSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF3A3A3C) else Color(0xFFF0F0F0)

val OpusAnnotationSheetSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF1A1B1C) else Color(0xFFFFFFFF)

val OpusScrim = Color(0x99000000)

val OpusDragHandle: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF8E8E93) else Color(0xFF9D9D9D)

// Typography colors
val OpusTextPrimary: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFFFFFFFF) else Color(0xFF1A1A1A)

val OpusTextSecondary: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFFA8ABAD) else Color(0xFF5C5D5E)

val OpusTextTertiary: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF8E9194) else Color(0xFF747678)

// Dividers & borders
val OpusDivider: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF3B3C3D) else Color(0xFFE0E0E0)

val OpusBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF404040) else Color(0xFFE0E0E0)

val OpusSearchBarBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF8E9194) else Color(0xFF747678)

// Brand and Accent colors
val OpusAccentBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFF2491FF) else Color(0xFF0274E6)

val OpusSheetActionBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = if (LocalIsDarkTheme.current) Color(0xFFA3D7F5) else Color(0xFF1E78BA)

val OpusHighlightBlue = Color(0xFF0274E6)
val OpusAccentGold = Color(0xFFD29648)
val OpusBookmarkRed = Color(0xFFD8232A)

// Reader Page background colors (exact Kindle photometric hexes)
val PageColorWhite = Color(0xFFFFFFFF)
val PageColorSepia = Color(0xFFFBF0D9)
val PageColorGreen = Color(0xFFC5E7CE)
val PageColorBlack = Color(0xFF000000)

// Reader Chrome colors for light/tinted themes (White, Sepia, Green) vs Black
val ReaderLightChromeBg = Color(0xFFFFFFFF)
val ReaderLightChromeSubBg = Color(0xFFEEEEEE)
val ReaderLightChromeText = Color(0xFF000000)
val ReaderLightChromeSecondary = Color(0xFF5C5D5E)
val ReaderDarkChromeBg = Color(0xFF232323)
val ReaderDarkChromeSubBg = Color(0xFF141414)

// Explicit Light Theme constants
val OpusLightBackground = Color(0xFFFFFFFF)
val OpusLightSurface = Color(0xFFF7F7F7)
val OpusLightTopBar = Color(0xFFFFFFFF)
val OpusLightBottomBar = Color(0xFFF2F2F2)
val OpusLightTextPrimary = Color(0xFF1A1A1A)
val OpusLightTextSecondary = Color(0xFF5C5D5E)
val OpusLightDivider = Color(0xFFE0E0E0)

// Annotation highlight colors
val HighlightDefault = Color(0xFFFFFFFF)
val HighlightGray = Color(0xFF9E9E9E)
val HighlightRed = Color(0xFFEF5350)
val HighlightOrange = Color(0xFFFFA726)
val HighlightYellow = Color(0xFFF9E054)
val HighlightGreen = Color(0xFF66BB6A)
val HighlightAqua = Color(0xFF26C6DA)
val HighlightBlue = Color(0xFF42A5F5)
val HighlightPurple = Color(0xFFAB47BC)
val HighlightPink = Color(0xFFF48FB1)
