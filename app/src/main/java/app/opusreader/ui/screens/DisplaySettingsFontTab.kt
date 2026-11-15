package app.opusreader.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSheetActionBlue
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

@Composable
fun DisplaySettingsFontTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    val settings by engine.settings.collectAsState()
    val availableFonts = listOf("Bookerly", "Caecilia", "Droid Serif", "Georgia", "Amazon Ember")
    val isDark = isOpusDarkTheme
    val dividerColor = if (isDark) Color(0xFF8E8E93) else Color(0xFF222222)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Gap from tab divider (y=1410) to Aa glyphs top (y=1460): 50 px
        Spacer(modifier = Modifier.height(50.refPx))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 72.refPx),
            horizontalArrangement = Arrangement.spacedBy(84.refPx)
        ) {
            items(availableFonts) { font ->
                val isSelected = settings.fontFamily == font
                val itemColor = if (isSelected) OpusSheetActionBlue else OpusTextPrimary
                val previewFamily = when (font) {
                    "Bookerly" -> BookerlyFontFamily
                    "Caecilia", "Droid Serif", "Georgia" -> FontFamily.Serif
                    else -> AmazonEmberFontFamily
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { engine.updateSettings { it.copy(fontFamily = font) } }
                ) {
                    // Aa glyph band: y=[1460, 1547], h=88 px
                    Text(
                        text = "Aa",
                        style = TextStyle(
                            fontFamily = previewFamily,
                            color = itemColor,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Normal,
                            platformStyle = NoPaddingPlatformStyle
                        )
                    )
                    // Gap from Aa bottom (1547) to font name top (1582): 35 px
                    Spacer(modifier = Modifier.height(35.refPx))
                    // Font label band: y=[1582, 1618], h=37 px
                    Text(
                        text = font,
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = itemColor,
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            platformStyle = NoPaddingPlatformStyle
                        )
                    )
                }
            }
        }

        // Gap from font label bottom (1618) to divider 1 (y=1691): 73 px
        Spacer(modifier = Modifier.height(73.refPx))
        HorizontalDivider(
            color = dividerColor,
            thickness = 2.refPx,
            modifier = Modifier.padding(horizontal = 55.refPx)
        )

        // Gap from divider 1 (1692) to size slider row top (1749): 57 px
        Spacer(modifier = Modifier.height(57.refPx))

        // Size slider row: y=[1749, 1828], h=80 px
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.refPx)
                .padding(horizontal = 86.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "A",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 14.sp,
                    platformStyle = NoPaddingPlatformStyle
                )
            )

            Slider(
                value = settings.fontSize.toFloat(),
                onValueChange = { newSize ->
                    engine.updateSettings { it.copy(fontSize = newSize.toInt()) }
                },
                valueRange = 12f..32f,
                colors = SliderDefaults.colors(
                    thumbColor = OpusAccentBlue,
                    activeTrackColor = OpusAccentBlue,
                    inactiveTrackColor = Color(0xFF6E7073)
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 42.refPx)
            )

            Text(
                text = "A",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 24.sp,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
        }

        // Gap from slider bottom (1828) to divider 2 (y=1912): 84 px
        Spacer(modifier = Modifier.height(84.refPx))
        HorizontalDivider(
            color = dividerColor,
            thickness = 2.refPx,
            modifier = Modifier.padding(horizontal = 55.refPx)
        )

        // Gap from divider 2 (1913) to Spacing title top (1963): 50 px
        Spacer(modifier = Modifier.height(50.refPx))

        // Spacing row: title y=[1963, 2002], subtitle y=[2025, 2059]
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val nextSpacing = when {
                        settings.lineSpacingMultiplier < 1.25f -> 1.35f
                        settings.lineSpacingMultiplier < 1.45f -> 1.6f
                        else -> 1.15f
                    }
                    engine.updateSettings { it.copy(lineSpacingMultiplier = nextSpacing) }
                }
                .padding(horizontal = 56.refPx)
        ) {
            Text(
                text = "Spacing",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
            Spacer(modifier = Modifier.height(23.refPx))
            Text(
                text = "Adjust text spacing of the page.",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextSecondary,
                    fontSize = 12.5.sp,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
        }
    }
}

@Preview(name = "17 Display Settings Font Tab Preview", showBackground = true, backgroundColor = 0xFF2C2C2E)
@Composable
fun DisplaySettingsFontTabPreview() {
    OpusReaderTheme {
        DisplaySettingsFontTab(engine = FakeReaderEngine())
    }
}
