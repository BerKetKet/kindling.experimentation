package app.opusreader.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.ContentType
import app.opusreader.data.LibraryFilterState
import app.opusreader.data.SampleBooks
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.refPx

private val FilterSheetBg = Color(0xFF232323)
private val FilterBlue = Color(0xFFA3D7F5)
private val FilterTextPrimary = Color(0xFFEEEEEE)
private val FilterSectionDivider = Color(0xFF404040)
private val FilterItemDivider = Color(0xFF3E3E3E)

@Composable
fun LibraryFilterSheet(
    initialState: LibraryFilterState = LibraryFilterState(),
    allBooks: List<Book> = emptyList(),
    onCloseClick: () -> Unit = {},
    onApplyFilter: (LibraryFilterState) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(initialState) }

    val readCount = if (allBooks.isNotEmpty()) allBooks.count { it.isRead } else 16
    val unreadCount = if (allBooks.isNotEmpty()) allBooks.count { !it.isRead } else 59
    val booksCount = if (allBooks.isNotEmpty()) allBooks.count { it.contentType == ContentType.BOOK } else 0
    val samplesCount = if (allBooks.isNotEmpty()) allBooks.count { it.contentType == ContentType.SAMPLE } else 5
    val docsCount = if (allBooks.isNotEmpty()) allBooks.count { it.contentType == ContentType.DOC } else 70

    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(1201.refPx)
            .clip(shape)
            .background(FilterSheetBg)
    ) {
        // Distance from sheet top (y=1076) to Header (y=1140): 58 px
        Spacer(modifier = Modifier.height(58.refPx))

        // Header: Filter & DONE (y=1140..1170, h=30 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.refPx)
                .padding(start = 45.refPx, end = 45.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Filter",
                fontFamily = BookerlyFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = FilterTextPrimary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "DONE",
                fontFamily = AmazonEmberFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = FilterBlue,
                modifier = Modifier.clickable { onApplyFilter(state) }
            )
        }

        // Distance from Header (1170) to Section 1 (1250): 79 px
        Spacer(modifier = Modifier.height(79.refPx))

        // Section 1: ∧ Read state (y=1250..1279, h=29 px)
        FilterSectionHeader(title = "Read state", height = 29.refPx)

        // Distance to Divider: 68 px
        Spacer(modifier = Modifier.height(68.refPx))

        // Divider below Section 1 (y=1340..1345, h=5 px)
        HorizontalDivider(color = FilterSectionDivider, thickness = 5.refPx)

        // Row 1: Read (16) (starts y=1345, text y=1405..1438, ends 1493, h=148 px)
        FilterItemRow(
            label = "Read ($readCount)",
            selected = state.showRead,
            onClick = { state = state.copy(showRead = !state.showRead) }
        )

        // Divider (y=1493..1495, h=2 px)
        HorizontalDivider(color = FilterItemDivider, thickness = 2.refPx)

        // Row 2: Unread (59) (starts y=1495, text y=1555..1588, ends 1643, h=148 px)
        FilterItemRow(
            label = "Unread ($unreadCount)",
            selected = state.showUnread,
            onClick = { state = state.copy(showUnread = !state.showUnread) }
        )

        // Divider (y=1643..1645, h=2 px)
        HorizontalDivider(color = FilterItemDivider, thickness = 2.refPx)

        // Distance to Section 2 (1705): 51 px
        Spacer(modifier = Modifier.height(51.refPx))

        // Section 2: ∧ Content type (y=1705..1739, h=34 px)
        FilterSectionHeader(title = "Content type", height = 34.refPx)

        // Distance to Divider: 65 px
        Spacer(modifier = Modifier.height(65.refPx))

        // Divider below Section 2 (y=1795..1800, h=5 px)
        HorizontalDivider(color = FilterSectionDivider, thickness = 5.refPx)

        // Row 3: Books (0) (starts y=1800, text y=1860..1893, ends 1948, h=148 px)
        FilterItemRow(
            label = "Books ($booksCount)",
            selected = state.showBooks,
            onClick = { state = state.copy(showBooks = !state.showBooks) }
        )

        // Divider (y=1948..1950, h=2 px)
        HorizontalDivider(color = FilterItemDivider, thickness = 2.refPx)

        // Row 4: Samples (5) (starts y=1950, text y=2010..2045, ends 2098, h=148 px)
        FilterItemRow(
            label = "Samples ($samplesCount)",
            selected = state.showSamples,
            onClick = { state = state.copy(showSamples = !state.showSamples) }
        )

        // Divider (y=2098..2100, h=2 px)
        HorizontalDivider(color = FilterItemDivider, thickness = 2.refPx)

        // Row 5: Docs (70) (starts y=2100, text y=2160..2193, ends 2248, h=148 px)
        FilterItemRow(
            label = "Docs ($docsCount)",
            selected = state.showDocs,
            onClick = { state = state.copy(showDocs = !state.showDocs) }
        )

        // Divider (y=2248..2250, h=2 px)
        HorizontalDivider(color = FilterItemDivider, thickness = 2.refPx)

        // Distance to sheet bottom: 25 px
        Spacer(modifier = Modifier.height(25.refPx))
    }
}

@Composable
private fun FilterSectionHeader(title: String, height: androidx.compose.ui.unit.Dp = 30.refPx) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(start = 24.refPx),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(20.refPx, 20.refPx)) {
            val strokeWidth = 4.refPx.toPx()
            val path = Path().apply {
                moveTo(strokeWidth, size.height - strokeWidth)
                lineTo(size.width / 2f, strokeWidth)
                lineTo(size.width - strokeWidth, size.height - strokeWidth)
            }
            drawPath(
                path = path,
                color = FilterBlue,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        Spacer(modifier = Modifier.width(12.refPx))

        Text(
            text = title,
            fontFamily = AmazonEmberFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.5.sp,
            color = FilterBlue
        )
    }
}

@Composable
private fun FilterItemRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(148.refPx)
            .clickable(onClick = onClick)
            .padding(start = 45.refPx, end = 45.refPx),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 49.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontFamily = AmazonEmberFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = FilterTextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = FilterBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
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
