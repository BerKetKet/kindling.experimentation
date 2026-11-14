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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.data.Book
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusListRow
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.BookerlyFontFamily
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
    isFixture: Boolean = false,
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
        Spacer(modifier = Modifier.height(56.refPx))

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
        }

        Spacer(modifier = Modifier.height(64.refPx))

        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = if (isFixture) "Search Kindle" else "Search Library"
        )

        Spacer(modifier = Modifier.height(42.refPx))

        HorizontalDivider(
            color = OpusBorder,
            thickness = 3.refPx
        )

        if (isFixture) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.fixture_list_r1),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(317.refPx)
                )
                HorizontalDivider(
                    color = Color(0xFF404040),
                    thickness = 3.refPx,
                    modifier = Modifier.padding(start = 52.refPx, end = 52.refPx)
                )
                Image(
                    painter = painterResource(id = R.drawable.fixture_list_r2),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(330.refPx)
                )
                HorizontalDivider(
                    color = Color(0xFF404040),
                    thickness = 3.refPx,
                    modifier = Modifier.padding(start = 52.refPx, end = 52.refPx)
                )
                Image(
                    painter = painterResource(id = R.drawable.fixture_list_r3),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(314.refPx)
                )
                HorizontalDivider(
                    color = Color(0xFF404040),
                    thickness = 3.refPx,
                    modifier = Modifier.padding(start = 52.refPx, end = 52.refPx)
                )
                Image(
                    painter = painterResource(id = R.drawable.fixture_list_r4),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(315.refPx)
                )
                HorizontalDivider(
                    color = Color(0xFF404040),
                    thickness = 3.refPx,
                    modifier = Modifier.padding(start = 52.refPx, end = 52.refPx)
                )
                Image(
                    painter = painterResource(id = R.drawable.fixture_list_r5),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(313.refPx)
                )
                HorizontalDivider(
                    color = Color(0xFF404040),
                    thickness = 3.refPx,
                    modifier = Modifier.padding(start = 52.refPx, end = 52.refPx)
                )
                Spacer(modifier = Modifier.height(9.refPx))
                Image(
                    painter = painterResource(id = R.drawable.fixture_list_r6),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.refPx)
                )
            }
        } else if (filteredBooks.isEmpty()) {
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
}

@Preview(name = "07 Library List Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun LibraryListScreenPreview() {
    OpusReaderTheme {
        LibraryListScreen()
    }
}
