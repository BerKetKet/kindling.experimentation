package app.opusreader.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.opusreader.R

// Kindle Serif: Bookerly family (used for screen titles: Library, Home, Settings, Annotations, etc.)
val BookerlyFontFamily = FontFamily(
    Font(R.font.bookerly_regular, FontWeight.Normal),
    Font(R.font.bookerly_bold, FontWeight.Bold),
    Font(R.font.bookerly_bold, FontWeight.SemiBold),
    Font(R.font.bookerly_display, FontWeight.Medium)
)

// Kindle UI Sans: Amazon Ember family (used for all body text, row labels, search text, etc.)
val AmazonEmberFontFamily = FontFamily(
    Font(R.font.amazon_ember_light, FontWeight.Light),
    Font(R.font.amazon_ember_regular, FontWeight.Normal),
    Font(R.font.amazon_ember_medium, FontWeight.Medium),
    Font(R.font.amazon_ember_medium, FontWeight.SemiBold),
    Font(R.font.amazon_ember_medium, FontWeight.Bold)
)

val NoPaddingPlatformStyle = PlatformTextStyle(includeFontPadding = false)

val OpusTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = BookerlyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    headlineMedium = TextStyle(
        fontFamily = BookerlyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    titleLarge = TextStyle(
        fontFamily = BookerlyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    titleMedium = TextStyle(
        fontFamily = AmazonEmberFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    titleSmall = TextStyle(
        fontFamily = AmazonEmberFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.5.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    bodyLarge = TextStyle(
        fontFamily = AmazonEmberFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    bodyMedium = TextStyle(
        fontFamily = AmazonEmberFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    bodySmall = TextStyle(
        fontFamily = AmazonEmberFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    labelLarge = TextStyle(
        fontFamily = AmazonEmberFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.5.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    labelMedium = TextStyle(
        fontFamily = AmazonEmberFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.5.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    ),
    labelSmall = TextStyle(
        fontFamily = AmazonEmberFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp,
        platformStyle = NoPaddingPlatformStyle
    )
)
