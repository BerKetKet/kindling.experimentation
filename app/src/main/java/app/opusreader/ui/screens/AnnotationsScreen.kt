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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.AnnotationItem
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.HighlightBlue
import app.opusreader.ui.theme.HighlightOrange
import app.opusreader.ui.theme.HighlightPink
import app.opusreader.ui.theme.HighlightYellow
import app.opusreader.ui.theme.OpusAccentGold
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary

@Composable
fun AnnotationsScreen(
    engine: ReaderEngine,
    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onItemClick: (AnnotationItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val annotations by engine.annotations.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
    ) {
        OpusTopBar(
            title = "Annotations",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navigationIconDescription = "Back to Reader",
            onNavigationClick = onBackClick,
            actions = {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter annotations",
                        tint = OpusTextPrimary
                    )
                }
                IconButton(onClick = onSortClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort annotations",
                        tint = OpusTextPrimary
                    )
                }
                IconButton(onClick = onExportClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Export annotations",
                        tint = OpusTextPrimary
                    )
                }
            }
        )

        if (annotations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No annotations yet.\nHighlight text or add notes while reading.",
                    color = OpusTextSecondary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(annotations, key = { it.id }) { item ->
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

@Composable
private fun AnnotationRow(
    item: AnnotationItem,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val highlightColor = when (item.highlightColor) {
        "BLUE" -> HighlightBlue
        "PINK" -> HighlightPink
        "ORANGE" -> HighlightOrange
        else -> HighlightYellow
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium)
    ) {
        // Section & metadata row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 8.dp, height = 8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(highlightColor)
            )

            Spacer(modifier = Modifier.width(OpusSpacing.small))

            Text(
                text = "${item.sectionTitle} · Location ${item.location}".uppercase(),
                color = OpusTextTertiary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = if (item.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (item.isFavorite) OpusAccentGold else OpusTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.xsmall))

        // Selected quote text
        Text(
            text = "“${item.selectedText}”",
            color = OpusTextPrimary,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        // Note if present
        if (!item.note.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(OpusSpacing.small))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(OpusSurfaceVariant)
                    .padding(OpusSpacing.small)
            ) {
                Text(
                    text = item.note,
                    color = OpusTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
    HorizontalDivider(color = OpusDivider.copy(alpha = 0.5f), thickness = 0.5.dp)
}

@Preview(name = "13 Annotations Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun AnnotationsScreenPreview() {
    OpusReaderTheme {
        AnnotationsScreen(engine = FakeReaderEngine())
    }
}
