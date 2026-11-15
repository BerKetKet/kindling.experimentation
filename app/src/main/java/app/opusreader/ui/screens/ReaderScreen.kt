package app.opusreader.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.AnnotationItem
import app.opusreader.engine.AnnotationType
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.engine.TextSelectionRange
import app.opusreader.ui.components.KindleNotebookIcon
import app.opusreader.ui.components.KindleShareBoxIcon
import app.opusreader.ui.components.OpusModalBottomSheet
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBookmarkRed
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.PageColorBlack
import app.opusreader.ui.theme.PageColorGreen
import app.opusreader.ui.theme.PageColorSepia
import app.opusreader.ui.theme.PageColorWhite
import app.opusreader.ui.theme.ReaderDarkChromeBg
import app.opusreader.ui.theme.ReaderDarkChromeSubBg
import app.opusreader.ui.theme.ReaderLightChromeBg
import app.opusreader.ui.theme.ReaderLightChromeSecondary
import app.opusreader.ui.theme.ReaderLightChromeSubBg
import app.opusreader.ui.theme.ReaderLightChromeText
import app.opusreader.ui.theme.refPx
import app.opusreader.util.Haptics

@Composable
fun ReaderScreen(
    engine: ReaderEngine,
    onBackClick: () -> Unit = {},
    onContentsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAnnotationsClick: () -> Unit = {},
    onDisplaySettingsClick: () -> Unit = {},
    onOverflowClick: () -> Unit = {},
    initialChromeVisible: Boolean = true,
    modifier: Modifier = Modifier
) {
    var chromeVisible by remember(initialChromeVisible) { mutableStateOf(initialChromeVisible) }
    val location by engine.currentLocation.collectAsState()
    val text by engine.currentText.collectAsState()
    val bookTitle by engine.bookTitle.collectAsState()
    val isBookmarked by engine.isBookmarked.collectAsState()
    val settings by engine.settings.collectAsState()

    val isDarkPage = settings.pageColor == "BLACK"
    val pageBgColor = when (settings.pageColor) {
        "WHITE" -> PageColorWhite
        "SEPIA" -> PageColorSepia
        "GREEN" -> PageColorGreen
        else -> PageColorBlack
    }
    val pageTextColor = when (settings.pageColor) {
        "WHITE" -> Color(0xFF000000)
        "SEPIA" -> Color(0xFF5F4B32)
        "GREEN" -> Color(0xFF3A4B43)
        else -> Color(0xFFCCCCCC)
    }
    val pageSecondaryTextColor = if (isDarkPage) Color(0xFF8E9194) else Color(0xFF5C5C5C)

    val topBarBg = if (isDarkPage) ReaderDarkChromeBg else ReaderLightChromeBg
    val subChromeBg = if (isDarkPage) ReaderDarkChromeSubBg else ReaderLightChromeSubBg
    val chromeContentColor = if (isDarkPage) Color.White else ReaderLightChromeText
    val chromeSecondaryColor = if (isDarkPage) Color(0xFFB9B9B9) else ReaderLightChromeSecondary

    val readerFontFamily = when (settings.fontFamily) {
        "Bookerly" -> BookerlyFontFamily
        "Caecilia", "Droid Serif", "Georgia" -> FontFamily.Serif
        else -> AmazonEmberFontFamily
    }
    val sideMargin = when (settings.marginPreset) {
        0 -> 14.dp
        2 -> 36.dp
        else -> 24.dp
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showOverflowMenu by remember { mutableStateOf(false) }
    var showGoToLocationDialog by remember { mutableStateOf(false) }
    var goToLocationInput by remember { mutableStateOf("") }
    var showBookDetailsDialog by remember { mutableStateOf(false) }
    var showThumbnailViewer by remember { mutableStateOf(false) }
    var isScrubbing by remember { mutableStateOf(false) }
    var sliderValue by remember(location.current) { mutableFloatStateOf(location.current.toFloat()) }
    var showSelectionBar by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var noteInputText by remember { mutableStateOf("") }
    var showDictionaryDialog by remember { mutableStateOf(false) }
    var dictionaryWord by remember { mutableStateOf("ambition") }
    var dragDistanceX by remember { mutableFloatStateOf(0f) }

    if (showGoToLocationDialog) {
        AlertDialog(
            onDismissRequest = { showGoToLocationDialog = false },
            title = {
                Text(
                    text = "Go to Location",
                    fontFamily = BookerlyFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = goToLocationInput,
                    onValueChange = { goToLocationInput = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Location (1-${location.total})") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val target = goToLocationInput.toIntOrNull()
                        if (target != null) {
                            engine.jumpToLocation(target)
                        }
                        showGoToLocationDialog = false
                        goToLocationInput = ""
                    }
                ) {
                    Text("GO", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoToLocationDialog = false }) {
                    Text("CANCEL", color = OpusTextSecondary)
                }
            },
            containerColor = if (isDarkPage) Color(0xFF222222) else Color.White
        )
    }

    if (showBookDetailsDialog) {
        AlertDialog(
            onDismissRequest = { showBookDetailsDialog = false },
            title = {
                Text(
                    text = "Book Details",
                    fontFamily = BookerlyFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("Title: $bookTitle", fontFamily = AmazonEmberFontFamily, color = OpusTextPrimary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Author: Steven Erikson", fontFamily = AmazonEmberFontFamily, color = OpusTextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Current Location: ${location.current} of ${location.total}", fontFamily = AmazonEmberFontFamily, color = OpusTextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Progress: ${location.progressPercent.toInt()}%", fontFamily = AmazonEmberFontFamily, color = OpusTextSecondary, fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showBookDetailsDialog = false }) {
                    Text("CLOSE", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = if (isDarkPage) Color(0xFF222222) else Color.White
        )
    }

    if (showAddNoteDialog) {
        AlertDialog(
            onDismissRequest = { showAddNoteDialog = false },
            title = {
                Text(
                    text = "Add Note",
                    fontFamily = BookerlyFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = noteInputText,
                    onValueChange = { noteInputText = it },
                    label = { Text("Note content") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (noteInputText.isNotBlank()) {
                            engine.addAnnotation(
                                AnnotationItem(
                                    id = "note_${System.currentTimeMillis()}",
                                    location = location.current,
                                    sectionTitle = location.chapterTitle,
                                    selectedText = "Note at location ${location.current}",
                                    note = noteInputText.trim(),
                                    highlightColor = "YELLOW",
                                    type = AnnotationType.NOTE,
                                    isFavorite = false,
                                    dateAdded = "Just now"
                                )
                            )
                        }
                        showAddNoteDialog = false
                        noteInputText = ""
                    }
                ) {
                    Text("SAVE", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNoteDialog = false }) {
                    Text("CANCEL", color = OpusTextSecondary)
                }
            },
            containerColor = if (isDarkPage) Color(0xFF222222) else Color.White
        )
    }

    if (showDictionaryDialog) {
        AlertDialog(
            onDismissRequest = { showDictionaryDialog = false },
            title = {
                Text(
                    text = "Dictionary",
                    fontFamily = BookerlyFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = dictionaryWord,
                        fontFamily = BookerlyFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = OpusTextPrimary,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "noun • An earnest desire for some type of achievement or distinction, as power, honor, fame, or wealth, and the willingness to strive for its attainment.",
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDictionaryDialog = false }) {
                    Text("CLOSE", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = if (isDarkPage) Color(0xFF222222) else Color.White
        )
    }

    if (showThumbnailViewer) {
        OpusModalBottomSheet(
            title = "Page Thumbnails",
            onDismissRequest = { showThumbnailViewer = false },
            showDragHandle = true
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                items(6) { pageNum ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clickable {
                                engine.jumpToLocation((pageNum + 1) * 120)
                                showThumbnailViewer = false
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkPage) Color(0xFF2A2A2A) else Color(0xFFE8E8E8)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Page ${pageNum + 1}\nLoc ${(pageNum + 1) * 120}",
                                fontSize = 11.sp,
                                fontFamily = AmazonEmberFontFamily,
                                color = chromeContentColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    if (!chromeVisible) {
        // STATE A: Immersive Full-Screen Reading Canvas
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(pageBgColor)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { dragDistanceX = 0f },
                        onHorizontalDrag = { change, dragAmount ->
                            dragDistanceX += dragAmount
                            if (kotlin.math.abs(dragDistanceX) > 30f) {
                                change.consume()
                            }
                        },
                        onDragEnd = {
                            if (dragDistanceX < -60f) {
                                engine.nextPage()
                            } else if (dragDistanceX > 60f) {
                                engine.prevPage()
                            }
                        }
                    )
                }
        ) {
            if (settings.visibleClock) {
                Text(
                    text = "9:30",
                    fontFamily = AmazonEmberFontFamily,
                    color = pageSecondaryTextColor,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = sideMargin, top = 12.dp)
                )
            }

            if (isBookmarked) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = "Bookmarked",
                    tint = OpusBookmarkRed,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp)
                        .size(28.dp)
                        .clickable {
                            engine.toggleBookmark()
                            Haptics.tick(context)
                        }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = sideMargin,
                        end = sideMargin,
                        top = 180.refPx,
                        bottom = 120.refPx
                    )
                    .clipToBounds()
                    .then(
                        if (settings.continuousScrolling) Modifier.verticalScroll(rememberScrollState())
                        else Modifier
                    ),
                contentAlignment = Alignment.TopStart
            ) {
                SelectionContainer {
                    Text(
                        text = text,
                        fontFamily = readerFontFamily,
                        color = pageTextColor,
                        fontSize = settings.fontSize.sp,
                        lineHeight = (settings.fontSize * settings.lineSpacingMultiplier).sp,
                        textAlign = if (settings.isJustified) TextAlign.Justify else TextAlign.Start
                    )
                }
            }

            if (settings.readingProgressDisplay != "OFF") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = sideMargin, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Location ${location.current} of ${location.total}",
                        fontFamily = AmazonEmberFontFamily,
                        color = pageSecondaryTextColor,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${location.progressPercent.toInt()}%",
                        fontFamily = AmazonEmberFontFamily,
                        color = pageSecondaryTextColor,
                        fontSize = 11.sp
                    )
                }
            }

            if (showSelectionBar) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 60.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isDarkPage) Color(0xFF2E2F30) else Color.White),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            "YELLOW" to Color(0xFFFFD54F),
                            "BLUE" to Color(0xFF64B5F6),
                            "PINK" to Color(0xFFF06292),
                            "ORANGE" to Color(0xFFFFB74D)
                        ).forEach { (colName, colValue) ->
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(colValue)
                                    .clickable {
                                        engine.createHighlight(TextSelectionRange(0, 40), colName)
                                        showSelectionBar = false
                                        Toast.makeText(context, "Highlighted in $colName", Toast.LENGTH_SHORT).show()
                                    }
                            )
                        }
                        TextButton(onClick = {
                            showSelectionBar = false
                            showAddNoteDialog = true
                        }) {
                            Text("Note", fontSize = 12.sp, color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                        }
                        TextButton(onClick = {
                            showSelectionBar = false
                            dictionaryWord = "ambition"
                            showDictionaryDialog = true
                        }) {
                            Text("Dictionary", fontSize = 12.sp, color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                        }
                        TextButton(onClick = {
                            showSelectionBar = false
                            clipboardManager.setText(AnnotatedString("There is no point in beginning something without ambition."))
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Copy", fontSize = 12.sp, color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { engine.prevPage() }
                )
                Box(
                    modifier = Modifier
                        .weight(0.50f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { chromeVisible = true }
                )
                Box(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { engine.nextPage() }
                )
            }
        }
    } else {
        // STATE B: Reader Chrome Visible — Kindle Zoomed-Out Page-Card Scrubber Mode (D10, R01, R07..R10)
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(subChromeBg)
        ) {
            // Top Chrome Action Bar (y = 0..250, #232323 dark / #FFFFFF light)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(topBarBg)
                    .statusBarsPadding()
            ) {
                Spacer(modifier = Modifier.height(48.refPx))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(57.refPx)
                        .padding(horizontal = 36.refPx),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.size(56.refPx)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back to Library",
                            tint = chromeContentColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(24.refPx))
                    IconButton(onClick = onContentsClick, modifier = Modifier.size(56.refPx)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                            contentDescription = "Table of Contents",
                            tint = chromeContentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = onSearchClick, modifier = Modifier.size(56.refPx)) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Book",
                            tint = chromeContentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(18.refPx))
                    IconButton(onClick = onAnnotationsClick, modifier = Modifier.size(56.refPx)) {
                        KindleNotebookIcon(tint = chromeContentColor)
                    }
                    Spacer(modifier = Modifier.width(18.refPx))
                    IconButton(onClick = onDisplaySettingsClick, modifier = Modifier.size(56.refPx)) {
                        Text(
                            text = "Aa",
                            fontFamily = BookerlyFontFamily,
                            color = chromeContentColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(modifier = Modifier.width(18.refPx))
                    IconButton(onClick = onAnnotationsClick, modifier = Modifier.size(56.refPx)) {
                        KindleShareBoxIcon(tint = chromeContentColor)
                    }
                    Spacer(modifier = Modifier.width(18.refPx))
                    Box {
                        IconButton(onClick = { showOverflowMenu = true }, modifier = Modifier.size(56.refPx)) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = chromeContentColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = showOverflowMenu,
                            onDismissRequest = { showOverflowMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Go to location") },
                                onClick = {
                                    showOverflowMenu = false
                                    showGoToLocationDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Book details") },
                                onClick = {
                                    showOverflowMenu = false
                                    showBookDetailsDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sync to furthest page") },
                                onClick = {
                                    showOverflowMenu = false
                                    Toast.makeText(context, "Already synced to furthest page read.", Toast.LENGTH_SHORT).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Share") },
                                onClick = {
                                    showOverflowMenu = false
                                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                        putExtra(Intent.EXTRA_TEXT, "Reading $bookTitle on Opus Reader")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Book"))
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(56.refPx))
            }

            HorizontalDivider(
                color = if (isDarkPage) OpusDivider else Color(0xFFE0E0E0),
                thickness = 2.refPx
            )

            // Centered Book Title Sub-Header (y = 251..470 in D10, #141414 dark / #FFFFFF light)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(168.refPx)
                    .background(if (isDarkPage) ReaderDarkChromeSubBg else Color.White)
                    .padding(horizontal = 42.refPx),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = bookTitle,
                    style = TextStyle(
                        fontFamily = BookerlyFontFamily,
                        color = chromeContentColor,
                        fontSize = 14.sp,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Center Zoomed-Out Page Card Carousel (y = 470..2000)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(subChromeBg)
                    .padding(vertical = 8.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left peeking previous page slice
                Box(
                    modifier = Modifier
                        .width(36.refPx)
                        .fillMaxHeight(0.94f)
                        .clip(RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp))
                        .background(pageBgColor)
                        .border(0.5.dp, Color(0xFF6E7073), RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp))
                        .clickable { engine.prevPage() }
                        .padding(4.dp)
                        .clipToBounds()
                ) {
                    Text(
                        text = text,
                        fontFamily = readerFontFamily,
                        color = pageTextColor.copy(alpha = 0.7f),
                        fontSize = 9.sp,
                        lineHeight = 13.sp
                    )
                }

                Spacer(modifier = Modifier.width(28.refPx))

                // Active centered page card framed in 2dp Kindle Accent Blue (#2491FF)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(0.96f)
                        .clip(RoundedCornerShape(2.dp))
                        .background(pageBgColor)
                        .border(4.refPx, OpusAccentBlue, RoundedCornerShape(2.dp))
                        .clickable { chromeVisible = false }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 42.refPx, vertical = 48.refPx)
                            .clipToBounds()
                    ) {
                        Text(
                            text = text,
                            fontFamily = readerFontFamily,
                            color = pageTextColor,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            textAlign = if (settings.isJustified) TextAlign.Justify else TextAlign.Start
                        )
                    }

                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Toggle Bookmark",
                        tint = if (isBookmarked) OpusBookmarkRed else chromeSecondaryColor,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 16.refPx, top = 8.refPx)
                            .size(22.dp)
                            .clickable {
                                engine.toggleBookmark()
                                Haptics.tick(context)
                            }
                    )
                }

                Spacer(modifier = Modifier.width(28.refPx))

                // Right peeking next page slice
                Box(
                    modifier = Modifier
                        .width(36.refPx)
                        .fillMaxHeight(0.94f)
                        .clip(RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp))
                        .background(pageBgColor)
                        .border(0.5.dp, Color(0xFF6E7073), RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp))
                        .clickable { engine.nextPage() }
                        .padding(4.dp)
                        .clipToBounds()
                ) {
                    Text(
                        text = text,
                        fontFamily = readerFontFamily,
                        color = pageTextColor.copy(alpha = 0.7f),
                        fontSize = 9.sp,
                        lineHeight = 13.sp
                    )
                }
            }

            // Bottom Scrubber & Chapter/Location Footer (y = 2000..2339, h = 340 px)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.refPx)
                    .background(subChromeBg)
                    .padding(horizontal = 37.refPx),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isScrubbing) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkPage) Color(0xFF2A2A2A) else Color(0xFFF0F0F0)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "${location.chapterTitle} • Location ${sliderValue.toInt()} (${(sliderValue / location.total * 100).toInt()}%)",
                            fontFamily = AmazonEmberFontFamily,
                            color = chromeContentColor,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(42.refPx))

                    Text(
                        text = "Location ${location.current} of ${location.total} • ${location.progressPercent.toInt()}%",
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = chromeSecondaryColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            platformStyle = NoPaddingPlatformStyle
                        )
                    )
                }

                Spacer(modifier = Modifier.height(48.refPx))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.refPx),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { showThumbnailViewer = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = "Page grid thumbnails",
                            tint = chromeContentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Slider(
                        value = sliderValue,
                        onValueChange = {
                            sliderValue = it
                            isScrubbing = true
                        },
                        onValueChangeFinished = {
                            isScrubbing = false
                            engine.jumpToLocation(sliderValue.toInt())
                        },
                        valueRange = 1f..location.total.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = if (isDarkPage) Color.White else OpusAccentBlue,
                            activeTrackColor = OpusAccentBlue,
                            inactiveTrackColor = if (isDarkPage) Color(0xFF4A4B4D) else Color(0xFFC8C8C8)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.refPx)
                    )
                }
            }
        }
    }
}

@Preview(name = "10 Reader Chrome Visible Preview", showBackground = true, backgroundColor = 0xFF141414)
@Composable
fun ReaderScreenPreview() {
    OpusReaderTheme {
        ReaderScreen(engine = FakeReaderEngine(), initialChromeVisible = true)
    }
}
