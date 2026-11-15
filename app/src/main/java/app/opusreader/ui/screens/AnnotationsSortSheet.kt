package app.opusreader.ui.screens

import androidx.compose.foundation.Image
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
import app.opusreader.ui.components.OpusBottomSheetContainer
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

enum class AnnotationSortField(val displayName: String) {
    LOCATION("Location"),
    DATE_CREATED("Date created"),
    DATE_MODIFIED("Date modified")
}

enum class SortDirection(val displayName: String) {
    ASCENDING("Ascending"),
    DESCENDING("Descending")
}

@Composable
fun AnnotationsSortSheet(
    initialField: AnnotationSortField = AnnotationSortField.LOCATION,
    initialDirection: SortDirection = SortDirection.ASCENDING,
    onDismiss: () -> Unit = {},
    onApply: (AnnotationSortField, SortDirection) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var selectedField by remember { mutableStateOf(initialField) }
    var selectedDirection by remember { mutableStateOf(initialDirection) }

    OpusBottomSheetContainer(
        title = "Sort",
        doneButtonText = "DONE",
        onDoneClick = { onApply(selectedField, selectedDirection) },
        onCloseClick = onDismiss,
        modifier = modifier
    ) {
        SectionTitle("Sort by")

        AnnotationSortField.entries.forEach { field ->
            val isSelected = selectedField == field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedField = field }
                    .padding(horizontal = OpusSpacing.standard, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = field.displayName,
                    color = OpusTextPrimary,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = OpusTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = OpusDivider, thickness = 1.dp)

        SectionTitle("Order")

        SortDirection.entries.forEach { dir ->
            val isSelected = selectedDirection == dir
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedDirection = dir }
                    .padding(horizontal = OpusSpacing.standard, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dir.displayName,
                    color = OpusTextPrimary,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = OpusTextPrimary,
                        modifier = Modifier.size(20.dp)
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

@Preview(name = "15 Annotations Sort Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun AnnotationsSortSheetPreview() {
    OpusReaderTheme {
        AnnotationsSortSheet()
    }
}
