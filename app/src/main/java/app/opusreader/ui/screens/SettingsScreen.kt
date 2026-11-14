package app.opusreader.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.ContentType
import app.opusreader.ui.components.OpusToggleRow
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.components.ToggleType
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onImportBook: (Book) -> Unit = {},
    onFontManagerClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var deviceName by remember { mutableStateOf("Opus Android Reader") }
    var showDeviceNameDialog by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(deviceName) }

    var downloadToSdCard by remember { mutableStateOf(false) }
    var syncEnabled by remember { mutableStateOf(true) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = queryDisplayName(context, uri) ?: "Imported Document"
            val cleanTitle = fileName.substringBeforeLast('.')
            val newBook = Book(
                id = "import_${System.currentTimeMillis()}",
                title = cleanTitle,
                author = "Imported File",
                series = null,
                progressPercent = 0,
                isDownloaded = true,
                fileSize = "2.8 MB",
                coverColor = Color(0xFF2E4034),
                isRead = false,
                contentType = if (fileName.endsWith(".pdf", ignoreCase = true)) ContentType.DOC else ContentType.BOOK,
                collectionIds = emptyList(),
                lastReadTimestamp = System.currentTimeMillis()
            )
            onImportBook(newBook)
            Toast.makeText(context, "Imported: $cleanTitle", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDeviceNameDialog) {
        AlertDialog(
            onDismissRequest = { showDeviceNameDialog = false },
            title = {
                Text(
                    text = "Edit Device Name",
                    color = OpusTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = OpusTextPrimary,
                        unfocusedTextColor = OpusTextPrimary,
                        focusedBorderColor = OpusAccentBlue,
                        unfocusedBorderColor = OpusDivider
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editedName.isNotBlank()) {
                            deviceName = editedName.trim()
                        }
                        showDeviceNameDialog = false
                    }
                ) {
                    Text("SAVE", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeviceNameDialog = false }) {
                    Text("CANCEL", color = OpusTextSecondary)
                }
            },
            containerColor = OpusSurface
        )
    }

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
            subtitle = deviceName,
            toggleType = ToggleType.CHEVRON,
            onClick = {
                editedName = deviceName
                showDeviceNameDialog = true
            }
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
            onClick = {
                filePickerLauncher.launch(
                    arrayOf(
                        "application/epub+zip",
                        "application/pdf",
                        "application/octet-stream",
                        "*/*"
                    )
                )
            }
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

private fun queryDisplayName(context: android.content.Context, uri: Uri): String? {
    return runCatching {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                cursor.getString(nameIndex)
            } else null
        } ?: uri.lastPathSegment
    }.getOrNull()
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
