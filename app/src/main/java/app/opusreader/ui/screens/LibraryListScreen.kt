package app.opusreader.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusListRow
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.LocalFixtureTrigger
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

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
    val fixtureTrigger = LocalFixtureTrigger.current
    val listState = rememberLazyListState()

    LaunchedEffect(fixtureTrigger) {
        listState.scrollToItem(0)
    }

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
        // Distance below status bar (y=90) to title band top (y=145): 54 px
        Spacer(modifier = Modifier.height(54.refPx))

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

        // Distance from top bar bottom (196 px) to search bar top (254 px): 66 px
        Spacer(modifier = Modifier.height(66.refPx))

        // Search pill: y=[254, 359], height=105 px
        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Opus"
        )

        // Distance from search pill bottom (359 px) to divider top (401 px) = 42 px
        Spacer(modifier = Modifier.height(42.refPx))

        // Divider: y=[401, 404], 3 px height
        HorizontalDivider(
            color = OpusBorder,
            thickness = 3.refPx
        )

        if (filteredBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No items match your filter",
                    color = OpusTextSecondary,
                    fontSize = 15.sp
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 63.dp)
            ) {
                items(filteredBooks, key = { it.id }) { book ->
                    OpusListRow(
                        title = book.title,
                        author = book.author,
                        metaInfo = book.fileSize,
                        progressPercent = book.progressPercent,
                        isDownloaded = book.isDownloaded,
                        coverColor = book.coverColor,
                        coverDrawableRes = book.coverDrawableRes ?: 0,
                        onClick = { onBookClick(book) },
                        onMoreClick = { onMoreOptionsClick(book) }
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
