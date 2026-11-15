package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.engine.AnnotationItem
import app.opusreader.engine.AnnotationType
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
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

    val groupedAnnotations = remember(annotations) {
        annotations.groupBy { it.sectionTitle }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Bar (y=0..251)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(251.refPx)
                .background(Color(0xFF232323))
        ) {
            // Back chevron
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 24.refPx, bottom = 18.refPx)
                    .size(48.refPx)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reader_back),
                    contentDescription = "Back",
                    tint = Color(0xFFEEEEEE),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Title "Annotations" (20sp Amazon Ember per Kindle new_notebook_actionbar)
            Text(
                text = "Annotations",
                color = Color(0xFFEEEEEE),
                fontFamily = AmazonEmberFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 26.refPx)
            )

            // Right actions: Share and Sort
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.refPx, bottom = 18.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onExportClick,
                    modifier = Modifier.size(48.refPx)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bds_share),
                        contentDescription = "Share",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(36.refPx))
                IconButton(
                    onClick = onSortClick,
                    modifier = Modifier.size(48.refPx)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bds_sort),
                        contentDescription = "Sort",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Controls bar (y=251..411)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.refPx)
                .background(Color(0xFF1F1F1F))
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 43.refPx, top = 34.refPx)
                    .size(width = 104.refPx, height = 90.refPx)
                    .border(width = 1.5.dp, color = Color(0xFF4A4A4A), shape = RoundedCornerShape(8.dp))
                    .clickable(onClick = onFilterClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bds_filter),
                    contentDescription = "Filter",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Divider line below controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.refPx)
                    .background(Color(0xFF3B3C3D))
                    .align(Alignment.BottomCenter)
            )
        }

        if (annotations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No annotations match your filter.",
                    color = Color(0xFF898989),
                    fontFamily = AmazonEmberFontFamily,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                groupedAnnotations.forEach { (sectionTitle, items) ->
                    item {
                        // Section Header: 16sp Amazon Ember Bold
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = (45 / 2.625).dp, end = (45 / 2.625).dp, top = (40 / 2.625).dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sectionTitle,
                                color = Color.White,
                                fontFamily = AmazonEmberFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                            )
                            Text(
                                text = "(${items.size})",
                                color = Color.White,
                                fontFamily = AmazonEmberFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                            )
                        }
                    }

                    items(items.size) { index ->
                        val item = items[index]
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

@Composable
private fun AnnotationRow(
    item: AnnotationItem,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Meta Row: 10sp Amazon Ember per note_list_item_annotation_type_and_location_font_size
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = (71 / 2.625).dp, end = (58 / 2.625).dp, top = (50 / 2.625).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_bds_star),
                contentDescription = if (item.isFavorite) "Favorite" else "Not favorite",
                tint = if (item.isFavorite) Color(0xFFFFD87C) else Color(0xFF898989),
                modifier = Modifier
                    .size(width = (36 / 2.625).dp, height = (35 / 2.625).dp)
                    .clickable { onToggleFavorite() }
            )

            Spacer(modifier = Modifier.width((28 / 2.625).dp))

            val typeLabel = when (item.type) {
                AnnotationType.HIGHLIGHT -> "HIGHLIGHT (${item.highlightColor.uppercase()})"
                AnnotationType.BOOKMARK -> "BOOKMARK (${item.highlightColor.uppercase()})"
                AnnotationType.NOTE -> "NOTE"
                AnnotationType.UNDERLINE -> "UNDERLINE"
            }

            Text(
                text = "LOCATION ${item.location} • $typeLabel",
                color = Color(0xFF898989),
                fontFamily = AmazonEmberFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.ic_reader_overflow),
                contentDescription = "Options",
                tint = Color(0xFF898989),
                modifier = Modifier.size(width = (6 / 2.625).dp, height = (34 / 2.625).dp)
            )
        }

        if (item.type == AnnotationType.HIGHLIGHT) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = (85 / 2.625).dp, end = (45 / 2.625).dp, top = (29 / 2.625).dp, bottom = (29 / 2.625).dp)
            ) {
                val barColor = when (item.highlightColor.uppercase()) {
                    "YELLOW" -> Color(0xFFFFD87C)
                    "BLUE" -> Color(0xFF5EAFFF)
                    "PINK" -> Color(0xFFFF8DA1)
                    "ORANGE" -> Color(0xFFFFB366)
                    else -> Color(0xFFFFD87C)
                }

                // Yellow vertical bar: x=85..95 (w=11px), y=615..811 (h=197px)
                Box(
                    modifier = Modifier
                        .width((11 / 2.625).dp)
                        .height((197 / 2.625).dp)
                        .background(barColor)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = (55 / 2.625).dp)
                ) {
                    Spacer(modifier = Modifier.height((10 / 2.625).dp))
                    Text(
                        text = item.selectedText,
                        color = Color.White,
                        fontFamily = BookerlyFontFamily,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )

                    Spacer(modifier = Modifier.height((74 / 2.625).dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* add note */ }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bds_sticky_note),
                            contentDescription = "Add note",
                            tint = Color(0xFF29ABE2),
                            modifier = Modifier.size((36 / 2.625).dp)
                        )
                        Spacer(modifier = Modifier.width((14 / 2.625).dp))
                        Text(
                            text = "ADD NOTE",
                            color = Color(0xFF29ABE2),
                            fontFamily = AmazonEmberFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }
                }
            }
        } else if (item.type == AnnotationType.BOOKMARK) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = (147 / 2.625).dp, top = (18 / 2.625).dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bds_bookmark),
                    contentDescription = "Bookmark",
                    tint = Color(0xFF5EAFFF),
                    modifier = Modifier.size(width = (35 / 2.625).dp, height = (50 / 2.625).dp)
                )
            }

            Spacer(modifier = Modifier.height((623 / 2.625).dp))

            Text(
                text = item.selectedText,
                color = Color.White,
                fontFamily = BookerlyFontFamily,
                fontSize = 15.sp,
                lineHeight = 23.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier.padding(start = (138 / 2.625).dp, end = (45 / 2.625).dp, bottom = (47 / 2.625).dp)
            )
        }

        // Horizontal divider between rows
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((5 / 2.625).dp)
                .background(Color(0xFF2D2D2D))
        )
    }
}

@Preview(name = "13 Annotations Preview", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AnnotationsScreenPreview() {
    OpusReaderTheme {
        AnnotationsScreen(engine = FakeReaderEngine())
    }
}
