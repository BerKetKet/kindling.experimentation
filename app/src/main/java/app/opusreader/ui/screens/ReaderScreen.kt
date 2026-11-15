package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.KindleNotebookIcon
import app.opusreader.ui.components.KindleShareBoxIcon
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBookmarkRed
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
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

    if (!chromeVisible) {
        // STATE A: Immersive Full-Screen Reading Canvas
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(pageBgColor)
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
                        .clickable { engine.toggleBookmark() }
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
                    IconButton(onClick = onOverflowClick, modifier = Modifier.size(56.refPx)) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = chromeContentColor,
                            modifier = Modifier.size(22.dp)
                        )
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
                            .clickable { engine.toggleBookmark() }
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

                Spacer(modifier = Modifier.height(48.refPx))

                var sliderValue by remember(location.current) {
                    mutableFloatStateOf(location.current.toFloat())
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.refPx),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = "Page grid thumbnails",
                        tint = chromeContentColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        onValueChangeFinished = { engine.jumpToLocation(sliderValue.toInt()) },
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
