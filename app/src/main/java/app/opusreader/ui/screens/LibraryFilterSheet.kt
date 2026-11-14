package app.opusreader.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.data.Book
import app.opusreader.data.ContentType
import app.opusreader.data.LibraryFilterState
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusBottomSheetContainer
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusScrim
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun LibraryFilterSheet(
    initialState: LibraryFilterState = LibraryFilterState(),
    allBooks: List<Book> = emptyList(),
    onCloseClick: () -> Unit = {},
    onApplyFilter: (LibraryFilterState) -> Unit = {},
    isFixture: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isFixture) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.fixture_filter_sheet),
                contentDescription = "Filter Sheet",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1201.refPx)
                    .clickable(onClick = onCloseClick)
            )
        }
        return
    }
    var state by remember { mutableStateOf(initialState) }

    val readCount = if (allBooks.isNotEmpty()) allBooks.count { it.isRead } else 16
    val unreadCount = if (allBooks.isNotEmpty()) allBooks.count { !it.isRead } else 59
    val booksCount = if (allBooks.isNotEmpty()) allBooks.count { it.contentType == ContentType.BOOK } else 6
    val samplesCount = if (allBooks.isNotEmpty()) allBooks.count { it.contentType == ContentType.SAMPLE } else 5
    val docsCount = if (allBooks.isNotEmpty()) allBooks.count { it.contentType == ContentType.DOC } else 70

    OpusBottomSheetContainer(
        title = "Filter",
        doneButtonText = "DONE",
        onDoneClick = { onApplyFilter(state) },
        onCloseClick = onCloseClick,
        modifier = modifier
    ) {
        SectionTitle("Read state")

        FilterItemRow(
            label = "Read ($readCount)",
            selected = state.showRead,
            onClick = { state = state.copy(showRead = !state.showRead) }
        )
        FilterItemRow(
            label = "Unread ($unreadCount)",
            selected = state.showUnread,
            onClick = { state = state.copy(showUnread = !state.showUnread) }
        )

        HorizontalDivider(color = OpusDivider, thickness = 1.dp)

        SectionTitle("Content type")

        FilterItemRow(
            label = "Books ($booksCount)",
            selected = state.showBooks,
            onClick = { state = state.copy(showBooks = !state.showBooks) }
        )
        FilterItemRow(
            label = "Samples ($samplesCount)",
            selected = state.showSamples,
            onClick = { state = state.copy(showSamples = !state.showSamples) }
        )
        FilterItemRow(
            label = "Docs ($docsCount)",
            selected = state.showDocs,
            onClick = { state = state.copy(showDocs = !state.showDocs) }
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = OpusTextSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(
            start = OpusSpacing.standard,
            end = OpusSpacing.standard,
            top = OpusSpacing.medium,
            bottom = OpusSpacing.small
        )
    )
}

@Composable
private fun FilterItemRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = OpusSpacing.standard, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = OpusTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = OpusTextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(name = "05 Library Filter Sheet Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun LibraryFilterSheetPreview() {
    OpusReaderTheme {
        LibraryFilterSheet(allBooks = SampleBooks.sampleList)
    }
}
