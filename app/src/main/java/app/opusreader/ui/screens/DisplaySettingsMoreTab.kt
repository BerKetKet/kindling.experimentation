package app.opusreader.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.OpusToggleRow
import app.opusreader.ui.components.ToggleType
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = OpusSpacing.small)
    ) {
        // Assistive reader
        OpusToggleRow(
            title = "Assistive Reader",
            subtitle = "Listen to text-to-speech and read along with highlighted text.",
            checked = assistiveReader,
            onCheckedChange = {
                assistiveReader = it
                engine.updateSettings { current -> current.copy(assistiveReaderEnabled = it) }
            },
            toggleType = ToggleType.SWITCH
        )

        HorizontalDivider(color = OpusDivider, modifier = Modifier.padding(horizontal = OpusSpacing.standard))

        // Reading Ruler
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    readingRuler = if (readingRuler == "Off") "On" else "Off"
                }
                .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium)
        ) {
            Text(
                text = "Reading Ruler",
                color = OpusTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Add a colored ruler over text to help guide reading.",
                color = OpusTextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Set to: $readingRuler",
                color = OpusTextSecondary,
                fontSize = 12.sp
            )
        }

        HorizontalDivider(color = OpusDivider, modifier = Modifier.padding(horizontal = OpusSpacing.standard))

        // Reading Progress
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Cycle progress display
                }
                .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium)
        ) {
            Text(
                text = "Reading Progress",
                color = OpusTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Choose how to display your progress while reading.",
                color = OpusTextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Set to: Location in book, Time left in book, and Hide reading progress",
                color = OpusTextSecondary,
                fontSize = 12.sp
            )
        }

        HorizontalDivider(color = OpusDivider, modifier = Modifier.padding(horizontal = OpusSpacing.standard))

        // Visible Clock
        OpusToggleRow(
            title = "Visible Clock",
            subtitle = "Display the current time on the page.",
            checked = visibleClock,
            onCheckedChange = {
                visibleClock = it
                engine.updateSettings { current -> current.copy(visibleClock = it) }
            },
            toggleType = ToggleType.SWITCH
        )

        HorizontalDivider(color = OpusDivider, modifier = Modifier.padding(horizontal = OpusSpacing.standard))

        // Turn Pages with Volume Controls
        OpusToggleRow(
            title = "Turn Pages with Volume Controls",
            subtitle = "Use your phone's volume buttons to turn pages.",
            checked = volumeKeys,
            onCheckedChange = {
                volumeKeys = it
                engine.updateSettings { current -> current.copy(volumeKeysTurnPages = it) }
            },
            toggleType = ToggleType.SWITCH
        )

        HorizontalDivider(color = OpusDivider, modifier = Modifier.padding(horizontal = OpusSpacing.standard))

        // Page Turn Animation
        OpusToggleRow(
            title = "Page Turn Animation",
            subtitle = "Use a page turn animation while reading.",
            checked = pageTurnAnimation,
            onCheckedChange = {
                pageTurnAnimation = it
                engine.updateSettings { current -> current.copy(pageTurnAnimation = it) }
            },
            toggleType = ToggleType.SWITCH
        )

        HorizontalDivider(color = OpusDivider, modifier = Modifier.padding(horizontal = OpusSpacing.standard))

        // Annotation Menu
        OpusToggleRow(
            title = "Annotation Menu",
            subtitle = "Show the annotation menu when you select a passage of text.",
            checked = annotationMenu,
            onCheckedChange = { annotationMenu = it },
            toggleType = ToggleType.SWITCH
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun DisplaySettingsMoreTabPreview() {
    OpusReaderTheme {
        DisplaySettingsMoreTab(engine = FakeReaderEngine())
    }
}
