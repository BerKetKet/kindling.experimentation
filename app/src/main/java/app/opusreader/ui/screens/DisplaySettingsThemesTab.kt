package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

data class ThemePreset(
    val name: String,
    val description: String,
    val fontSize: Int,
    val lineSpacingMultiplier: Float,
    val marginPreset: Int
)

@Composable
fun DisplaySettingsThemesTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    val settings by engine.settings.collectAsState()
    var selectedPreset by remember { mutableStateOf<String?>("Standard") }

    val presets = listOf(
        ThemePreset("Compact", "Smaller text, tight spacing", 14, 1.2f, 0),
        ThemePreset("Standard", "Balanced default reading layout", 18, 1.3f, 1),
        ThemePreset("Large", "Enlarged text and generous line height", 22, 1.5f, 1),
        ThemePreset("Low Vision", "High legibility, maximum font size and margins", 28, 1.8f, 2)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = OpusSpacing.medium)
    ) {
        // Custom Theme Section
        Text(
            text = "Custom",
            color = OpusTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Save custom theme logic
                }
                .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(OpusSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = OpusAccentBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.size(OpusSpacing.medium))
            Text(
                text = "Save settings as new theme",
                color = OpusTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }

        HorizontalDivider(
            color = OpusDivider,
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small)
        )

        // Presets list
        presets.forEach { preset ->
            val isSelected = selectedPreset == preset.name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                    .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = preset.name,
                        color = OpusTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = preset.description,
                        color = OpusTextSecondary,
                        fontSize = 13.sp
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = OpusAccentBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun DisplaySettingsThemesTabPreview() {
    OpusReaderTheme {
        DisplaySettingsThemesTab(engine = FakeReaderEngine())
    }
}
