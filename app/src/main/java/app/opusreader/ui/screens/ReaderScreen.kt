package app.opusreader.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.OpusBookmarkRed
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.PageColorBlack
import app.opusreader.ui.theme.PageColorGreen
import app.opusreader.ui.theme.PageColorSepia
import app.opusreader.ui.theme.PageColorWhite
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
                    top = if (chromeVisible) 100.dp else 40.dp,
                    bottom = if (chromeVisible) 140.dp else 40.dp
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
                .padding(end = 20.dp, top = if (chromeVisible) 105.dp else 16.dp)
                .clickable { engine.toggleBookmark() }
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.Bookmark,
                contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                tint = if (isBookmarked) OpusBookmarkRed else OpusTextSecondary.copy(alpha = 0.6f),
                modifier = Modifier.size(32.dp)
            )
        }

        // Top Chrome: y=0..251 (background #232323)
        AnimatedVisibility(
            visible = chromeVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(251.refPx)
                    .background(Color(0xFF232323))
            ) {
                // Status bar spacer (y=0..90)
                Spacer(modifier = Modifier.height(90.refPx))

                // Icons Row: y=90..251 (height 161 px)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(161.refPx)
                        .offset(y = (-4).refPx),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button (width 132 px, center cx=66 px)
                    Box(
                        modifier = Modifier
                            .width(132.refPx)
                            .fillMaxHeight()
                            .offset(x = 3.refPx)
                            .clickable(onClick = onBackClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reader_back),
                            contentDescription = "Back to Library",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Table of Contents (width 149 px, center cx=206.5 px)
                    Box(
                        modifier = Modifier
                            .width(149.refPx)
                            .fillMaxHeight()
                            .clickable(onClick = onContentsClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reader_toc),
                            contentDescription = "Table of Contents",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Aa display settings (width 138 px, center cx=598 px)
                    Box(
                        modifier = Modifier
                            .width(138.refPx)
                            .fillMaxHeight()
                            .clickable(onClick = onDisplaySettingsClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reader_aa),
                            contentDescription = "Display Settings",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Search (width 138 px, center cx=736.5 px)
                    Box(
                        modifier = Modifier
                            .width(138.refPx)
                            .fillMaxHeight()
                            .clickable(onClick = onSearchClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reader_search),
                            contentDescription = "Search Book",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Notebook annotations (width 138 px, center cx=873 px)
                    Box(
                        modifier = Modifier
                            .width(138.refPx)
                            .fillMaxHeight()
                            .clickable(onClick = onAnnotationsClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reader_notebook),
                            contentDescription = "Annotations",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Overflow 3-dots (width 138 px, center cx=1011 px)
                    Box(
                        modifier = Modifier
                            .width(138.refPx)
                            .fillMaxHeight()
                            .clickable(onClick = onOverflowClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reader_overflow),
                            contentDescription = "More Options",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Bottom Chrome with Scrubber (y=1994..2340, background #141414)
        AnimatedVisibility(
            visible = chromeVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(346.refPx)
                    .background(Color(0xFF141414))
            ) {
                // Top blue line at y=1994..1996
                Box(
                    modifier = Modifier
                        .padding(start = 199.refPx, end = 200.refPx)
                        .fillMaxWidth()
                        .height(3.refPx)
                        .background(Color(0xFF29ABE2))
                )

                // Location label (y=2045..2074)
                Text(
                    text = "Location ${location.current} of ${location.total} · ${location.progressPercent.toInt()}%",
                    color = Color(0xFFB9B9B9),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(top = 36.5.refPx)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(49.refPx))

                // Interactive Scrubber and Birds Eye View icon row
                var currentLocVal by remember(location.current) {
                    mutableFloatStateOf(location.current.toFloat())
                }

                val density = LocalDensity.current
                val trackStartX = with(density) { 169.refPx.toPx() }
                val trackEndX = with(density) { 1012.refPx.toPx() }
                val trackWidth = trackEndX - trackStartX
                val thumbRadiusPx = with(density) { 24.refPx.toPx() }
                val trackHeightPx = with(density) { 4.refPx.toPx() }
                val trackYPx = with(density) { 56.5.refPx.toPx() }

                val fraction = if (location.total > 1) {
                    ((currentLocVal - 1f) / (location.total - 1f)).coerceIn(0f, 1f)
                } else 0f

                val thumbXPx = trackStartX + (fraction * trackWidth)
                val thumbXDp = with(density) { thumbXPx.toDp() }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.refPx)
                        .pointerInput(location.total) {
                            detectTapGestures { offset ->
                                if (offset.x in trackStartX..trackEndX) {
                                    val newFraction = ((offset.x - trackStartX) / trackWidth).coerceIn(0f, 1f)
                                    val newLoc = (1 + newFraction * (location.total - 1)).toInt()
                                    currentLocVal = newLoc.toFloat()
                                    engine.jumpToLocation(newLoc)
                                }
                            }
                        }
                        .pointerInput(location.total) {
                            detectDragGestures(
                                onDrag = { change, _ ->
                                    val newFraction = ((change.position.x - trackStartX) / trackWidth).coerceIn(0f, 1f)
                                    val newLoc = (1 + newFraction * (location.total - 1)).toInt()
                                    currentLocVal = newLoc.toFloat()
                                },
                                onDragEnd = {
                                    engine.jumpToLocation(currentLocVal.toInt())
                                }
                            )
                        }
                ) {
                    // Birds eye grid icon: centered at cx=68.5 px, cy=56.5 px in this box
                    Box(
                        modifier = Modifier
                            .width(137.refPx)
                            .height(110.refPx),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reader_birds_eye),
                            contentDescription = "Multi-page grid",
                            tint = Color(0xFFC5C5C5),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Canvas for track and thumb
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val activeEnd = (thumbXPx - thumbRadiusPx).coerceAtLeast(trackStartX)
                        val inactiveStart = (thumbXPx + thumbRadiusPx).coerceAtMost(trackEndX)

                        // Active track (blue)
                        if (activeEnd > trackStartX) {
                            drawRect(
                                color = Color(0xFF29ABE2),
                                topLeft = Offset(trackStartX, trackYPx - trackHeightPx / 2f),
                                size = Size(activeEnd - trackStartX, trackHeightPx)
                            )
                        }

                        // Inactive track (grey)
                        if (inactiveStart < trackEndX) {
                            drawRect(
                                color = Color(0xFFD4D4D4),
                                topLeft = Offset(inactiveStart, trackYPx - trackHeightPx / 2f),
                                size = Size(trackEndX - inactiveStart, trackHeightPx)
                            )
                        }

                        // Thumb circle
                        drawCircle(
                            color = Color.White,
                            radius = thumbRadiusPx,
                            center = Offset(thumbXPx, trackYPx)
                        )
                    }

                    // Location number under thumb
                    Text(
                        text = "${currentLocVal.toInt()}",
                        color = Color(0xFFA0A0A0),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = (thumbXPx - with(density) { 30.dp.toPx() }).toInt(),
                                    y = with(density) { (trackYPx + thumbRadiusPx + 4.refPx.toPx()).toInt() }
                                )
                            }
                            .width(60.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bottom divider line at y=2276 (height 1 px, color #898989)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.refPx)
                        .background(Color(0xFF898989))
                )

                // Gesture bar space
                Spacer(modifier = Modifier.height(63.refPx))
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
