package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusReaderTheme

data class FontOption(val name: String, val fontFamily: FontFamily)

@Composable
fun DisplaySettingsFontTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    val settings by engine.settings.collectAsState()
    val availableFonts = listOf(
        FontOption("Bookerly", BookerlyFontFamily),
        FontOption("Caecilia", FontFamily.Serif),
        FontOption("Droid Serif", FontFamily.Serif),
        FontOption("Georgia", FontFamily.Serif),
        FontOption("Amazon Ember", AmazonEmberFontFamily),
        FontOption("Baskerville", FontFamily.Serif),
        FontOption("Palatino", FontFamily.Serif)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = (19 / 2.625).dp)
    ) {
        // Font Carousel (y=1460..1618)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 21.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(availableFonts) { fontOpt ->
                val isSelected = settings.fontFamily == fontOpt.name
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(94.dp)
                        .clickable { engine.updateSettings { it.copy(fontFamily = fontOpt.name) } }
                ) {
                    // Guidance text "Aa"
                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((115 / 2.625).dp)
                    ) {
                        Text(
                            text = "Aa",
                            color = if (isSelected) Color(0xFFA3D7F5) else Color.White,
                            fontFamily = fontOpt.fontFamily,
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Normal,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }

                    Spacer(modifier = Modifier.height((29 / 2.625).dp))

                    // Font Name
                    Text(
                        text = fontOpt.name,
                        color = if (isSelected) Color(0xFFA3D7F5) else Color.White,
                        fontFamily = AmazonEmberFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        minLines = 2,
                        modifier = Modifier.height((37 / 2.625).dp),
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height((80 / 2.625).dp))

        // Divider 1 (y=1691..1692)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height((2 / 2.625).dp)
                .background(Color(0xFF48484A))
        )

        Spacer(modifier = Modifier.height((56 / 2.625).dp))

        // Font Size Slider (y=1749..1828)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height((80 / 2.625).dp)
                .padding(horizontal = 21.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "A",
                color = Color(0xFF8E8E93),
                fontFamily = AmazonEmberFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Slider(
                value = settings.fontSize.toFloat(),
                onValueChange = { newSize ->
                    engine.updateSettings { it.copy(fontSize = newSize.toInt()) }
                },
                valueRange = 1f..14f,
                steps = 12,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF2491FF),
                    activeTrackColor = Color(0xFF2491FF),
                    inactiveTrackColor = Color(0xFF5D5D61)
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            Text(
                text = "A",
                color = Color(0xFF8E8E93),
                fontFamily = AmazonEmberFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }

        Spacer(modifier = Modifier.height((83 / 2.625).dp))

        // Divider 2 (y=1912..1913)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height((2 / 2.625).dp)
                .background(Color(0xFF48484A))
        )

        Spacer(modifier = Modifier.height((39 / 2.625).dp))

        // Spacing disclosure row (y=1963..2059)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .clickable { }
        ) {
            Text(
                text = "Spacing",
                color = Color.White,
                fontFamily = AmazonEmberFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Spacer(modifier = Modifier.height((15 / 2.625).dp))
            Text(
                text = "Adjust text spacing of the page.",
                color = Color(0xFFA8ABAD),
                fontFamily = AmazonEmberFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
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
