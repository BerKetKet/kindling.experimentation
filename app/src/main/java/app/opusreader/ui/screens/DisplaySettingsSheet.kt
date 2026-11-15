package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.refPx

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

    val sheetHeight = if (selectedTab == 0) (2340 - 1186).refPx else (2340 - 284).refPx

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(sheetHeight)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(Color(0xFF2C2C2E))
    ) {
        // Drag Handle
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = (19 / 2.625).dp)
                .size(width = (62 / 2.625).dp, height = (10 / 2.625).dp)
                .clip(CircleShape)
                .background(Color(0xFF8E8E93))
        )

        Spacer(modifier = Modifier.height((114 / 2.625).dp))

        // Tabs Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (30 / 2.625).dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                val isSelected = selectedTab == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { selectedTab = index }
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = tabTitle,
                        color = if (isSelected) Color(0xFFA3D7F5) else Color.White,
                        fontFamily = AmazonEmberFontFamily,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                    Spacer(modifier = Modifier.height((27 / 2.625).dp))
                    Box(
                        modifier = Modifier
                            .height((4 / 2.625).dp)
                            .width((80 / 2.625).dp)
                            .background(if (isSelected) Color(0xFFA3D7F5) else Color.Transparent)
                    )
                }
            }
        }

        // Divider below tabs
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((1 / 2.625).dp)
                .background(Color(0xFF48484A))
        )

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> DisplaySettingsFontTab(engine = engine)
                1 -> DisplaySettingsLayoutTab(engine = engine)
                2 -> DisplaySettingsThemesTab(engine = engine)
                3 -> DisplaySettingsMoreTab(engine = engine)
            }
        }

        // Common Brightness Bar (pinned at bottom, y=2076..2340)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height((264 / 2.625).dp)
                .background(Color(0xFF3A3A3C))
        ) {
            // Divider above brightness bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((1 / 2.625).dp)
                    .background(Color(0xFF48484A))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = (45 / 2.625).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.BrightnessLow,
                    contentDescription = "Decrease brightness",
                    tint = Color(0xFF8E8E93),
                    modifier = Modifier.size(18.dp)
                )

                Slider(
                    value = brightness,
                    onValueChange = {
                        brightness = it
                        engine.updateSettings { current -> current.copy(brightness = it) }
                    },
                    valueRange = 0f..1f,
                    modifier = Modifier
                        .weight(6.3f)
                        .padding(horizontal = 8.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF2491FF),
                        activeTrackColor = Color(0xFF2491FF),
                        inactiveTrackColor = Color(0xFF5D5D61)
                    )
                )

                Icon(
                    imageVector = Icons.Default.BrightnessMedium,
                    contentDescription = "Increase brightness",
                    tint = Color(0xFF8E8E93),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            val newValue = !autoBrightness
                            autoBrightness = newValue
                            engine.updateSettings { current -> current.copy(autoBrightness = newValue) }
                        }
                ) {
                    Checkbox(
                        checked = autoBrightness,
                        onCheckedChange = {
                            autoBrightness = it
                            engine.updateSettings { current -> current.copy(autoBrightness = it) }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF2491FF),
                            uncheckedColor = Color(0xFF8E8E93)
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Auto",
                        color = Color.White,
                        fontFamily = AmazonEmberFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
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
