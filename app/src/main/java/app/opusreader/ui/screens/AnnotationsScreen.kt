package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.AnnotationItem
import app.opusreader.engine.AnnotationType
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.FilterSlidersIcon
import app.opusreader.ui.components.KindleShareBoxIcon
import app.opusreader.ui.components.SortArrowsIcon
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.HighlightBlue
import app.opusreader.ui.theme.HighlightGreen
import app.opusreader.ui.theme.HighlightOrange
import app.opusreader.ui.theme.HighlightPink
import app.opusreader.ui.theme.HighlightYellow
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentGold
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSheetActionBlue
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary
import app.opusreader.ui.theme.refPx

@Composable
fun AnnotationsScreen(
    engine: ReaderEngine,
    filterTypes: Set<String> = emptySet(),
    filterColors: Set<String> = emptySet(),
    sortField: AnnotationSortField = AnnotationSortField.LOCATION,
    sortDirection: SortDirection = SortDirection.ASCENDING,
    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onItemClick: (AnnotationItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val rawAnnotations by engine.annotations.collectAsState()

    val annotations = remember(rawAnnotations, filterTypes, filterColors, sortField, sortDirection) {
        rawAnnotations
            .filter { item ->
                val typeMatch = if (filterTypes.isEmpty()) true else {
                    (filterTypes.contains("Favorite") && item.isFavorite) ||
                        (filterTypes.contains("Note") && item.type == AnnotationType.NOTE) ||
                        (filterTypes.contains("Highlight") && item.type == AnnotationType.HIGHLIGHT) ||
                        (filterTypes.contains("Bookmark") && item.type == AnnotationType.BOOKMARK)
                }
                val colorMatch = if (filterColors.isEmpty()) true else {
                    filterColors.contains(item.highlightColor.uppercase())
                }
                typeMatch && colorMatch
            }
            .let { list ->
                when (sortField) {
                    AnnotationSortField.LOCATION -> {
                        if (sortDirection == SortDirection.ASCENDING) list.sortedBy { it.location }
                        else list.sortedByDescending { it.location }
                    }
                    AnnotationSortField.DATE_CREATED, AnnotationSortField.DATE_MODIFIED -> {
                        if (sortDirection == SortDirection.ASCENDING) list.sortedBy { it.dateAdded }
                        else list.sortedByDescending { it.dateAdded }
                    }
                }
            }
    }

    val groupedByChapter = remember(annotations) {
        annotations.groupBy { it.sectionTitle }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Combined Top Bar + Sub-header bar: y=[0, 411] (412 px total height)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(412.refPx)
                .background(OpusSurface)
                .statusBarsPadding()
        ) {
            // Top bar row with centered "Annotations" + Share + Sort
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.refPx)
                    .padding(horizontal = 36.refPx),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Annotations",
                    style = TextStyle(
                        fontFamily = BookerlyFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = OpusTextPrimary,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.size(56.refPx)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back to Reader",
                            tint = OpusTextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = onExportClick, modifier = Modifier.size(56.refPx)) {
                        KindleShareBoxIcon()
                    }
                    Spacer(modifier = Modifier.width(18.refPx))
                    IconButton(onClick = onSortClick, modifier = Modifier.size(56.refPx)) {
                        SortArrowsIcon()
                    }
                }
            }

            // Sub-header bar with bordered Filter button on the left
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF1F1F1F))
                    .padding(horizontal = 45.refPx),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 110.refPx, height = 95.refPx)
                        .clip(RoundedCornerShape(4.dp))
                        .border(2.refPx, Color(0xFF6E7073), RoundedCornerShape(4.dp))
                        .clickable(onClick = onFilterClick),
                    contentAlignment = Alignment.Center
                ) {
                    FilterSlidersIcon(modifier = Modifier.size(20.dp))
                }
            }
        }

        if (annotations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No annotations match your filter.",
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextSecondary,
                    fontSize = 15.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clipToBounds()
            ) {
                groupedByChapter.forEach { (chapterTitle, chapterItems) ->
                    item(key = "header_$chapterTitle") {
                        // Chapter header band: y=[460, 497] (48 px top gap from 412, 38 px height)
                        Spacer(modifier = Modifier.height(48.refPx))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.refPx)
                                .padding(horizontal = 45.refPx),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = chapterTitle,
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
                                text = "(${chapterItems.size})",
                                style = TextStyle(
                                    fontFamily = AmazonEmberFontFamily,
                                    color = OpusTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    platformStyle = NoPaddingPlatformStyle
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(59.refPx))
                    }

                    chapterItems.forEach { item ->
                        item(key = item.id) {
                            AnnotationRow(
                                item = item,
                                onClick = {
                                    engine.jumpToLocation(item.location)
                                    onItemClick(item)
                                },
                                onToggleFavorite = { engine.toggleFavoriteAnnotation(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnnotationRow(
    item: AnnotationItem,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val highlightColor = when (item.highlightColor.uppercase()) {
        "YELLOW" -> HighlightYellow
        "BLUE" -> HighlightBlue
        "PINK" -> HighlightPink
        "ORANGE" -> HighlightOrange
        "GREEN" -> HighlightGreen
        else -> HighlightYellow
    }

    val typeLabel = when (item.type) {
        AnnotationType.BOOKMARK -> "BOOKMARK (${item.highlightColor.uppercase()})"
        AnnotationType.NOTE -> "NOTE (${item.highlightColor.uppercase()})"
        else -> "HIGHLIGHT (${item.highlightColor.uppercase()})"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        // Metadata line: y=[557, 593] (37 px height), x=[71, 1022]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(37.refPx)
                .padding(start = 71.refPx, end = 58.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Favorite",
                tint = if (item.isFavorite) OpusAccentGold else OpusTextTertiary,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(onClick = onToggleFavorite)
            )

            Spacer(modifier = Modifier.width(16.refPx))

            Text(
                text = "LOCATION ${item.location} • $typeLabel",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextTertiary,
                    fontSize = 12.sp,
                    platformStyle = NoPaddingPlatformStyle
                ),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = OpusTextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        // Gap to quote top (y=615): 22 px
        Spacer(modifier = Modifier.height(22.refPx))

        if (item.type == AnnotationType.BOOKMARK) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "Bookmark",
                tint = HighlightBlue,
                modifier = Modifier
                    .padding(start = 147.refPx, bottom = 30.refPx)
                    .size(52.refPx)
            )
        } else {
            // Quote block: y=[615, 811] (197 px height), x=85
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 85.refPx, end = 58.refPx)
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .width(8.refPx)
                        .fillMaxHeight()
                        .background(highlightColor)
                )

                Spacer(modifier = Modifier.width(32.refPx))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.selectedText,
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusTextPrimary,
                            fontSize = 13.5.sp,
                            lineHeight = 18.sp,
                            platformStyle = NoPaddingPlatformStyle
                        )
                    )

                    Spacer(modifier = Modifier.height(26.refPx))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = OpusSheetActionBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(12.refPx))
                        Text(
                            text = if (!item.note.isNullOrBlank()) item.note else "ADD NOTE",
                            style = TextStyle(
                                fontFamily = AmazonEmberFontFamily,
                                color = OpusSheetActionBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                platformStyle = NoPaddingPlatformStyle
                            )
                        )
                    }
                }
            }
        }

        // Gap to thick divider (y=841..846): 30 px
        Spacer(modifier = Modifier.height(30.refPx))
        HorizontalDivider(color = Color(0xFF1F1F1F), thickness = 6.refPx)
    }
}

@Preview(name = "13 Annotations Preview", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AnnotationsScreenPreview() {
    OpusReaderTheme {
        AnnotationsScreen(engine = FakeReaderEngine())
    }
}
