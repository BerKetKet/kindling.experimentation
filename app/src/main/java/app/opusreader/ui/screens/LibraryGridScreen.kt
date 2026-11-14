package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import app.opusreader.ui.components.OpusCoverTile
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary

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
        )

        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Library"
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                start = OpusSpacing.standard,
                end = OpusSpacing.standard,
                top = OpusSpacing.small,
                bottom = 90.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(OpusSpacing.standard),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredBooks, key = { it.id }) { book ->
                OpusCoverTile(
                    title = book.title,
                    author = book.author,
                    progressPercent = book.progressPercent,
                    isDownloaded = book.isDownloaded,
                    coverColor = book.coverColor,
                    onClick = { onBookClick(book) },
                    onLongClick = { onBookLongClick(book) }
                )
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
