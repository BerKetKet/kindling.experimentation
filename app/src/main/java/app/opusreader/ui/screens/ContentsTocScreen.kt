package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.engine.TocItem
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusContentsSurface
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

@Composable
fun ContentsTocScreen(
    engine: ReaderEngine,
    onCloseClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tocList by engine.tableOfContents.collectAsState()
    val currentLocation by engine.currentLocation.collectAsState()
    val bookTitle by engine.bookTitle.collectAsState()
    val bookAuthor by engine.bookAuthor.collectAsState()
    val isDark = isOpusDarkTheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusContentsSurface)
            .statusBarsPadding()
    ) {
        // Header block: y=[83..279] (197 px height)
        // Title at y=[118, 185] (2 lines Bookerly Bold), Author at y=[207, 238]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.refPx)
                .padding(horizontal = 44.refPx),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 120.refPx)
            ) {
                Text(
                    text = bookTitle,
                    style = TextStyle(
                        fontFamily = BookerlyFontFamily,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(22.refPx))
                Text(
                    text = bookAuthor,
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 24.refPx)
                    .size(48.refPx)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Table of Contents",
                    tint = OpusTextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        HorizontalDivider(color = OpusDivider, thickness = 2.refPx)

        // Chapter list starting at y = 280 with exact 132.refPx (50.29 dp) row pitch
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds()
        ) {
            items(tocList, key = { it.id }) { item ->
                TocRow(
                    item = item,
                    isCurrent = item.title == currentLocation.chapterTitle,
                    isDark = isDark,
                    onClick = {
                        engine.jumpToLocation(item.targetLocation)
                        onCloseClick()
                    }
                )
                item.children.forEach { child ->
                    TocRow(
                        item = child,
                        isCurrent = child.title == currentLocation.chapterTitle,
                        isDark = isDark,
                        onClick = {
                            engine.jumpToLocation(child.targetLocation)
                            onCloseClick()
                        }
                    )
                }
            }
        }

        // Bottom gesture bar area: y=[2277, 2339] (63 px)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(63.refPx)
                .background(if (isDark) Color(0xFF080808) else Color.Black)
        )
    }
}

@Composable
private fun TocRow(
    item: TocItem,
    isCurrent: Boolean,
    isDark: Boolean,
    onClick: () -> Unit
) {
    // 2-line Foreword row is 181 px tall (y=464..644); single-line rows are 132 px tall; active row is 135 px tall (y=280..414)
    val isMultiLine = item.title.length > 42
    val rowHeight = when {
        isCurrent -> 135.refPx
        isMultiLine -> 181.refPx
        else -> 132.refPx
    }
    val activeBg = if (isDark) Color(0xFF323335) else Color(0xFFE6F2FF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .background(if (isCurrent) activeBg else Color.Transparent)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isCurrent) {
            Box(
                modifier = Modifier
                    .width(11.refPx)
                    .fillMaxHeight()
                    .background(Color(0xFF8E8E93))
            )
        }

        Text(
            text = item.title,
            style = TextStyle(
                fontFamily = AmazonEmberFontFamily,
                color = OpusTextPrimary,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                platformStyle = NoPaddingPlatformStyle
            ),
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = if (item.level > 0) 129.refPx else if (isCurrent) 76.refPx else 87.refPx,
                    end = 56.refPx
                )
        )
    }
}

@Preview(name = "11 Contents TOC Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun ContentsTocScreenPreview() {
    OpusReaderTheme {
        ContentsTocScreen(engine = FakeReaderEngine())
    }
}
