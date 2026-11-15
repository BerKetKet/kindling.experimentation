package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.MarginLinesIcon
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSheetActionBlue
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

data class ThemePreset(
    val name: String,
    val fontSize: Int,
    val lineSpacingMultiplier: Float,
    val marginPreset: Int
)

@Composable
fun DisplaySettingsThemesTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    var selectedPreset by remember { mutableStateOf<String?>("Standard") }
    val isDark = isOpusDarkTheme
    val dividerColor = if (isDark) Color(0xFF5D5D61) else Color(0xFF909090)

    val presets = listOf(
        ThemePreset("Compact", 14, 1.2f, 0),
        ThemePreset("Standard", 18, 1.3f, 1),
        ThemePreset("Large", 22, 1.5f, 1),
        ThemePreset("Low Vision", 28, 1.8f, 2)
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Gap from tab bar divider (y=507) to Row 1 top (y=542): 35 px
        Spacer(modifier = Modifier.height(35.refPx))

        // Row 1: Custom (y=[542, 657], h=116 px, x=[73, 1007])
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(116.refPx)
                .padding(horizontal = 73.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(116.refPx)
                    .clip(RoundedCornerShape(4.dp))
                    .background(OpusSheetActionBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(64.refPx)
                )
            }
            Spacer(modifier = Modifier.width(36.refPx))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Custom",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
                Spacer(modifier = Modifier.height(8.refPx))
                Text(
                    text = "Save settings as new theme",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.sp,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black)
                    .border(2.refPx, Color.White, RoundedCornerShape(4.dp))
                    .clickable { selectedPreset = "Custom" }
                    .padding(horizontal = 28.refPx, vertical = 14.refPx),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = Color.White,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(34.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        presets.forEachIndexed { index, preset ->
            val isSelected = selectedPreset == preset.name
            Spacer(modifier = Modifier.height(34.refPx))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.refPx)
                    .clickable {
                        selectedPreset = preset.name
                        engine.updateSettings { current ->
                            current.copy(
                                fontSize = preset.fontSize,
                                lineSpacingMultiplier = preset.lineSpacingMultiplier,
                                marginPreset = preset.marginPreset
                            )
                        }
                    }
                    .padding(horizontal = 73.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(116.refPx)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black)
                        .border(
                            width = if (isSelected) 4.refPx else 2.refPx,
                            color = if (isSelected) OpusSheetActionBlue else Color.White,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (preset.name == "Low Vision") {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(56.refPx)
                        )
                    } else {
                        MarginLinesIcon(
                            preset = index.coerceIn(0, 2),
                            tint = Color.White,
                            modifier = Modifier.size(width = 68.refPx, height = 48.refPx)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(36.refPx))
                Text(
                    text = preset.name,
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            if (index < presets.lastIndex) {
                Spacer(modifier = Modifier.height(34.refPx))
                HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2C2C2E)
@Composable
fun DisplaySettingsThemesTabPreview() {
    OpusReaderTheme {
        DisplaySettingsThemesTab(engine = FakeReaderEngine())
    }
}
