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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GridOn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.LibrarySortOrder
import app.opusreader.data.LibraryViewMode
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusScrim
import app.opusreader.ui.theme.OpusSheetActionBlue
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

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
    val isDark = isOpusDarkTheme

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OpusScrim)
            .clickable(onClick = { onApplyViewSort(state) }),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            // Sheet surface: y=[758, 2276], h=1519 px
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1519.refPx)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(if (isDark) OpusSurface else Color.White)
            ) {
                // Header row: View + DONE at y=[822, 852] (64 px from top 758)
                Spacer(modifier = Modifier.height(64.refPx))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(31.refPx)
                        .padding(horizontal = 44.refPx),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View",
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
                        modifier = Modifier.clickable { onApplyViewSort(state) }
                    )
                }

                // Divider 1 at y=[918, 920] (66 px gap)
                Spacer(modifier = Modifier.height(66.refPx))
                HorizontalDivider(color = OpusBorder, thickness = 3.refPx)

                // Group by Series row at y=[974, 1026] (54 px gap, 53 px height)
                Spacer(modifier = Modifier.height(54.refPx))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(53.refPx)
                        .padding(horizontal = 44.refPx),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Group by Series",
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusTextPrimary,
                            fontSize = 14.sp,
                            platformStyle = NoPaddingPlatformStyle
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = state.groupBySeries,
                        onCheckedChange = { state = state.copy(groupBySeries = it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = OpusAccentBlue,
                            uncheckedThumbColor = OpusTextSecondary,
                            uncheckedTrackColor = Color(0xFF3A3B3C)
                        )
                    )
                }

                // Divider 2 at y=[1079, 1081] (53 px gap)
                Spacer(modifier = Modifier.height(53.refPx))
                HorizontalDivider(color = OpusBorder, thickness = 3.refPx)

                // Grid (y=[1134, 1176]), List (y=[1284, 1320]), Collections (y=[1428, 1470])
                Spacer(modifier = Modifier.height(53.refPx))
                ViewOptionRow(
                    icon = Icons.Default.GridOn,
                    label = "Grid",
                    selected = state.viewMode == LibraryViewMode.GRID,
                    onClick = { state = state.copy(viewMode = LibraryViewMode.GRID) }
                )
                Spacer(modifier = Modifier.height(104.refPx))
                ViewOptionRow(
                    icon = Icons.AutoMirrored.Filled.FormatListBulleted,
                    label = "List",
                    selected = state.viewMode == LibraryViewMode.LIST,
                    onClick = { state = state.copy(viewMode = LibraryViewMode.LIST) }
                )
                Spacer(modifier = Modifier.height(104.refPx))
                ViewOptionRow(
                    icon = Icons.Default.GridView,
                    label = "Collections",
                    selected = state.viewMode == LibraryViewMode.COLLECTIONS,
                    onClick = { state = state.copy(viewMode = LibraryViewMode.COLLECTIONS) }
                )

                // Thick section divider at y=[1523, 1538] (53 px gap, 16 px height)
                Spacer(modifier = Modifier.height(53.refPx))
                HorizontalDivider(color = OpusBorder, thickness = 16.refPx)

                // Sort header at y=[1599, 1627] (61 px gap, 29 px height)
                Spacer(modifier = Modifier.height(61.refPx))
                Text(
                    text = "Sort",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    modifier = Modifier.padding(horizontal = 44.refPx)
                )

                // Divider at y=[1686, 1688] (59 px gap)
                Spacer(modifier = Modifier.height(59.refPx))
                HorizontalDivider(color = OpusBorder, thickness = 3.refPx)

                // Sort rows at y=1747, 1895, 2042, 2189 (148 px pitch)
                Spacer(modifier = Modifier.height(59.refPx))
                LibrarySortOrder.entries.forEachIndexed { idx, order ->
                    SortOptionRow(
                        label = order.displayName,
                        selected = state.sortOrder == order,
                        onClick = { state = state.copy(sortOrder = order) }
                    )
                    if (idx < LibrarySortOrder.entries.lastIndex) {
                        Spacer(modifier = Modifier.height(114.refPx))
                    }
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
private fun ViewOptionRow(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(43.refPx)
            .clickable(onClick = onClick)
            .padding(horizontal = 52.refPx),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OpusTextPrimary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            style = TextStyle(
                fontFamily = AmazonEmberFontFamily,
                color = OpusTextPrimary,
                fontSize = 14.sp,
                platformStyle = NoPaddingPlatformStyle
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 36.refPx)
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
            .height(34.refPx)
            .clickable(onClick = onClick)
            .padding(horizontal = 44.refPx),
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
}

@Preview(name = "06 Library View and Sort Sheet Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun LibraryViewSortSheetPreview() {
    OpusReaderTheme {
        LibraryViewSortSheet()
    }
}
