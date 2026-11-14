package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.components.OpusToggleRow
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.components.ToggleType
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onDeviceNameClick: () -> Unit = {},
    onImportFilesClick: () -> Unit = {},
    onFontManagerClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var downloadToSdCard by remember { mutableStateOf(false) }
    var syncEnabled by remember { mutableStateOf(true) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
            .verticalScroll(rememberScrollState())
    ) {
        OpusTopBar(
            title = "Settings",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navigationIconDescription = "Back to More",
            onNavigationClick = onBackClick
        )

        SectionTitle("Device & Account")

        OpusToggleRow(
            title = "Device name",
            subtitle = "Opus Android Reader",
            toggleType = ToggleType.CHEVRON,
            onClick = onDeviceNameClick
        )

        OpusToggleRow(
            title = "Push notifications",
            subtitle = if (pushNotificationsEnabled) "Notifications are on." else "Notifications are off.",
            checked = pushNotificationsEnabled,
            toggleType = ToggleType.SWITCH,
            onCheckedChange = { pushNotificationsEnabled = it }
        )

        HorizontalDivider(color = OpusDivider.copy(alpha = 0.5f), thickness = 0.5.dp)

        SectionTitle("Storage & Downloads")

        OpusToggleRow(
            title = "Download to SD card",
            subtitle = "Save new downloads to your external SD Card.",
            checked = downloadToSdCard,
            toggleType = ToggleType.CHECKBOX,
            onCheckedChange = { downloadToSdCard = it }
        )

        OpusToggleRow(
            title = "Import EPUB / PDF books",
            subtitle = "Select local book files from device storage",
            toggleType = ToggleType.CHEVRON,
            onClick = onImportFilesClick
        )

        HorizontalDivider(color = OpusDivider.copy(alpha = 0.5f), thickness = 0.5.dp)

        SectionTitle("Sync & Customization")

        OpusToggleRow(
            title = "Sync library & reading progress",
            subtitle = "Keep your reading position and bookmarks synchronized.",
            checked = syncEnabled,
            toggleType = ToggleType.CHECKBOX,
            onCheckedChange = { syncEnabled = it }
        )

        OpusToggleRow(
            title = "Manage additional fonts",
            subtitle = "Install custom OTF/TTF fonts for the reader.",
            toggleType = ToggleType.CHEVRON,
            onClick = onFontManagerClick
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = OpusTextSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(
            start = OpusSpacing.standard,
            end = OpusSpacing.standard,
            top = OpusSpacing.standard,
            bottom = OpusSpacing.small
        )
    )
}

@Preview(name = "04 Settings Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun SettingsScreenPreview() {
    OpusReaderTheme {
        SettingsScreen()
    }
}
