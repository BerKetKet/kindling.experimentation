package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.opusreader.data.Book
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusListRow
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextPrimary

@Composable
fun LibraryListScreen(
    books: List<Book> = SampleBooks.sampleList,
    onBookClick: (Book) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
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
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = OpusTextPrimary
                    )
                }
                IconButton(onClick = onSortClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort and View",
                        tint = OpusTextPrimary
                    )
                }
            }
        )

        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Library"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) {
            items(filteredBooks, key = { it.id }) { book ->
                OpusListRow(
                    title = book.title,
                    author = book.author,
                    metaInfo = book.fileSize,
                    progressPercent = book.progressPercent,
                    isDownloaded = book.isDownloaded,
                    coverColor = book.coverColor,
                    onClick = { onBookClick(book) },
                    onMoreClick = { onMoreOptionsClick(book) }
                )
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
