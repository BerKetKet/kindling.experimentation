package app.opusreader.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.ContentType
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.LocalFixtureTrigger
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
    var deviceName by remember {
        mutableStateOf("Reader's 11th Android Device")
    }
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

    val scrollState = rememberScrollState()
    val fixtureTrigger = LocalFixtureTrigger.current
    LaunchedEffect(fixtureTrigger) {
        scrollState.scrollTo(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
            .verticalScroll(scrollState)
    ) {
        // Distance below status bar (y=90) to title band top (y=148): 148 - 90 = 58 px
        Spacer(modifier = Modifier.height(58.refPx))

        // Top bar placed precisely: y=[148, 195], back arrow at x=[51, 97], title at x=150
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(47.refPx)
                .padding(start = 51.refPx, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.refPx)
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to More",
                    tint = OpusTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(53.refPx))

            Text(
                text = "Settings",
                fontFamily = BookerlyFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                color = OpusTextPrimary
            )
        }

        // Distance from top bar bottom (195 px) to Row 1 title top (309 px) = 113 px
        Spacer(modifier = Modifier.height(113.refPx))

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
            subtitle = "Notifications are on.",
            rowHeight = 169.refPx,
            onClick = {}
        )

        // Row 3: Download to SD card (starts y=666, pitch=234 px)
        SettingsCheckboxRow(
            title = "Download to SD card",
            subtitle = "Save new downloads to your external SD Card.\n1.8 GB free",
            rowHeight = 234.refPx,
            checked = downloadToSdCard,
            onCheckedChange = { downloadToSdCard = it }
        )

        // Row 4: Local file access (starts y=900, pitch=248 px)
        SettingsChevronRow(
            title = "Local file access",
            subtitle = "Enable or disable access to view files on your device in\nyour Kindle library.",
            rowHeight = 248.refPx,
            chevronTopPadding = 8.refPx,
            onClick = {}
        )

        // Row 5: Send to Kindle / Import (starts y=1148, pitch=189 px)
        SettingsChevronRow(
            title = "Send to Kindle email address",
            subtitle = "reader_app_kAYRm8@opus-reader.app",
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
            subtitle = "Download the audiobook companions you own\nautomatically when you download the paired books.",
            rowHeight = 231.refPx,
            checked = false,
            onCheckedChange = {}
        )

        // Row 8: Sync (starts y=1737, pitch=283 px)
        SettingsCheckboxRow(
            title = "Sync",
            subtitle = "Sync your reading position, notes, and highlights to\nyour account, so you can pick up where you left off\nwhen switching devices. Tap for more info.",
            rowHeight = 283.refPx,
            checked = syncEnabled,
            onCheckedChange = { syncEnabled = it }
        )

        // Row 9: 1-Click payment method (starts y=2020, pitch=172 px)
        SettingsChevronRow(
            title = "1-Click payment method",
            subtitle = "Manage your 1-Click payment method.",
            rowHeight = 172.refPx,
            onClick = {}
        )

        // Row 10: Manage additional fonts (starts y=2192, pitch=172 px)
        SettingsChevronRow(
            title = "Manage additional fonts",
            subtitle = "Download or remove reading fonts with large file\nsizes.",
            rowHeight = 172.refPx,
            chevronTopPadding = 8.refPx,
            onClick = onFontManagerClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SettingsChevronRow(
    title: String,
    subtitle: String,
    rowHeight: Dp,
    chevronTopPadding: Dp = 13.refPx,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .clickable(onClick = onClick)
            .padding(start = 45.refPx, end = 61.refPx),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    color = OpusTextPrimary
                )
                Spacer(modifier = Modifier.height(1.refPx))
                Text(
                    text = subtitle,
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.5.sp,
                    lineHeight = 20.2.sp,
                    color = OpusTextSecondary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            SettingsChevron(
                modifier = Modifier.padding(top = chevronTopPadding)
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
            .padding(start = 45.refPx, end = 48.refPx),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    color = OpusTextPrimary
                )
                Spacer(modifier = Modifier.height(1.refPx))
                Text(
                    text = subtitle,
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.5.sp,
                    lineHeight = 20.2.sp,
                    color = OpusTextSecondary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            SettingsCheckbox(
                checked = checked,
                modifier = Modifier.padding(top = 13.refPx)
            )
        }
    }
}

@Composable
private fun SettingsChevron(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(26.refPx, 46.refPx)) {
        val strokeWidth = 6.refPx.toPx()
        val path = Path().apply {
            moveTo(strokeWidth / 2f, strokeWidth / 2f)
            lineTo(size.width - strokeWidth / 2f, size.height / 2f)
            lineTo(strokeWidth / 2f, size.height - strokeWidth / 2f)
        }
        drawPath(
            path = path,
            color = OpusTextSecondary,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
private fun SettingsCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier
) {
    val boxShape = RoundedCornerShape(3.dp)
    Box(
        modifier = modifier
            .size(53.refPx)
            .clip(boxShape)
            .then(
                if (checked) Modifier.background(OpusAccentBlue)
                else Modifier
                    .background(Color.Transparent)
                    .border(3.refPx, OpusTextSecondary, boxShape)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Canvas(modifier = Modifier.size(32.refPx, 24.refPx)) {
                val strokeWidth = 5.refPx.toPx()
                val path = Path().apply {
                    moveTo(0f, size.height * 0.45f)
                    lineTo(size.width * 0.35f, size.height)
                    lineTo(size.width, 0f)
                }
                drawPath(
                    path = path,
                    color = OpusBackground,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
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
