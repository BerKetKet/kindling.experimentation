package app.opusreader.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

@Composable
fun DisplaySettingsMoreTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    val settings by engine.settings.collectAsState()
    var assistiveReader by remember { mutableStateOf(settings.assistiveReaderEnabled) }
    var readingRuler by remember { mutableStateOf("Off") }
    var visibleClock by remember { mutableStateOf(settings.visibleClock) }
    var volumeKeys by remember { mutableStateOf(settings.volumeKeysTurnPages) }
    var pageTurnAnimation by remember { mutableStateOf(settings.pageTurnAnimation) }
    var annotationMenu by remember { mutableStateOf(true) }
    val isDark = isOpusDarkTheme
    val dividerColor = if (isDark) Color(0xFF8E8E93) else Color(0xFF222222)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Gap from tab bar divider (y=507) to Row 1 top (y=519): 12 px
        Spacer(modifier = Modifier.height(12.refPx))

        // Row 1: Assistive Reader (y=[519, 659], h=141 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(141.refPx)
                .clickable {
                    val next = !assistiveReader
                    assistiveReader = next
                    engine.updateSettings { current -> current.copy(assistiveReaderEnabled = next) }
                }
                .padding(horizontal = 55.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Assistive Reader",
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
                    text = "Listen to text-to-speech and read along with highlighted text.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 16.5.sp,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Spacer(modifier = Modifier.width(24.refPx))
            Switch(
                checked = assistiveReader,
                onCheckedChange = {
                    assistiveReader = it
                    engine.updateSettings { current -> current.copy(assistiveReaderEnabled = it) }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = OpusAccentBlue,
                    uncheckedThumbColor = OpusTextSecondary,
                    uncheckedTrackColor = Color(0xFF4A4B4D)
                )
            )
        }

        // Gap to Divider 1 (y=699): 40 px
        Spacer(modifier = Modifier.height(40.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to Row 2 top (y=750): 50 px
        Spacer(modifier = Modifier.height(50.refPx))

        // Row 2: Reading Ruler (title y=[750, 789], subtitle y=[812, 846], Set to y=[870, 896])
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    readingRuler = if (readingRuler == "Off") "On" else "Off"
                }
                .padding(horizontal = 57.refPx)
        ) {
            Text(
                text = "Reading Ruler",
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
                text = "Add a colored ruler over text to help guide reading.",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextSecondary,
                    fontSize = 12.5.sp,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
            Spacer(modifier = Modifier.height(24.refPx))
            Text(
                text = "Set to: $readingRuler",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
        }

        // Gap to Divider 2 (y=947): 51 px
        Spacer(modifier = Modifier.height(51.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to Row 3 top (y=998): 50 px
        Spacer(modifier = Modifier.height(50.refPx))

        // Row 3: Reading Progress (title y=[998, 1037], subtitle 2 lines y=[1060, 1138], Set to 2 lines y=[1162, 1240])
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 57.refPx)
        ) {
            Text(
                text = "Reading Progress",
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
                text = "Choose how to display your progress while reading.",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextSecondary,
                    fontSize = 12.5.sp,
                    lineHeight = 16.8.sp,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
            Spacer(modifier = Modifier.height(24.refPx))
            Text(
                text = "Set to: Location in book, Time left in book, and Hide reading progress",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 12.5.sp,
                    lineHeight = 16.8.sp,
                    fontWeight = FontWeight.Bold,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
        }

        // Gap to Divider 3 (y=1283): 43 px
        Spacer(modifier = Modifier.height(43.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to Row 4 top (y=1338): 54 px
        Spacer(modifier = Modifier.height(54.refPx))

        // Row 4: Visible Clock (y=[1338, 1434], h=97 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(97.refPx)
                .clickable {
                    val next = !visibleClock
                    visibleClock = next
                    engine.updateSettings { current -> current.copy(visibleClock = next) }
                }
                .padding(horizontal = 55.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Visible Clock",
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
                    text = "Display the current time on the page.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Switch(
                checked = visibleClock,
                onCheckedChange = {
                    visibleClock = it
                    engine.updateSettings { current -> current.copy(visibleClock = it) }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = OpusAccentBlue,
                    uncheckedThumbColor = OpusTextSecondary,
                    uncheckedTrackColor = Color(0xFF4A4B4D)
                )
            )
        }

        // Gap to Divider 4 (y=1479): 45 px
        Spacer(modifier = Modifier.height(45.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to Row 5 top (y=1530): 50 px
        Spacer(modifier = Modifier.height(50.refPx))

        // Row 5: Turn Pages with Volume Controls (y=[1530, 1670], h=141 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(141.refPx)
                .clickable {
                    val next = !volumeKeys
                    volumeKeys = next
                    engine.updateSettings { current -> current.copy(volumeKeysTurnPages = next) }
                }
                .padding(horizontal = 56.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Turn Pages with Volume Controls",
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
                    text = "Use your phone's volume buttons to turn pages.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 16.5.sp,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Spacer(modifier = Modifier.width(24.refPx))
            Switch(
                checked = volumeKeys,
                onCheckedChange = {
                    volumeKeys = it
                    engine.updateSettings { current -> current.copy(volumeKeysTurnPages = it) }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = OpusAccentBlue,
                    uncheckedThumbColor = OpusTextSecondary,
                    uncheckedTrackColor = Color(0xFF4A4B4D)
                )
            )
        }

        // Gap to Divider 5 (y=1710): 40 px
        Spacer(modifier = Modifier.height(40.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to Row 6 top (y=1765): 54 px
        Spacer(modifier = Modifier.height(54.refPx))

        // Row 6: Page Turn Animation (y=[1765, 1861], h=97 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(97.refPx)
                .clickable {
                    val next = !pageTurnAnimation
                    pageTurnAnimation = next
                    engine.updateSettings { current -> current.copy(pageTurnAnimation = next) }
                }
                .padding(horizontal = 58.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Page Turn Animation",
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
                    text = "Use a page turn animation while reading.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Switch(
                checked = pageTurnAnimation,
                onCheckedChange = {
                    pageTurnAnimation = it
                    engine.updateSettings { current -> current.copy(pageTurnAnimation = it) }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = OpusAccentBlue,
                    uncheckedThumbColor = OpusTextSecondary,
                    uncheckedTrackColor = Color(0xFF4A4B4D)
                )
            )
        }

        // Gap to Divider 6 (y=1906): 45 px
        Spacer(modifier = Modifier.height(45.refPx))
        HorizontalDivider(color = dividerColor, thickness = 2.refPx, modifier = Modifier.padding(horizontal = 55.refPx))

        // Gap to Row 7 top (y=1957): 50 px
        Spacer(modifier = Modifier.height(50.refPx))

        // Row 7: Annotation Menu (starts at y=1957, clipped naturally by container at y=2077)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(141.refPx)
                .clickable { annotationMenu = !annotationMenu }
                .padding(horizontal = 58.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Annotation Menu",
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
                    text = "Show the annotation menu when you select a passage of text.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 16.5.sp,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Spacer(modifier = Modifier.width(24.refPx))
            Switch(
                checked = annotationMenu,
                onCheckedChange = { annotationMenu = it },
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

@Preview(showBackground = true, backgroundColor = 0xFF2C2C2E)
@Composable
fun DisplaySettingsMoreTabPreview() {
    OpusReaderTheme {
        DisplaySettingsMoreTab(engine = FakeReaderEngine())
    }
}
