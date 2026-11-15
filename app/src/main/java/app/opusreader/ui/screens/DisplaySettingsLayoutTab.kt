package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.AlignmentLinesIcon
import app.opusreader.ui.components.MarginLinesIcon
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSheetActionBlue
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.PageColorBlack
import app.opusreader.ui.theme.PageColorGreen
import app.opusreader.ui.theme.PageColorSepia
import app.opusreader.ui.theme.PageColorWhite
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

@Composable
fun DisplaySettingsLayoutTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    val settings by engine.settings.collectAsState()
    val isDark = isOpusDarkTheme
    val dividerColor = if (isDark) Color(0xFF8E8E93) else Color(0xFF222222)

    val pageColors = listOf(
        "WHITE" to PageColorWhite,
        "SEPIA" to PageColorSepia,
        "GREEN" to PageColorGreen,
        "BLACK" to PageColorBlack
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Gap from tab bar divider (y=507) to "Page Color" top (y=557): 50 px
        Spacer(modifier = Modifier.height(50.refPx))

        // "Page Color": y=[557, 596], h=40 px, x=58
        Text(
            text = "Page Color",
            style = TextStyle(
                fontFamily = AmazonEmberFontFamily,
                color = OpusTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                platformStyle = NoPaddingPlatformStyle
            ),
            modifier = Modifier.padding(horizontal = 58.refPx)
        )

        // Gap to swatches top (y=613): 17 px
        Spacer(modifier = Modifier.height(17.refPx))

        // Swatches row: y=[613, 732], h=120 px (45.7 dp), x=[81, 724]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 81.refPx),
            horizontalArrangement = Arrangement.spacedBy(54.refPx)
        ) {
            pageColors.forEach { (name, color) ->
                val isSelected = settings.pageColor == name
                Box(
                    modifier = Modifier
                        .size(120.refPx)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isSelected) 6.refPx else 3.refPx,
                            color = if (isSelected) OpusSheetActionBlue else Color(0xFF6E7073),
                            shape = CircleShape
                        )
                        .clickable { engine.updateSettings { it.copy(pageColor = name) } }
                )
            }
        }

        // Gap to checkbox row top (y=765): 33 px
        Spacer(modifier = Modifier.height(33.refPx))

        // Checkbox row: y=[765, 811], h=47 px, x=55
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(47.refPx)
                .clickable {
                    engine.updateSettings { it.copy(updatePageWithSystemTheme = !it.updatePageWithSystemTheme) }
                }
                .padding(horizontal = 55.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val boxShape = RoundedCornerShape(2.dp)
            Box(
                modifier = Modifier
                    .size(47.refPx)
                    .clip(boxShape)
                    .then(
                        if (settings.updatePageWithSystemTheme) Modifier.background(OpusAccentBlue)
                        else Modifier.border(1.5.dp, OpusTextSecondary, boxShape)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (settings.updatePageWithSystemTheme) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(28.refPx))
            Text(
                text = "Update Page Using System Theme",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
        }

        // Gap to Divider 1 (y=854): 43 px
        Spacer(modifier = Modifier.height(43.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to Continuous Scrolling top (y=909): 54 px
        Spacer(modifier = Modifier.height(54.refPx))

        // Continuous Scrolling: y=[909, 1005], h=97 px
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(97.refPx)
                .clickable {
                    engine.updateSettings { it.copy(continuousScrolling = !it.continuousScrolling) }
                }
                .padding(horizontal = 57.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Continuous Scrolling",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
                Spacer(modifier = Modifier.height(18.refPx))
                Text(
                    text = "Read by scrolling vertically.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Switch(
                checked = settings.continuousScrolling,
                onCheckedChange = { sc -> engine.updateSettings { it.copy(continuousScrolling = sc) } },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = OpusAccentBlue,
                    uncheckedThumbColor = OpusTextSecondary,
                    uncheckedTrackColor = Color(0xFF4A4B4D)
                )
            )
        }

        // Gap to Divider 2 (y=1050): 45 px
        Spacer(modifier = Modifier.height(45.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to "Margins" top (y=1101): 50 px
        Spacer(modifier = Modifier.height(50.refPx))
        Text(
            text = "Margins",
            style = TextStyle(
                fontFamily = AmazonEmberFontFamily,
                color = OpusTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                platformStyle = NoPaddingPlatformStyle
            ),
            modifier = Modifier.padding(horizontal = 58.refPx)
        )

        // Gap to Margin preset boxes (y=1157): 17 px
        Spacer(modifier = Modifier.height(17.refPx))

        // Margin preset boxes: y=[1157, 1263], h=107 px, x=[74, 862]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 74.refPx),
            horizontalArrangement = Arrangement.spacedBy(38.refPx)
        ) {
            for (preset in 0..2) {
                val isSelected = settings.marginPreset == preset
                val shape = RoundedCornerShape(4.dp)
                Box(
                    modifier = Modifier
                        .size(width = 236.refPx, height = 107.refPx)
                        .clip(shape)
                        .background(Color.Black)
                        .border(
                            width = if (isSelected) 4.refPx else 2.refPx,
                            color = if (isSelected) OpusSheetActionBlue else Color(0xFF6E7073),
                            shape = shape
                        )
                        .clickable { engine.updateSettings { it.copy(marginPreset = preset) } },
                    contentAlignment = Alignment.Center
                ) {
                    MarginLinesIcon(preset = preset, tint = Color.White)
                }
            }
        }

        // Gap to Divider 3 (y=1319): 56 px
        Spacer(modifier = Modifier.height(56.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to "Alignment" top (y=1370): 50 px
        Spacer(modifier = Modifier.height(50.refPx))
        Text(
            text = "Alignment",
            style = TextStyle(
                fontFamily = AmazonEmberFontFamily,
                color = OpusTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                platformStyle = NoPaddingPlatformStyle
            ),
            modifier = Modifier.padding(horizontal = 55.refPx)
        )

        // Gap to Alignment boxes (y=1426): 17 px
        Spacer(modifier = Modifier.height(17.refPx))

        // Alignment boxes: y=[1426, 1532], h=107 px, x=[71, 581]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 71.refPx),
            horizontalArrangement = Arrangement.spacedBy(38.refPx)
        ) {
            listOf(true, false).forEach { justified ->
                val isSelected = settings.isJustified == justified
                val shape = RoundedCornerShape(4.dp)
                Box(
                    modifier = Modifier
                        .size(width = 236.refPx, height = 107.refPx)
                        .clip(shape)
                        .background(Color.Black)
                        .border(
                            width = if (isSelected) 4.refPx else 2.refPx,
                            color = if (isSelected) OpusSheetActionBlue else Color(0xFF6E7073),
                            shape = shape
                        )
                        .clickable { engine.updateSettings { it.copy(isJustified = justified) } },
                    contentAlignment = Alignment.Center
                ) {
                    AlignmentLinesIcon(justified = justified, tint = Color.White)
                }
            }
        }

        // Gap to Divider 4 (y=1588): 56 px
        Spacer(modifier = Modifier.height(56.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to Orientation Lock top (y=1639): 50 px
        Spacer(modifier = Modifier.height(50.refPx))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    engine.updateSettings { it.copy(orientationLock = !it.orientationLock) }
                }
                .padding(horizontal = 57.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Orientation Lock",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
                Spacer(modifier = Modifier.height(14.refPx))
                Text(
                    text = "Lock content in the current screen orientation. Turn off to use system orientation.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Spacer(modifier = Modifier.width(24.refPx))
            Switch(
                checked = settings.orientationLock,
                onCheckedChange = { lock -> engine.updateSettings { it.copy(orientationLock = lock) } },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = OpusAccentBlue,
                    uncheckedThumbColor = OpusTextSecondary,
                    uncheckedTrackColor = Color(0xFF4A4B4D)
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
