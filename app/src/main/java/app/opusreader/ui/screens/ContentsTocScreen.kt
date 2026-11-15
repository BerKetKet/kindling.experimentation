package app.opusreader.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.engine.TocItem
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun ContentsTocScreen(
    engine: ReaderEngine,
    bookTitle: String = "Gardens of the Moon",
    bookAuthor: String = "Steven Erikson",
    onCloseClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tocList by engine.tableOfContents.collectAsState()
    val currentLocation by engine.currentLocation.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusSurface)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookTitle,
                    color = OpusTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = bookAuthor,
                    color = OpusTextSecondary,
                    fontSize = 14.sp
                )
            }

            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Table of Contents",
                    tint = OpusTextPrimary
                )
            }
        }

        HorizontalDivider(color = OpusDivider, thickness = 1.dp)

        // Chapter list
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tocList, key = { it.id }) { item ->
                TocRow(
                    item = item,
                    isCurrent = item.title == currentLocation.chapterTitle,
                    onClick = {
                        engine.jumpToLocation(item.targetLocation)
                        onCloseClick()
                    }
                )
                item.children.forEach { child ->
                    TocRow(
                        item = child,
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
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                start = if (item.level > 0) 36.dp else OpusSpacing.standard,
                end = OpusSpacing.standard,
                top = 14.dp,
                bottom = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isCurrent) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 18.dp)
                    .background(OpusAccentBlue)
            )
            Spacer(modifier = Modifier.width(OpusSpacing.small))
        }

        Text(
            text = item.title,
            color = if (isCurrent) OpusTextPrimary else OpusTextSecondary,
            fontSize = 15.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
    }
    HorizontalDivider(color = OpusDivider.copy(alpha = 0.3f), thickness = 0.5.dp)
}

@Preview(name = "11 Contents TOC Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun ContentsTocScreenPreview() {
    OpusReaderTheme {
        ContentsTocScreen(engine = FakeReaderEngine())
    }
}
