package app.opusreader.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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

@Composable
fun DisplaySettingsLayoutTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    val settings by engine.settings.collectAsState()

    val pageColors = listOf(
        "WHITE" to Color(0xFFFFFFFF),
        "SEPIA" to Color(0xFFF9EFDA),
        "GREEN" to Color(0xFFCAE6D0),
        "BLACK" to Color(0xFF000000)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = (38 / 2.625).dp)
    ) {
        // Band 3: Page Color Title (y=557..596)
        Text(
            text = "Page Color",
            color = Color.White,
            fontFamily = AmazonEmberFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = Modifier.padding(horizontal = 21.dp)
        )

        Spacer(modifier = Modifier.height((17 / 2.625).dp))

        // Band 4: Page Color Palette (y=613..732)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp),
            horizontalArrangement = Arrangement.spacedBy((22 / 2.625).dp)
        ) {
            pageColors.forEach { (name, color) ->
                val isSelected = settings.pageColor == name
                Box(
                    modifier = Modifier
                        .size((120 / 2.625).dp)
                        .clip(CircleShape)
                        .clickable { engine.updateSettings { it.copy(pageColor = name) } },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 3.dp,
                                    color = Color(0xFFA3D7F5),
                                    shape = CircleShape
                                )
                                .padding((8 / 2.625).dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size((100 / 2.625).dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 1.dp,
                                    color = if (name == "BLACK") Color(0xFF5E5E60) else Color.Black,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height((31 / 2.625).dp))

        // Band 5: Update Page Using System Theme (y=765..811)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height((47 / 2.625).dp)
                .padding(horizontal = 21.dp)
                .clickable {
                    engine.updateSettings { it.copy(updatePageWithSystemTheme = !it.updatePageWithSystemTheme) }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size((47 / 2.625).dp)
                    .border(
                        width = 1.5.dp,
                        color = if (settings.updatePageWithSystemTheme) Color(0xFF2491FF) else Color(0xFF8E8E93),
                        shape = RoundedCornerShape(3.dp)
                    )
                    .background(
                        color = if (settings.updatePageWithSystemTheme) Color(0xFF2491FF) else Color.Transparent,
                        shape = RoundedCornerShape(3.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (settings.updatePageWithSystemTheme) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width((17 / 2.625).dp))

            Text(
                text = "Update Page Using System Theme",
                color = Color.White,
                fontFamily = AmazonEmberFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }

        Spacer(modifier = Modifier.height((42 / 2.625).dp))

        // Band 6: Divider 1 (y=854..855)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height((2 / 2.625).dp)
                .background(Color(0xFF8E8E93))
        )

        Spacer(modifier = Modifier.height((43 / 2.625).dp))

        // Band 7: Continuous Scrolling (y=909..1005)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Continuous Scrolling",
                    color = Color.White,
                    fontFamily = AmazonEmberFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
                Spacer(modifier = Modifier.height((12 / 2.625).dp))
                Text(
                    text = "Read by scrolling vertically.",
                    color = Color(0xFFA8ABAD),
                    fontFamily = AmazonEmberFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            Switch(
                modifier = Modifier.padding(top = (2 / 2.625).dp),
                checked = settings.continuousScrolling,
                onCheckedChange = { sc -> engine.updateSettings { it.copy(continuousScrolling = sc) } },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF2491FF),
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFF39393D),
                    uncheckedThumbColor = Color(0xFF8E8E93),
                    uncheckedBorderColor = Color(0xFF5E5E60)
                )
            )
        }

        Spacer(modifier = Modifier.height((23 / 2.625).dp))

        // Band 8: Divider 2 (y=1050..1051)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height((2 / 2.625).dp)
                .background(Color(0xFF8E8E93))
        )

        Spacer(modifier = Modifier.height((38 / 2.625).dp))

        // Band 9: Margins Title (y=1101..1140)
        Text(
            text = "Margins",
            color = Color.White,
            fontFamily = AmazonEmberFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = Modifier.padding(horizontal = 21.dp)
        )

        Spacer(modifier = Modifier.height((17 / 2.625).dp))

        // Band 10: Margins Preset Buttons (y=1157..1263)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp),
            horizontalArrangement = Arrangement.spacedBy((40 / 2.625).dp)
        ) {
            val marginWidthFractions = listOf(0.64f, 0.46f, 0.28f)
            marginWidthFractions.forEachIndexed { index, fraction ->
                val isSelected = settings.marginPreset == index
                Box(
                    modifier = Modifier
                        .width((241 / 2.625).dp)
                        .height((107 / 2.625).dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) Color(0xFFA3D7F5) else Color(0xFF5E5E60),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { engine.updateSettings { it.copy(marginPreset = index) } }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val lineWidth = size.width * fraction
                        val startX = (size.width - lineWidth) / 2f
                        val endX = startX + lineWidth
                        val strokePx = 2.dp.toPx()
                        val yPositions = listOf(
                            size.height * (25f / 70f),
                            size.height * (35f / 70f),
                            size.height * (45f / 70f)
                        )
                        yPositions.forEach { yPos ->
                            drawLine(
                                color = Color.White,
                                start = Offset(startX, yPos),
                                end = Offset(endX, yPos),
                                strokeWidth = strokePx
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height((56 / 2.625).dp))

        // Band 11: Divider 3 (y=1319..1320)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height((2 / 2.625).dp)
                .background(Color(0xFF8E8E93))
        )

        Spacer(modifier = Modifier.height((38 / 2.625).dp))

        // Band 12: Alignment Title (y=1370..1409)
        Text(
            text = "Alignment",
            color = Color.White,
            fontFamily = AmazonEmberFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = Modifier.padding(horizontal = 21.dp)
        )

        Spacer(modifier = Modifier.height((17 / 2.625).dp))

        // Band 13: Alignment Buttons (y=1426..1532)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp),
            horizontalArrangement = Arrangement.spacedBy((40 / 2.625).dp)
        ) {
            // Button 1: Justified
            Box(
                modifier = Modifier
                    .width((241 / 2.625).dp)
                    .height((107 / 2.625).dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = if (settings.isJustified) 3.dp else 1.dp,
                        color = if (settings.isJustified) Color(0xFFA3D7F5) else Color(0xFF5E5E60),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { engine.updateSettings { it.copy(isJustified = true) } }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val lineWidth = size.width * (50f / 110f)
                    val startX = (size.width - lineWidth) / 2f
                    val endX = startX + lineWidth
                    val strokePx = 2.dp.toPx()
                    val yPositions = listOf(
                        size.height * (26f / 70f),
                        size.height * (32f / 70f),
                        size.height * (38f / 70f),
                        size.height * (44f / 70f)
                    )
                    yPositions.forEach { yPos ->
                        drawLine(
                            color = Color.White,
                            start = Offset(startX, yPos),
                            end = Offset(endX, yPos),
                            strokeWidth = strokePx
                        )
                    }
                }
            }

            // Button 2: Left Aligned
            Box(
                modifier = Modifier
                    .width((241 / 2.625).dp)
                    .height((107 / 2.625).dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = if (!settings.isJustified) 3.dp else 1.dp,
                        color = if (!settings.isJustified) Color(0xFFA3D7F5) else Color(0xFF5E5E60),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { engine.updateSettings { it.copy(isJustified = false) } }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val startX = size.width * (30f / 110f)
                    val strokePx = 2.dp.toPx()
                    val lines = listOf(
                        size.height * (26f / 70f) to size.width * (80f / 110f),
                        size.height * (32f / 70f) to size.width * (75f / 110f),
                        size.height * (38f / 70f) to size.width * (80f / 110f),
                        size.height * (44f / 70f) to size.width * (75f / 110f)
                    )
                    lines.forEach { (yPos, endX) ->
                        drawLine(
                            color = Color.White,
                            start = Offset(startX, yPos),
                            end = Offset(endX, yPos),
                            strokeWidth = strokePx
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height((56 / 2.625).dp))

        // Band 14: Divider 4 (y=1588..1589)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .height((2 / 2.625).dp)
                .background(Color(0xFF8E8E93))
        )

        Spacer(modifier = Modifier.height((38 / 2.625).dp))

        // Band 15, 16, 17: Orientation Lock (y=1639..1815)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Orientation Lock",
                    color = Color.White,
                    fontFamily = AmazonEmberFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
                Spacer(modifier = Modifier.height((14 / 2.625).dp))
                Text(
                    text = "Lock content in the current screen orientation. Turn off to use system orientation.",
                    color = Color(0xFFA8ABAD),
                    fontFamily = AmazonEmberFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 16.5.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = Modifier.width((585 / 2.625).dp)
                )
            }

            Switch(
                modifier = Modifier.padding(top = (33 / 2.625).dp),
                checked = settings.orientationLock,
                onCheckedChange = { lock -> engine.updateSettings { it.copy(orientationLock = lock) } },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF2491FF),
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFF39393D),
                    uncheckedThumbColor = Color(0xFF8E8E93),
                    uncheckedBorderColor = Color(0xFF5E5E60)
                )
            )
        }
    }
}

@Preview(name = "18 Display Settings Layout Tab Preview", showBackground = true, backgroundColor = 0xFF2C2C2E)
@Composable
fun DisplaySettingsLayoutTabPreview() {
    OpusReaderTheme {
        DisplaySettingsLayoutTab(engine = FakeReaderEngine())
    }
}
