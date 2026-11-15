package app.opusreader.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.ContentType
import app.opusreader.data.LibraryFilterState
import app.opusreader.data.SampleBooks
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusScrim
import app.opusreader.ui.theme.OpusSheetActionBlue
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

@Composable
fun LibraryFilterSheet(
    initialState: LibraryFilterState = LibraryFilterState(),
    allBooks: List<Book> = emptyList(),
    onCloseClick: () -> Unit = {},
    onApplyFilter: (LibraryFilterState) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(initialState) }
    var expandRead by remember { mutableStateOf(true) }
    var expandType by remember { mutableStateOf(true) }
    val isDark = isOpusDarkTheme

    val readCount = 16
    val unreadCount = 60
    val booksCount = 6
    val samplesCount = 2
    val docsCount = 2

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OpusScrim)
            .clickable(onClick = { onApplyFilter(state) }),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            // Sheet surface: y=[1076, 2276], h=1201 px
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1201.refPx)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(if (isDark) OpusSurface else Color.White)
            ) {
                // Header row: Filter + DONE at y=[1140, 1170]
                Spacer(modifier = Modifier.height(64.refPx))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(31.refPx)
                        .padding(horizontal = 45.refPx),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter",
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            platformStyle = NoPaddingPlatformStyle
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "DONE",
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusSheetActionBlue,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            platformStyle = NoPaddingPlatformStyle
                        ),
                        modifier = Modifier.clickable { onApplyFilter(state) }
                    )
                }

                // Read state accordion header at y=[1250, 1279]
                Spacer(modifier = Modifier.height(80.refPx))
                AccordionSectionHeader(
                    title = "Read state",
                    expanded = expandRead,
                    onClick = { expandRead = !expandRead }
                )
                Spacer(modifier = Modifier.height(61.refPx))
                HorizontalDivider(color = OpusBorder, thickness = 6.refPx)

                if (expandRead) {
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
                }

                // Content type accordion header at y=[1705, 1739]
                Spacer(modifier = Modifier.height(60.refPx))
                AccordionSectionHeader(
                    title = "Content type",
                    expanded = expandType,
                    onClick = { expandType = !expandType }
                )
                Spacer(modifier = Modifier.height(56.refPx))
                HorizontalDivider(color = OpusBorder, thickness = 6.refPx)

                if (expandType) {
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

            // Bottom gesture bar area: y=[2277, 2339] (63 px)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.refPx)
                    .background(if (isDark) Color(0xFF080808) else Color.Black)
            )
        }
    }
}

@Composable
private fun AccordionSectionHeader(
    title: String,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.refPx)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.refPx),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = OpusSheetActionBlue,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = TextStyle(
                fontFamily = AmazonEmberFontFamily,
                color = OpusSheetActionBlue,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium,
                platformStyle = NoPaddingPlatformStyle
            )
        )
    }
}

@Composable
private fun FilterItemRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Exact 150.refPx row pitch (60px top gap + 34px text + 55px bottom gap + 3px divider)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Spacer(modifier = Modifier.height(60.refPx))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.refPx)
                .padding(horizontal = 45.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 14.sp,
                    platformStyle = NoPaddingPlatformStyle
                ),
                modifier = Modifier.weight(1f)
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = OpusAccentBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(55.refPx))
        HorizontalDivider(color = OpusDivider, thickness = 3.refPx)
    }
}

@Preview(name = "05 Library Filter Sheet Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun LibraryFilterSheetPreview() {
    OpusReaderTheme {
        LibraryFilterSheet(allBooks = SampleBooks.sampleList)
    }
}
