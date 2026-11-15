package app.opusreader.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.ContentType
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onImportBook: (Book) -> Unit = {},
    onFontManagerClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var deviceName by remember { mutableStateOf("Opus Reader Device") }
    var showDeviceNameDialog by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(deviceName) }

    var downloadToSdCard by remember { mutableStateOf(false) }
    var autoDownloadAudiobooks by remember { mutableStateOf(false) }
    var syncEnabled by remember { mutableStateOf(true) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var showFontManagerDialog by remember { mutableStateOf(false) }

    val fontPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = queryDisplayName(context, uri) ?: "custom_font.ttf"
            Toast.makeText(context, "Added font: $fileName", Toast.LENGTH_SHORT).show()
        }
    }

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
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = OpusTextPrimary,
                    fontSize = 18.sp
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

    if (showFontManagerDialog) {
        val fonts = listOf("Bookerly", "Amazon Ember", "Caecilia", "Droid Serif", "Georgia", "Baskerville", "OpenDyslexic")
        AlertDialog(
            onDismissRequest = { showFontManagerDialog = false },
            title = {
                Text(
                    text = "Manage Additional Fonts",
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = OpusTextPrimary,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Installed reading fonts:",
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    fonts.forEach { fontName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = fontName,
                                fontFamily = AmazonEmberFontFamily,
                                color = OpusTextPrimary,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Installed",
                                tint = OpusAccentBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            fontPickerLauncher.launch(arrayOf("*/*", "font/ttf", "font/otf"))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ ADD CUSTOM FONT (.TTF / .OTF)", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontManagerDialog = false }) {
                    Text("DONE", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = OpusSurface
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
            .statusBarsPadding()
            .clipToBounds()
            .verticalScroll(rememberScrollState())
    ) {
        // Distance below status bar (y=90) to title band top (y=148): 58 px
        Spacer(modifier = Modifier.height(58.refPx))

        // Top bar placed at y=[148, 194], height=47 px
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(47.refPx)
                .padding(start = 51.refPx, end = 48.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(46.refPx)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to More",
                    tint = OpusTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(36.refPx))

            Text(
                text = "Settings",
                style = TextStyle(
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = OpusTextPrimary,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
        }

        // Distance from top bar bottom (194) to Row 1 title top (309): 115 px
        Spacer(modifier = Modifier.height(115.refPx))

        // Row 1: Device name (starts y=309, pitch=188 px)
        SettingsChevronRow(
            title = "Device name",
            subtitle = deviceName,
            rowHeight = 188.refPx,
            onClick = {
                editedName = deviceName
                showDeviceNameDialog = true
            }
        )

        // Row 2: Push notifications (starts y=497, pitch=169 px)
        SettingsChevronRow(
            title = "Push notifications",
            subtitle = if (pushNotificationsEnabled) "Notifications are on." else "Notifications are off.",
            rowHeight = 169.refPx,
            onClick = { pushNotificationsEnabled = !pushNotificationsEnabled }
        )

        // Row 3: Download to SD card (starts y=666, pitch=234 px)
        SettingsCheckboxRow(
            title = "Download to SD card",
            subtitle = "Save new downloads to your external SD Card. 1.8 GB free",
            rowHeight = 234.refPx,
            checked = downloadToSdCard,
            onCheckedChange = { downloadToSdCard = it }
        )

        // Row 4: Local file access (starts y=900, pitch=248 px)
        SettingsChevronRow(
            title = "Local file access",
            subtitle = "Enable or disable access to view files on your device in your Kindle library.",
            rowHeight = 248.refPx,
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

        // Row 5: Import / Send to Kindle (starts y=1148, pitch=189 px)
        SettingsChevronRow(
            title = "Import EPUB / PDF books",
            subtitle = "Select local book files from device storage",
            rowHeight = 189.refPx,
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

        // Row 6: Book updates (starts y=1337, pitch=169 px)
        SettingsChevronRow(
            title = "Book updates",
            subtitle = "See recent updates and enable automatic updates.",
            rowHeight = 169.refPx,
            onClick = {}
        )

        // Row 7: Auto-download audiobooks (starts y=1506, pitch=231 px)
        SettingsCheckboxRow(
            title = "Auto-download audiobooks",
            subtitle = "Download the audiobook companions you own automatically when you download the paired books.",
            rowHeight = 231.refPx,
            checked = autoDownloadAudiobooks,
            onCheckedChange = { autoDownloadAudiobooks = it }
        )

        // Row 8: Sync (starts y=1737, pitch=283 px)
        SettingsCheckboxRow(
            title = "Sync",
            subtitle = "Sync your reading position, notes, and highlights to your account, so you can pick up where you left off when switching devices. Tap for more info.",
            rowHeight = 283.refPx,
            checked = syncEnabled,
            onCheckedChange = { syncEnabled = it }
        )

        // Row 9: Manage additional fonts (starts y=2020, pitch=172 px)
        SettingsChevronRow(
            title = "Manage additional fonts",
            subtitle = "Download or remove reading fonts with large file sizes.",
            rowHeight = 172.refPx,
            onClick = {
                onFontManagerClick()
                showFontManagerDialog = true
            }
        )
    }
}

@Composable
private fun SettingsChevronRow(
    title: String,
    subtitle: String,
    rowHeight: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .clickable(onClick = onClick)
            .padding(start = 44.refPx, end = 48.refPx),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = OpusTextPrimary,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
                Spacer(modifier = Modifier.height(10.refPx))
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp,
                        color = OpusTextSecondary,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = OpusTextSecondary,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(22.dp)
            )
        }
    }
}

@Composable
private fun SettingsCheckboxRow(
    title: String,
    subtitle: String,
    rowHeight: Dp,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .clickable { onCheckedChange(!checked) }
            .padding(start = 44.refPx, end = 48.refPx),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = OpusTextPrimary,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
                Spacer(modifier = Modifier.height(10.refPx))
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp,
                        color = OpusTextSecondary,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            val boxShape = RoundedCornerShape(2.dp)
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(53.refPx)
                    .clip(boxShape)
                    .then(
                        if (checked) Modifier.background(OpusAccentBlue)
                        else Modifier
                            .background(Color.Transparent)
                            .border(1.5.dp, OpusTextSecondary, boxShape)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (checked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
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

@Preview(name = "04 Settings Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun SettingsScreenPreview() {
    OpusReaderTheme {
        SettingsScreen()
    }
}
