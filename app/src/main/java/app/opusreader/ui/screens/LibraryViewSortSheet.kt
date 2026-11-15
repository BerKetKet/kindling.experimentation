package app.opusreader.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.LibrarySortOrder
import app.opusreader.data.LibraryViewMode
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.refPx

private val SheetBg = Color(0xFF232323)
private val SheetBlue = Color(0xFFA3D7F5)
private val SheetTextPrimary = Color(0xFFEEEEEE)
private val SheetDivider = Color(0xFF404040)

data class ViewSortState(
    val groupBySeries: Boolean = false,
    val viewMode: LibraryViewMode = LibraryViewMode.GRID,
    val sortOrder: LibrarySortOrder = LibrarySortOrder.MOST_RECENT
)

@Composable
fun LibraryViewSortSheet(
    initialState: ViewSortState = ViewSortState(),
    onCloseClick: () -> Unit = {},
    onApplyViewSort: (ViewSortState) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(initialState) }
    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(1519.refPx)
            .clip(shape)
            .background(SheetBg)
    ) {
        // Distance from sheet top (y=758) to Header (y=822): 58 px
        Spacer(modifier = Modifier.height(58.refPx))

        // Header: View & sort / DONE (y=822..853, h=31 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(31.refPx)
                .padding(start = 45.refPx, end = 45.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "View & sort",
                fontFamily = BookerlyFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = SheetTextPrimary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "DONE",
                fontFamily = AmazonEmberFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = SheetBlue,
                modifier = Modifier.clickable { onApplyViewSort(state) }
            )
        }

        // Distance to Divider 1: 71 px
        Spacer(modifier = Modifier.height(71.refPx))

        // Divider 1 (y=918..921, h=3 px)
        HorizontalDivider(color = SheetDivider, thickness = 3.refPx)

        // Group by series toggle (starts 921, text/switch at 974..1026, ends 1079, h=158 px)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(158.refPx)
                .padding(start = 45.refPx, end = 45.refPx),
            contentAlignment = Alignment.TopStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 52.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Group by series",
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = SheetTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                KindleSwitch(
                    checked = state.groupBySeries,
                    onCheckedChange = { state = state.copy(groupBySeries = it) }
                )
            }
        }

        // Divider 2 (y=1079..1082, h=3 px)
        HorizontalDivider(color = SheetDivider, thickness = 3.refPx)

        // View options: Grid, List, Collections (each 147 px)
        ViewOptionRow(
            icon = Icons.Default.GridView,
            label = "Grid",
            topPadding = 46,
            selected = state.viewMode == LibraryViewMode.GRID,
            onClick = { state = state.copy(viewMode = LibraryViewMode.GRID) }
        )
        ViewOptionRow(
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            label = "List",
            topPadding = 45,
            selected = state.viewMode == LibraryViewMode.LIST,
            onClick = { state = state.copy(viewMode = LibraryViewMode.LIST) }
        )
        ViewOptionRow(
            icon = Icons.Default.Folder,
            label = "Collections",
            topPadding = 44,
            selected = state.viewMode == LibraryViewMode.COLLECTIONS,
            onClick = { state = state.copy(viewMode = LibraryViewMode.COLLECTIONS) }
        )

        // Thick section bar (y=1523..1539, h=16 px)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.refPx)
                .background(SheetDivider)
        )

        // Sort section header (y=1599..1628, h=29 px)
        Spacer(modifier = Modifier.height(51.refPx))
        Text(
            text = "Sort",
            fontFamily = BookerlyFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = SheetTextPrimary,
            modifier = Modifier
                .padding(start = 45.refPx)
                .height(29.refPx)
        )
        Spacer(modifier = Modifier.height(67.refPx))

        // Divider 3 (y=1686..1689, h=3 px)
        HorizontalDivider(color = SheetDivider, thickness = 3.refPx)

        // Sort options: Most Recent, Title, Author A-Z, Author Z-A (each 147 px)
        SortOptionRow(
            label = LibrarySortOrder.MOST_RECENT.displayName,
            topPadding = 46,
            selected = state.sortOrder == LibrarySortOrder.MOST_RECENT,
            onClick = { state = state.copy(sortOrder = LibrarySortOrder.MOST_RECENT) }
        )
        SortOptionRow(
            label = LibrarySortOrder.TITLE.displayName,
            topPadding = 47,
            selected = state.sortOrder == LibrarySortOrder.TITLE,
            onClick = { state = state.copy(sortOrder = LibrarySortOrder.TITLE) }
        )
        SortOptionRow(
            label = LibrarySortOrder.AUTHOR_AZ.displayName,
            topPadding = 48,
            selected = state.sortOrder == LibrarySortOrder.AUTHOR_AZ,
            onClick = { state = state.copy(sortOrder = LibrarySortOrder.AUTHOR_AZ) }
        )
        SortOptionRow(
            label = LibrarySortOrder.AUTHOR_ZA.displayName,
            topPadding = 48,
            selected = state.sortOrder == LibrarySortOrder.AUTHOR_ZA,
            onClick = { state = state.copy(sortOrder = LibrarySortOrder.AUTHOR_ZA) }
        )

        Spacer(modifier = Modifier.height(25.refPx))
    }
}

@Composable
private fun KindleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackWidth = 94.refPx
    val trackHeight = 52.refPx
    val thumbDiameter = 44.refPx
    val thumbPadding = 4.refPx

    Box(
        modifier = modifier
            .size(width = trackWidth, height = trackHeight)
            .clip(RoundedCornerShape(26.refPx))
            .background(if (checked) SheetBlue else Color(0xFF333333))
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = thumbPadding),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbDiameter)
                .clip(RoundedCornerShape(22.refPx))
                .background(if (checked) Color.White else Color(0xFF9E9E9E))
        )
    }
}

@Composable
private fun ViewOptionRow(
    icon: ImageVector,
    label: String,
    topPadding: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(147.refPx)
            .clickable(onClick = onClick)
            .padding(start = 45.refPx, end = 45.refPx),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SheetTextPrimary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(35.refPx))

            Text(
                text = label,
                fontFamily = AmazonEmberFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = SheetTextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = SheetBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SortOptionRow(
    label: String,
    topPadding: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(147.refPx)
            .clickable(onClick = onClick)
            .padding(start = 45.refPx, end = 45.refPx),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontFamily = AmazonEmberFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = SheetTextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = SheetBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(name = "06 Library View and Sort Sheet Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun LibraryViewSortSheetPreview() {
    OpusReaderTheme {
        LibraryViewSortSheet()
    }
}
