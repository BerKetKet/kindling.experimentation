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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun DisplaySettingsFontTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    val settings by engine.settings.collectAsState()
    val availableFonts = listOf("Bookerly", "Caecilia", "Georgia", "Amazon Ember", "Droid Serif")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = OpusSpacing.medium)
    ) {
        // Font family picker
        Text(
            text = "Font",
            color = OpusTextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.xsmall)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = OpusSpacing.standard),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.small)
        ) {
            items(availableFonts) { font ->
                val isSelected = settings.fontFamily == font
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) OpusTextPrimary else OpusSurfaceVariant)
                        .clickable { engine.updateSettings { it.copy(fontFamily = font) } }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = font,
                        color = if (isSelected) Color(0xFF101112) else OpusTextPrimary,
                        fontSize = 14.sp,
                        fontFamily = if (font in listOf("Bookerly", "Caecilia", "Georgia")) FontFamily.Serif else FontFamily.SansSerif,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.large))

        // Size slider
        Text(
            text = "Size",
            color = OpusTextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.xsmall)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "A",
                color = OpusTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Slider(
                value = settings.fontSize.toFloat(),
                onValueChange = { newSize ->
                    engine.updateSettings { it.copy(fontSize = newSize.toInt()) }
                },
                valueRange = 12f..32f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = OpusAccentBlue,
                    inactiveTrackColor = Color(0xFF404040)
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = OpusSpacing.medium)
            )

            Text(
                text = "A",
                color = OpusTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(name = "17 Display Settings Font Tab Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun DisplaySettingsFontTabPreview() {
    OpusReaderTheme {
        DisplaySettingsFontTab(engine = FakeReaderEngine())
    }
}
