package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.ContentType
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.FilterSlidersIcon
import app.opusreader.ui.components.OpusListRow
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.components.SortArrowsIcon
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun LibraryListScreen(
    books: List<Book> = SampleBooks.sampleList,
    onBookClick: (Book) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onDownloadClick: (Book) -> Unit = {},
    onMoreOptionsClick: (Book) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredBooks = remember(books, searchQuery) {
        if (searchQuery.isBlank()) books
        else books.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                it.author.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
    ) {
        OpusTopBar(
            title = "Library",
            actions = {
                IconButton(onClick = onFilterClick) {
                    FilterSlidersIcon()
                }
                IconButton(onClick = onSortClick) {
                    SortArrowsIcon()
                }
            }
        )

        // Search pill: y=[254, 358], h=105 px
        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Kindle"
        )

        // Gap from search pill bottom (358) to divider top (401): 42 px
        Spacer(modifier = Modifier.height(42.refPx))

        // Main divider: y=[401, 403], 3 px height
        HorizontalDivider(
            color = OpusBorder,
            thickness = 3.refPx
        )

        if (filteredBooks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No items match your filter",
                    color = OpusTextSecondary,
                    fontSize = 15.sp
                )
            }
        } else {
            // Clipped strictly to the Scaffold body above the bottom nav bar (eliminates the 28px gesture bar offset!)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .clipToBounds()
            ) {
                items(filteredBooks, key = { it.id }) { book ->
                    OpusListRow(
                        title = book.title,
                        author = book.author,
                        metaInfo = book.fileSize,
                        progressPercent = book.progressPercent,
                        isDownloaded = book.isDownloaded,
                        isSample = book.contentType == ContentType.SAMPLE,
                        coverColor = book.coverColor,
                        coverDrawableRes = book.coverDrawableRes,
                        onClick = { onBookClick(book) },
                        onMoreClick = { onMoreOptionsClick(book) },
                        onDownloadClick = { onDownloadClick(book) }
                    )
                }
            }
        }
    }
}

@Preview(name = "07 Library List Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun LibraryListScreenPreview() {
    OpusReaderTheme {
        LibraryListScreen()
    }
}
