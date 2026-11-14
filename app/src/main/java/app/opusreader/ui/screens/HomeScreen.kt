package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusCoverTile
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusBookmarkRed
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun HomeScreen(
    books: List<Book> = SampleBooks.sampleList,
    onBookClick: (Book) -> Unit = {},
    onSeeAllLibraryClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedChip by remember { mutableStateOf("Explore") }
    val chips = listOf("Explore", "All", "Fiction", "Nonfiction", "Fantasy", "Classics")

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
            placeholder = "Search Opus"
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

        // "From Your Library" shelf
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "From Your Library",
                color = OpusTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "See All",
                color = OpusAccentBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onSeeAllLibraryClick() }
            )
        }

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

        // Gutenberg Discovery shelf
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Project Gutenberg Classics",
                color = OpusTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(OpusSpacing.small))

        LazyRow(
            contentPadding = PaddingValues(horizontal = OpusSpacing.standard),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
        ) {
            items(books.drop(2), key = { it.id }) { book ->
                OpusCoverTile(
                    title = book.title,
                    author = book.author,
                    progressPercent = 0,
                    coverColor = book.coverColor,
                    onClick = { onBookClick(book) }
                )
            }
        }
    }
}

@Preview(name = "02 Home Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun HomeScreenPreview() {
    OpusReaderTheme {
        HomeScreen()
    }
}
