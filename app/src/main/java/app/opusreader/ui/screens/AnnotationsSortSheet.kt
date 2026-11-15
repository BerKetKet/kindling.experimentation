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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusScrim
import app.opusreader.ui.theme.OpusTextPrimary
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OpusScrim)
            .clickable(onClick = { onApply(selectedField, selectedDirection) }),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(906.refPx)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color(0xFF1A1B1C))
            ) {
                // Header row: centered "Sort" + right Close 'X' at y=[1450, 1474], divider at y=[1536, 1538]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(166.refPx)
                        .padding(horizontal = 42.refPx),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sort",
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            platformStyle = NoPaddingPlatformStyle
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IconButton(
                        onClick = { onApply(selectedField, selectedDirection) },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(48.refPx)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = OpusTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider(color = OpusDivider, thickness = 3.refPx)

                // Location (y=1591), Date created (y=1744), Date modified (y=1891)
                AnnotationSortField.entries.forEach { field ->
                    val isSelected = selectedField == field
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(147.refPx)
                            .clickable {
                                selectedField = field
                                onApply(field, selectedDirection)
                            }
                            .padding(horizontal = 44.refPx),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = field.displayName,
                            style = TextStyle(
                                fontFamily = AmazonEmberFontFamily,
                                color = OpusTextPrimary,
                                fontSize = 14.sp,
                                platformStyle = NoPaddingPlatformStyle
                            ),
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

                HorizontalDivider(color = OpusDivider, thickness = 3.refPx)

                // Ascending (y=2035), Descending (y=2188)
                SortDirection.entries.forEach { dir ->
                    val isSelected = selectedDirection == dir
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(147.refPx)
                            .clickable {
                                selectedDirection = dir
                                onApply(selectedField, dir)
                            }
                            .padding(horizontal = 44.refPx),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dir.displayName,
                            style = TextStyle(
                                fontFamily = AmazonEmberFontFamily,
                                color = OpusTextPrimary,
                                fontSize = 14.sp,
                                platformStyle = NoPaddingPlatformStyle
                            ),
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.refPx)
                    .background(Color(0xFF1A1B1C))
            )
        }
    }
}

@Preview(name = "15 Annotations Sort Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun AnnotationsSortSheetPreview() {
    OpusReaderTheme {
        AnnotationsSortSheet()
    }
}
