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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.OpusBottomSheetContainer
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun DisplaySettingsSheet(
    engine: ReaderEngine,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialTab: Int = 0
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val tabs = listOf("Font", "Layout", "Themes", "More")
    val settings by engine.settings.collectAsState()
    var brightness by remember { mutableStateOf(settings.brightness) }
    var autoBrightness by remember { mutableStateOf(settings.autoBrightness) }

    OpusBottomSheetContainer(
        title = "Display Settings",
        doneButtonText = "DONE",
        onDoneClick = onDismiss,
        onCloseClick = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = OpusSpacing.medium)
        ) {
            // Tabs Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    val isSelected = selectedTab == index
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { selectedTab = index }
                            .padding(horizontal = OpusSpacing.medium, vertical = OpusSpacing.small)
                    ) {
                        Text(
                            text = tabTitle,
                            color = if (isSelected) OpusAccentBlue else OpusTextSecondary,
                            fontSize = 16.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .width(32.dp)
                                .background(if (isSelected) OpusAccentBlue else Color.Transparent)
                        )
                    }
                }
            }

            HorizontalDivider(color = OpusDivider)

            // Tab Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    0 -> DisplaySettingsFontTab(engine = engine)
                    1 -> DisplaySettingsLayoutTab(engine = engine)
                    2 -> DisplaySettingsThemesTab(engine = engine)
                    3 -> DisplaySettingsMoreTab(engine = engine)
                }
            }

            HorizontalDivider(color = OpusDivider)

            // Common Brightness Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.BrightnessLow,
                    contentDescription = "Low Brightness",
                    tint = OpusTextSecondary,
                    modifier = Modifier.size(20.dp)
                )

                Slider(
                    value = brightness,
                    onValueChange = {
                        brightness = it
                        engine.updateSettings { current -> current.copy(brightness = it) }
                    },
                    valueRange = 0f..1f,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = OpusSpacing.medium),
                    colors = SliderDefaults.colors(
                        thumbColor = OpusAccentBlue,
                        activeTrackColor = OpusAccentBlue,
                        inactiveTrackColor = OpusSurfaceVariant
                    )
                )

                Icon(
                    imageVector = Icons.Default.BrightnessMedium,
                    contentDescription = "High Brightness",
                    tint = OpusTextSecondary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(OpusSpacing.medium))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        val newValue = !autoBrightness
                        autoBrightness = newValue
                        engine.updateSettings { current -> current.copy(autoBrightness = newValue) }
                    }
                ) {
                    Text(
                        text = "Auto",
                        color = OpusTextPrimary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(OpusSpacing.xsmall))
                    Switch(
                        checked = autoBrightness,
                        onCheckedChange = {
                            autoBrightness = it
                            engine.updateSettings { current -> current.copy(autoBrightness = it) }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = OpusAccentBlue,
                            uncheckedThumbColor = OpusTextSecondary,
                            uncheckedTrackColor = OpusSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun DisplaySettingsSheetPreview() {
    OpusReaderTheme {
        DisplaySettingsSheet(
            engine = FakeReaderEngine(),
            onDismiss = {}
        )
    }
}
