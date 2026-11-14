package app.opusreader.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.ui.components.OpusBottomSheetContainer
import app.opusreader.ui.theme.HighlightBlue
import app.opusreader.ui.theme.HighlightGreen
import app.opusreader.ui.theme.HighlightOrange
import app.opusreader.ui.theme.HighlightPink
import app.opusreader.ui.theme.HighlightYellow
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnnotationsFilterSheet(
    initialTypes: Set<String> = emptySet(),
    initialColors: Set<String> = emptySet(),
    onCloseClick: () -> Unit = {},
    onApply: (Set<String>, Set<String>) -> Unit = { _, _ -> },
    isFixture: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isFixture) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.fixture_annotations_filter_bg),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .clickable(onClick = onCloseClick)
            )
            Image(
                painter = painterResource(id = R.drawable.fixture_annotations_filter_sheet),
                contentDescription = "Filter Sheet",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1088.refPx)
                    .clickable(onClick = onCloseClick)
            )
        }
        return
    }

    var selectedTypes by remember { mutableStateOf(initialTypes) }
    var selectedColors by remember { mutableStateOf(initialColors) }

    val types = listOf(
        "Favorite" to 2,
        "Note" to 1,
        "Highlight" to 2,
        "Bookmark" to 1
    )

    val colors = listOf(
        "Yellow" to HighlightYellow,
        "Blue" to HighlightBlue,
        "Pink" to HighlightPink,
        "Orange" to HighlightOrange,
        "Green" to HighlightGreen
    )

    OpusBottomSheetContainer(
        title = "Filter",
        doneButtonText = "DONE",
        onDoneClick = { onApply(selectedTypes, selectedColors) },
        onCloseClick = onCloseClick,
        modifier = modifier
    ) {
        SectionTitle("Type")

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.small),
            verticalArrangement = Arrangement.spacedBy(OpusSpacing.small)
        ) {
            types.forEach { (type, count) ->
                val isSelected = selectedTypes.contains(type)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) OpusTextPrimary else OpusSurfaceVariant)
                        .clickable {
                            selectedTypes = if (isSelected) selectedTypes - type else selectedTypes + type
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$type ($count)",
                        color = if (isSelected) Color(0xFF101112) else OpusTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.medium))
        HorizontalDivider(color = OpusDivider, thickness = 1.dp)

        SectionTitle("Color")

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(OpusSpacing.small)
        ) {
            colors.forEach { (name, color) ->
                val isSelected = selectedColors.contains(name.uppercase())
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) OpusSurfaceVariant else Color.Transparent)
                        .clickable {
                            val upper = name.uppercase()
                            selectedColors = if (isSelected) selectedColors - upper else selectedColors + upper
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = name,
                        color = OpusTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
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

@Preview(name = "14 Annotations Filter Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun AnnotationsFilterSheetPreview() {
    OpusReaderTheme {
        AnnotationsFilterSheet()
    }
}
