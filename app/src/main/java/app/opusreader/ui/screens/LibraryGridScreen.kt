package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.ContentType
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.FilterSlidersIcon
import app.opusreader.ui.components.OpusCoverTile
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.components.SortArrowsIcon
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun LibraryGridScreen(
    books: List<Book> = SampleBooks.sampleList,
    onBookClick: (Book) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onBookLongClick: (Book) -> Unit = {},
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

        // Divider: y=[401, 403], 3 px height
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(
                    start = OpusSpacing.gridSideMargin,
                    end = OpusSpacing.gridSideMargin,
                    top = 68.refPx,
                    bottom = 24.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(52.refPx),
                verticalArrangement = Arrangement.spacedBy(OpusSpacing.gridRowGap),
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds()
            ) {
                items(filteredBooks, key = { it.id }) { book ->
                    OpusCoverTile(
                        title = book.title,
                        author = book.author,
                        progressPercent = book.progressPercent,
                        isDownloaded = book.isDownloaded,
                        coverColor = book.coverColor,
                        coverDrawableRes = book.coverDrawableRes,
                        ribbonText = if (book.contentType == ContentType.SAMPLE) "SAMPLE" else null,
                        onClick = { onBookClick(book) },
                        onLongClick = { onBookLongClick(book) }
                    )
                }
            }
        }
    }
}

@Preview(name = "01 Library Grid Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun LibraryGridScreenPreview() {
    OpusReaderTheme {
        LibraryGridScreen()
    }
}
