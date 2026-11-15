package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.engine.TocItem
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.refPx

@Composable
fun ContentsTocScreen(
    engine: ReaderEngine,
    bookTitle: String = "The Will of the Many",
    bookAuthor: String = "James Islington",
    onCloseClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tocList by engine.tableOfContents.collectAsState()
    val currentLocation by engine.currentLocation.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF232323))
    ) {
        // Status bar space (y=0..90)
        Spacer(modifier = Modifier.height(90.refPx))

        // Header (y=90..279, height 189 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(189.refPx)
                .padding(start = 88.refPx, end = 50.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookTitle,
                    color = Color(0xFFEEEEEE),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(22.refPx))
                Text(
                    text = bookAuthor,
                    color = Color(0xFFA6A6A6),
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = onCloseClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Table of Contents",
                    tint = Color(0xFFEEEEEE),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        HorizontalDivider(color = Color(0xFF8E8E93), thickness = 3.refPx)

        // Tabs Row (y=280..414, height 134 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(134.refPx)
                .padding(horizontal = 88.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { /* Chapters tab */ },
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "CHAPTERS",
                    color = Color(0xFFEEEEEE),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .width(75.refPx)
                        .height(3.refPx)
                        .background(Color(0xFF29ABE2))
                )
            }

            Spacer(modifier = Modifier.width(90.refPx))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { /* Notes tab */ },
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "NOTES",
                    color = Color(0xFF8E8E93),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(3.refPx))
            }
        }

        HorizontalDivider(color = Color(0xFF383838), thickness = 1.refPx)

        // Chapter list (15 rows, pitch 132 px)
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tocList, key = { it.id }) { item ->
                val isFirst = item.id == tocList.firstOrNull()?.id
                TocRow(
                    item = item,
                    isFirst = isFirst,
                    isCurrent = item.title == currentLocation.chapterTitle,
                    onClick = {
                        engine.jumpToLocation(item.targetLocation)
                        onCloseClick()
                    }
                )
                item.children.forEach { child ->
                    TocRow(
                        item = child,
                        isFirst = false,
                        isCurrent = child.title == currentLocation.chapterTitle,
                        onClick = {
                            engine.jumpToLocation(child.targetLocation)
                            onCloseClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TocRow(
    item: TocItem,
    isFirst: Boolean,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isFirst) 49.refPx else 132.refPx)
            .clickable { onClick() }
            .padding(
                start = if (item.level > 0) 129.refPx else 88.refPx,
                end = 88.refPx
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = item.title,
            color = Color(0xFFEEEEEE),
            fontSize = 15.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Preview(name = "11 Contents Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun ContentsTocScreenPreview() {
    OpusReaderTheme {
        ContentsTocScreen(engine = FakeReaderEngine())
    }
}
