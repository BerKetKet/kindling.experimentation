package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.BookRepository
import app.opusreader.data.CatalogRepository
import app.opusreader.data.FakeBookRepository
import app.opusreader.data.FakeCatalogRepository
import app.opusreader.data.GutenbergBook
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusCoverTile
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusBookmarkRed
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    books: List<Book> = SampleBooks.sampleList,
    catalogRepository: CatalogRepository = remember { FakeCatalogRepository() },
    bookRepository: BookRepository? = null,
    onBookClick: (Book) -> Unit = {},
    onSeeAllLibraryClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedChip by remember { mutableStateOf("Explore") }
    val chips = listOf("Explore", "All", "Fiction", "Fantasy", "Classics")

    val popularBooks by catalogRepository.popularBooks.collectAsState()
    val topicBooks by catalogRepository.topicBooks.collectAsState()
    val searchResults by catalogRepository.searchResults.collectAsState()

    var selectedGutenbergBook by remember { mutableStateOf<GutenbergBook?>(null) }

    LaunchedEffect(searchQuery) {
        catalogRepository.searchCatalog(searchQuery)
    }

    if (selectedGutenbergBook != null) {
        val gutenberg = selectedGutenbergBook!!
        val isAlreadyInLibrary = books.any { it.id == "gutenberg_${gutenberg.id}" }
        GutenbergBookDetailsDialog(
            book = gutenberg,
            isAlreadyInLibrary = isAlreadyInLibrary,
            onDismiss = { selectedGutenbergBook = null },
            onDownload = {
                if (bookRepository != null) {
                    coroutineScope.launch {
                        catalogRepository.downloadBookToLibrary(gutenberg, bookRepository)
                    }
                }
                selectedGutenbergBook = null
            },
            onRead = {
                val foundBook = books.firstOrNull { it.id == "gutenberg_${gutenberg.id}" }
                if (foundBook != null) {
                    onBookClick(foundBook)
                }
                selectedGutenbergBook = null
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        OpusTopBar(
            title = "Home",
            actions = {
                Box {
                    IconButton(onClick = onNotificationsClick) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = OpusTextPrimary
                        )
                    }
                    // Red notification badge dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 10.dp, end = 10.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(OpusBookmarkRed)
                    )
                }
            }
        )

        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Opus & Gutenberg"
        )

        // Topic chips row
        LazyRow(
            contentPadding = PaddingValues(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.small)
        ) {
            items(chips) { chip ->
                val isSelected = chip == selectedChip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) OpusTextPrimary else OpusSurfaceVariant)
                        .clickable { selectedChip = chip }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = chip,
                        color = if (isSelected) OpusBackground else OpusTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.medium))

        // Search Results shelf (when search query is present)
        if (searchQuery.isNotBlank()) {
            ShelfHeader(title = "Search Results (${searchResults.size})")
            Spacer(modifier = Modifier.height(OpusSpacing.small))
            if (searchResults.isEmpty()) {
                Text(
                    text = "No books found matching \"$searchQuery\"",
                    color = OpusTextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small)
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = OpusSpacing.standard),
                    horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
                ) {
                    items(searchResults, key = { it.id }) { book ->
                        OpusCoverTile(
                            title = book.title,
                            author = book.authors.firstOrNull() ?: "",
                            progressPercent = 0,
                            coverColor = book.coverColor,
                            onClick = { selectedGutenbergBook = book }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(OpusSpacing.large))
        }

        // "From Your Library" shelf
        ShelfHeader(
            title = "From Your Library",
            actionText = "See All",
            onActionClick = onSeeAllLibraryClick
        )

        Spacer(modifier = Modifier.height(OpusSpacing.small))

        LazyRow(
            contentPadding = PaddingValues(horizontal = OpusSpacing.standard),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
        ) {
            items(books.take(5), key = { it.id }) { book ->
                OpusCoverTile(
                    title = book.title,
                    author = book.author,
                    progressPercent = book.progressPercent,
                    coverColor = book.coverColor,
                    onClick = { onBookClick(book) }
                )
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.xlarge))

        // Gutenberg Shelves based on Chip selection
        if (selectedChip == "Explore" || selectedChip == "All") {
            // Popular shelf
            ShelfHeader(title = "Project Gutenberg Popular")
            Spacer(modifier = Modifier.height(OpusSpacing.small))
            LazyRow(
                contentPadding = PaddingValues(horizontal = OpusSpacing.standard),
                horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
            ) {
                items(popularBooks, key = { it.id }) { book ->
                    OpusCoverTile(
                        title = book.title,
                        author = book.authors.firstOrNull() ?: "",
                        progressPercent = 0,
                        coverColor = book.coverColor,
                        onClick = { selectedGutenbergBook = book }
                    )
                }
            }

            Spacer(modifier = Modifier.height(OpusSpacing.xlarge))

            // Fiction shelf
            val fictionBooks = topicBooks["Fiction"] ?: emptyList()
            if (fictionBooks.isNotEmpty()) {
                ShelfHeader(title = "Gutenberg Fiction")
                Spacer(modifier = Modifier.height(OpusSpacing.small))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = OpusSpacing.standard),
                    horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
                ) {
                    items(fictionBooks, key = { it.id }) { book ->
                        OpusCoverTile(
                            title = book.title,
                            author = book.authors.firstOrNull() ?: "",
                            progressPercent = 0,
                            coverColor = book.coverColor,
                            onClick = { selectedGutenbergBook = book }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(OpusSpacing.xlarge))
            }

            // Classics shelf
            val classicsBooks = topicBooks["Classics"] ?: emptyList()
            if (classicsBooks.isNotEmpty()) {
                ShelfHeader(title = "Gutenberg Classics")
                Spacer(modifier = Modifier.height(OpusSpacing.small))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = OpusSpacing.standard),
                    horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
                ) {
                    items(classicsBooks, key = { it.id }) { book ->
                        OpusCoverTile(
                            title = book.title,
                            author = book.authors.firstOrNull() ?: "",
                            progressPercent = 0,
                            coverColor = book.coverColor,
                            onClick = { selectedGutenbergBook = book }
                        )
                    }
                }
            }
        } else {
            // Specific topic shelf
            val topicList = topicBooks[selectedChip] ?: emptyList()
            ShelfHeader(title = "Gutenberg $selectedChip")
            Spacer(modifier = Modifier.height(OpusSpacing.small))
            if (topicList.isEmpty()) {
                Text(
                    text = "No books found for $selectedChip",
                    color = OpusTextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = OpusSpacing.standard)
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = OpusSpacing.standard),
                    horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
                ) {
                    items(topicList, key = { it.id }) { book ->
                        OpusCoverTile(
                            title = book.title,
                            author = book.authors.firstOrNull() ?: "",
                            progressPercent = 0,
                            coverColor = book.coverColor,
                            onClick = { selectedGutenbergBook = book }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShelfHeader(
    title: String,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = OpusSpacing.standard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = OpusTextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        if (actionText != null) {
            Text(
                text = actionText,
                color = OpusAccentBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GutenbergBookDetailsDialog(
    book: GutenbergBook,
    isAlreadyInLibrary: Boolean,
    onDismiss: () -> Unit,
    onDownload: () -> Unit,
    onRead: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Book Details",
                color = OpusTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
                ) {
                    // Cover Preview
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(115.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(book.coverColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = book.title.take(16),
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(6.dp),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = book.title,
                            color = OpusTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = book.authors.joinToString(", ").ifEmpty { "Project Gutenberg" },
                            color = OpusTextSecondary,
                            fontSize = 13.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Downloads: %,d".format(book.downloadCount),
                            color = OpusAccentBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(OpusSpacing.medium))

                Text(
                    text = "Subjects & Topics",
                    color = OpusTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    book.subjects.take(4).forEach { subject ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(OpusSurfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = subject,
                                color = OpusTextPrimary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(OpusSpacing.medium))

                Text(
                    text = "Source: Project Gutenberg (Public Domain)",
                    color = OpusTextSecondary.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        },
        confirmButton = {
            if (isAlreadyInLibrary) {
                Button(
                    onClick = onRead,
                    colors = ButtonDefaults.buttonColors(containerColor = OpusAccentBlue)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("READ NOW", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onDownload,
                    colors = ButtonDefaults.buttonColors(containerColor = OpusAccentBlue)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("DOWNLOAD EPUB", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE", color = OpusTextSecondary)
            }
        },
        containerColor = OpusSurface
    )
}

@Preview(name = "02 Home Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun HomeScreenPreview() {
    OpusReaderTheme {
        HomeScreen()
    }
}
