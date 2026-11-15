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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(972.refPx)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color(0xFF1A1B1C))
    ) {
        // Drag handle (y=1389..1399, top padding = 21px from top of sheet)
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = (21 / 2.625).dp)
                .size(width = (104 / 2.625).dp, height = (10 / 2.625).dp)
                .clip(CircleShape)
                .background(Color(0xFF8E9194))
        )

        // Title "Sort" (y=1445..1474, top padding = 34px from handle)
        Text(
            text = "Sort",
            color = Color.White,
            fontFamily = AmazonEmberFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = (34 / 2.625).dp)
        )

        Spacer(modifier = Modifier.height((56 / 2.625).dp))

        // Sort Field Rows (Location, Date created, Date modified)
        AnnotationSortField.entries.forEach { field ->
            val isSelected = selectedField == field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((146 / 2.625).dp)
                    .clickable {
                        selectedField = field
                        onApply(selectedField, selectedDirection)
                    }
                    .padding(start = (46 / 2.625).dp, end = (49 / 2.625).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = field.displayName,
                    color = Color.White,
                    fontFamily = AmazonEmberFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = Modifier.weight(1f)
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Divider 1 (y=1980..1982)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((2 / 2.625).dp)
                .background(Color(0xFF3B3C3D))
        )

        // Sort Direction Rows (Ascending, Descending)
        SortDirection.entries.forEach { dir ->
            val isSelected = selectedDirection == dir
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((146 / 2.625).dp)
                    .clickable {
                        selectedDirection = dir
                        onApply(selectedField, selectedDirection)
                    }
                    .padding(start = (46 / 2.625).dp, end = (49 / 2.625).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dir.displayName,
                    color = Color.White,
                    fontFamily = AmazonEmberFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = Modifier.weight(1f)
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Divider 2 (y=2275..2277)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((2 / 2.625).dp)
                .background(Color(0xFF3B3C3D))
        )
    }
}

@Preview(name = "15 Annotations Sort Sheet Preview", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AnnotationsSortSheetPreview() {
    OpusReaderTheme {
        AnnotationsSortSheet()
    }
}
