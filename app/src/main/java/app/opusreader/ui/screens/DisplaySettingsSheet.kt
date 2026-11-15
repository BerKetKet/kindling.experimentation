package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusDisplaySettingsFooterSurface
import app.opusreader.ui.theme.OpusDisplaySettingsSurface
import app.opusreader.ui.theme.OpusDragHandle
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSheetActionBlue
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

@Composable
fun DisplaySettingsSheet(
    engine: ReaderEngine,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialTab: Int = 0,
    collapsedSheet: Boolean = (initialTab == 0)
) {
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) }
    val tabs = listOf("Font", "Layout", "Themes", "More")
    val settings by engine.settings.collectAsState()
    var brightness by remember(settings.brightness) { mutableStateOf(settings.brightness) }
    var autoBrightness by remember(settings.autoBrightness) { mutableStateOf(settings.autoBrightness) }
    val isDark = isOpusDarkTheme

    // In Kindle: Font tab opens at half-height (y=1176, h=1164px), while Layout/Themes/More expand to y=284 (h=2056px)
    val sheetHeight = if (collapsedSheet) 1164.refPx else 2056.refPx
    val dragHandleTopPadding = if (collapsedSheet) 29.refPx else 18.refPx
    val tabHeaderTopGap = if (collapsedSheet && selectedTab == 0) 122.refPx else 122.refPx
    val dividerColor = if (isDark) Color(0xFF8E8E93) else Color(0xFF222222)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Transparent tap-to-dismiss area above the sheet
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismiss)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(OpusDisplaySettingsSurface)
                .clickable(enabled = false) {}
        ) {
            // Drag handle: 63x11 px (#8E8E93 dark / #9D9D9D light)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dragHandleTopPadding),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 63.refPx, height = 11.refPx)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(OpusDragHandle)
                )
            }

            Spacer(modifier = Modifier.height(tabHeaderTopGap))

            // Left-aligned Tabs Header (Font | Layout | Themes | More) at y=434..474 (or y=1338..1377)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.refPx, end = 30.refPx),
                horizontalArrangement = Arrangement.spacedBy(52.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    val isSelected = selectedTab == index
                    val textColor = if (isSelected) OpusSheetActionBlue else OpusTextPrimary
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedTab = index }
                    ) {
                        Text(
                            text = tabTitle,
                            style = TextStyle(
                                fontFamily = AmazonEmberFontFamily,
                                color = textColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                platformStyle = NoPaddingPlatformStyle
                            )
                        )
                        Spacer(modifier = Modifier.height(29.refPx))
                        Box(
                            modifier = Modifier
                                .height(5.refPx)
                                .width(110.refPx)
                                .background(if (isSelected) OpusSheetActionBlue else Color.Transparent)
                        )
                    }
                }
            }

            HorizontalDivider(color = dividerColor, thickness = 2.refPx)

            // Tab body container clipped above pinned brightness footer (y = 2077..2339, h = 263 px)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clipToBounds()
            ) {
                when (selectedTab) {
                    0 -> DisplaySettingsFontTab(engine = engine)
                    1 -> DisplaySettingsLayoutTab(engine = engine)
                    2 -> DisplaySettingsThemesTab(engine = engine)
                    3 -> DisplaySettingsMoreTab(engine = engine)
                }
            }

            // Pinned Brightness Footer at y = [2077, 2339] (263 px height, #3A3A3C dark / #F0F0F0 light)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(263.refPx)
                    .background(OpusDisplaySettingsFooterSurface)
                    .padding(start = 56.refPx, end = 56.refPx, bottom = 63.refPx),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WbSunny,
                        contentDescription = "Low Brightness",
                        tint = OpusTextSecondary,
                        modifier = Modifier.size(42.refPx)
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
                            .padding(horizontal = 24.refPx),
                        colors = SliderDefaults.colors(
                            thumbColor = OpusAccentBlue,
                            activeTrackColor = OpusAccentBlue,
                            inactiveTrackColor = if (isDark) Color(0xFF6E7073) else Color(0xFFB5B7B9)
                        )
                    )

                    Icon(
                        imageVector = Icons.Outlined.WbSunny,
                        contentDescription = "High Brightness",
                        tint = OpusTextPrimary,
                        modifier = Modifier.size(60.refPx)
                    )

                    Spacer(modifier = Modifier.width(42.refPx))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            val next = !autoBrightness
                            autoBrightness = next
                            engine.updateSettings { current -> current.copy(autoBrightness = next) }
                        }
                    ) {
                        val boxShape = RoundedCornerShape(2.dp)
                        Box(
                            modifier = Modifier
                                .size(48.refPx)
                                .clip(boxShape)
                                .then(
                                    if (autoBrightness) Modifier.background(OpusAccentBlue)
                                    else Modifier.border(1.5.dp, OpusTextSecondary, boxShape)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (autoBrightness) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.refPx))
                        Text(
                            text = "Auto",
                            style = TextStyle(
                                fontFamily = AmazonEmberFontFamily,
                                color = OpusTextPrimary,
                                fontSize = 11.sp,
                                platformStyle = NoPaddingPlatformStyle
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun DisplaySettingsSheetPreview() {
    OpusReaderTheme {
        DisplaySettingsSheet(engine = FakeReaderEngine(), onDismiss = {})
    }
}
