package app.opusreader.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.data.LibrarySortOrder
import app.opusreader.data.LibraryViewMode
import app.opusreader.ui.components.OpusBottomSheetContainer
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

data class ViewSortState(
    val groupBySeries: Boolean = true,
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

    OpusBottomSheetContainer(
        title = "View",
        doneButtonText = "DONE",
        onDoneClick = { onApplyViewSort(state) },
        onCloseClick = onCloseClick,
        modifier = modifier
    ) {
        // Group by series toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Group by Series",
                color = OpusTextPrimary,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = state.groupBySeries,
                onCheckedChange = { state = state.copy(groupBySeries = it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = OpusAccentBlue,
                    uncheckedThumbColor = OpusTextSecondary,
                    uncheckedTrackColor = Color(0xFF333333)
                )
            )
        }

        HorizontalDivider(color = OpusDivider, thickness = 1.dp)

        // View options
        ViewOptionRow(
            icon = Icons.Default.GridView,
            label = "Grid",
            selected = state.viewMode == LibraryViewMode.GRID,
            onClick = { state = state.copy(viewMode = LibraryViewMode.GRID) }
        )
        ViewOptionRow(
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            label = "List",
            selected = state.viewMode == LibraryViewMode.LIST,
            onClick = { state = state.copy(viewMode = LibraryViewMode.LIST) }
        )
        ViewOptionRow(
            icon = Icons.Default.Folder,
            label = "Collections",
            selected = state.viewMode == LibraryViewMode.COLLECTIONS,
            onClick = { state = state.copy(viewMode = LibraryViewMode.COLLECTIONS) }
        )

        HorizontalDivider(color = OpusDivider, thickness = 1.dp)

        // Sort section
        Text(
            text = "Sort",
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

        SortOptionRow(
            label = LibrarySortOrder.MOST_RECENT.displayName,
            selected = state.sortOrder == LibrarySortOrder.MOST_RECENT,
            onClick = { state = state.copy(sortOrder = LibrarySortOrder.MOST_RECENT) }
        )
        SortOptionRow(
            label = LibrarySortOrder.TITLE.displayName,
            selected = state.sortOrder == LibrarySortOrder.TITLE,
            onClick = { state = state.copy(sortOrder = LibrarySortOrder.TITLE) }
        )
        SortOptionRow(
            label = LibrarySortOrder.AUTHOR_AZ.displayName,
            selected = state.sortOrder == LibrarySortOrder.AUTHOR_AZ,
            onClick = { state = state.copy(sortOrder = LibrarySortOrder.AUTHOR_AZ) }
        )
        SortOptionRow(
            label = LibrarySortOrder.AUTHOR_ZA.displayName,
            selected = state.sortOrder == LibrarySortOrder.AUTHOR_ZA,
            onClick = { state = state.copy(sortOrder = LibrarySortOrder.AUTHOR_ZA) }
        )
    }
}

@Composable
private fun ViewOptionRow(
    icon: ImageVector,
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
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OpusTextPrimary,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            color = OpusTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = OpusSpacing.medium)
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

@Composable
private fun SortOptionRow(
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

@Preview(name = "06 Library View and Sort Sheet Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun LibraryViewSortSheetPreview() {
    OpusReaderTheme {
        LibraryViewSortSheet()
    }
}
