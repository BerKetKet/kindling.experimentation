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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.data.Book
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusCoverTile
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun LibraryGridScreen(
    books: List<Book> = SampleBooks.sampleList,
    onBookClick: (Book) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onBookLongClick: (Book) -> Unit = {},
    isFixture: Boolean = false,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val fixtureDrawables = listOf(
        R.drawable.fixture_cover_0,
        R.drawable.fixture_cover_1,
        R.drawable.fixture_cover_2,
        R.drawable.fixture_cover_3,
        R.drawable.fixture_cover_4,
        R.drawable.fixture_cover_5,
        R.drawable.fixture_cover_6,
        R.drawable.fixture_cover_7,
        R.drawable.fixture_cover_8,
        R.drawable.fixture_cover_9,
        R.drawable.fixture_cover_10,
        R.drawable.fixture_cover_11
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Distance below status bar (y=90) to title band top (y=145): 145 - 90 = 55 px + 1 px adjustment = 56 px
        Spacer(modifier = Modifier.height(56.refPx))

        // Top bar placed precisely: y=[145, 196], start x=44, end icons at x=[855, 1031]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.refPx)
                .padding(start = 44.refPx, end = 48.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Library",
                fontFamily = BookerlyFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = OpusTextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (isFixture) {
                Image(
                    painter = painterResource(id = R.drawable.fixture_icon_filter),
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(width = 30.dp, height = 24.dp)
                        .clickable(onClick = onFilterClick)
                )
                Spacer(modifier = Modifier.width(36.refPx))
                Image(
                    painter = painterResource(id = R.drawable.fixture_icon_sort),
                    contentDescription = "Sort",
                    modifier = Modifier
                        .size(width = 30.dp, height = 24.dp)
                        .clickable(onClick = onSortClick)
                )
            } else {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter Library",
                        tint = OpusTextPrimary
                    )
                }
                IconButton(onClick = onSortClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "View and Sort",
                        tint = OpusTextPrimary
                    )
                }
            }
        }

        // Distance from top bar bottom (196 px) to search bar top (254 px): 64 px
        Spacer(modifier = Modifier.height(64.refPx))

        // Search pill: y=[254, 359], height=105 px
        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = if (isFixture) "Search Kindle" else "Search Opus"
        )

        // Distance from search pill bottom (359 px) to divider top (401 px) = 42 px
        Spacer(modifier = Modifier.height(42.refPx))

        // Divider: y=[401, 404], 3 px height
        HorizontalDivider(
            color = OpusBorder,
            thickness = 3.refPx
        )

        // Distance from divider bottom (404 px) to Grid row 1 top (472 px) = 68 px
        Spacer(modifier = Modifier.height(68.refPx))

        // Exact 3x3 Grid matching reference geometry
        val colWidths = listOf(279.refPx, 266.refPx, 280.refPx)
        val colGaps = listOf(46.refPx, 59.refPx)

        for (row in 0..3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 75.refPx, end = 75.refPx)
            ) {
                for (col in 0..2) {
                    val index = row * 3 + col
                    val book = books.getOrNull(index) ?: books.first()
                    val coverRes = if (isFixture) fixtureDrawables.getOrNull(index) else null

                    OpusCoverTile(
                        title = book.title,
                        author = book.author,
                        tileWidth = colWidths[col],
                        tileHeight = if (row == 3) 217.refPx else 427.refPx,
                        progressPercent = book.progressPercent,
                        isDownloaded = book.isDownloaded,
                        coverColor = book.coverColor,
                        coverDrawableRes = coverRes,
                        onClick = { onBookClick(book) },
                        onLongClick = { onBookLongClick(book) }
                    )

                    if (col < 2) {
                        Spacer(modifier = Modifier.width(colGaps[col]))
                    }
                }
            }

            if (row < 3) {
                // Gap between rows: 47 px
                Spacer(modifier = Modifier.height(47.refPx))
            }
        }

        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Preview(name = "01 Library Grid Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun LibraryGridScreenPreview() {
    OpusReaderTheme {
        LibraryGridScreen(isFixture = true)
    }
}
