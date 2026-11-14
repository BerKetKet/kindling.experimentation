package app.opusreader.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.EditNote
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBookmarkRed
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.PageColorBlack
import app.opusreader.ui.theme.PageColorGreen
import app.opusreader.ui.theme.PageColorSepia
import app.opusreader.ui.theme.PageColorWhite

@Composable
fun ReaderScreen(
    engine: ReaderEngine,
    onBackClick: () -> Unit = {},
    onContentsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAnnotationsClick: () -> Unit = {},
    onDisplaySettingsClick: () -> Unit = {},
    onOverflowClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var chromeVisible by remember { mutableStateOf(true) }
    val location by engine.currentLocation.collectAsState()
    val text by engine.currentText.collectAsState()
    val isBookmarked by engine.isBookmarked.collectAsState()
    val settings by engine.settings.collectAsState()

    val pageBgColor = when (settings.pageColor) {
        "WHITE" -> PageColorWhite
        "SEPIA" -> PageColorSepia
        "GREEN" -> PageColorGreen
        else -> PageColorBlack
    }

    val pageTextColor = when (settings.pageColor) {
        "WHITE", "SEPIA", "GREEN" -> Color(0xFF1A1A1A)
        else -> Color(0xFFE8E8E8)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(pageBgColor)
    ) {
        // Reader text content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = when (settings.marginPreset) {
                        0 -> 12.dp
                        2 -> 36.dp
                        else -> 24.dp
                    },
                    end = when (settings.marginPreset) {
                        0 -> 12.dp
                        2 -> 36.dp
                        else -> 24.dp
                    },
                    top = if (chromeVisible) 80.dp else 40.dp,
                    bottom = if (chromeVisible) 110.dp else 40.dp
                ),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = text,
                color = pageTextColor,
                fontSize = settings.fontSize.sp,
                lineHeight = (settings.fontSize * settings.lineSpacingMultiplier).sp,
                textAlign = if (settings.isJustified) TextAlign.Justify else TextAlign.Start,
                fontFamily = when (settings.fontFamily) {
                    "Bookerly", "Georgia" -> FontFamily.Serif
                    else -> FontFamily.SansSerif
                }
            )
        }

        // Tap zones: left 25% prev, right 25% next, center 50% toggle chrome
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
                    .weight(0.5f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { chromeVisible = !chromeVisible }
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

        // Bookmark ribbon (top right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = if (chromeVisible) 64.dp else 16.dp)
                .clickable { engine.toggleBookmark() }
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.Bookmark,
                contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                tint = if (isBookmarked) OpusBookmarkRed else OpusTextSecondary.copy(alpha = 0.6f),
                modifier = Modifier.size(32.dp)
            )
        }

        // Top Chrome
        AnimatedVisibility(
            visible = chromeVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(OpusSpacing.topBarHeight)
                    .background(OpusSurface)
                    .padding(horizontal = OpusSpacing.xsmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Library",
                        tint = OpusTextPrimary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onContentsClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Table of Contents",
                        tint = OpusTextPrimary
                    )
                }

                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Book",
                        tint = OpusTextPrimary
                    )
                }

                IconButton(onClick = onAnnotationsClick) {
                    Icon(
                        imageVector = Icons.Outlined.EditNote,
                        contentDescription = "Annotations",
                        tint = OpusTextPrimary
                    )
                }

                IconButton(onClick = onDisplaySettingsClick) {
                    Text(
                        text = "Aa",
                        color = OpusTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onOverflowClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = OpusTextPrimary
                    )
                }
            }
        }

        // Bottom Chrome with Scrubber
        AnimatedVisibility(
            visible = chromeVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OpusSurface)
                    .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Location label
                Text(
                    text = "Location ${location.current} of ${location.total} · ${location.progressPercent.toInt()}%",
                    color = OpusTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                // Scrubber slider
                var sliderValue by remember(location.current) {
                    mutableFloatStateOf(location.current.toFloat())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Page grid thumbnails",
                        tint = OpusTextSecondary,
                        modifier = Modifier.size(24.dp)
                    )

                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        onValueChangeFinished = { engine.jumpToLocation(sliderValue.toInt()) },
                        valueRange = 1f..location.total.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = OpusAccentBlue,
                            inactiveTrackColor = Color(0xFF404040)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = OpusSpacing.small)
                    )
                }
            }
        }
    }
}

@Preview(name = "10 Reader Preview", showBackground = true, backgroundColor = 0xFF141414)
@Composable
fun ReaderScreenPreview() {
    OpusReaderTheme {
        ReaderScreen(engine = FakeReaderEngine())
    }
}
