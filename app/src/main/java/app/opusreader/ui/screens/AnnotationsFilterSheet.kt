package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.refPx

@Composable
fun AnnotationsFilterSheet(
    initialTypes: Set<String> = setOf("Highlight", "Bookmark"),
    initialColors: Set<String> = setOf("YELLOW", "BLUE"),
    onCloseClick: () -> Unit = {},
    onApply: (Set<String>, Set<String>) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var selectedTypes by remember { mutableStateOf(initialTypes) }
    var selectedColors by remember { mutableStateOf(initialColors) }

    val typeRows = listOf(
        listOf("Favorite" to 0, "Note" to 0, "Highlight" to 1),
        listOf("Underline" to 0, "Outline" to 0, "Bookmark" to 1)
    )

    val colorRows = listOf(
        listOf(Triple("Default", 0, Color.White), Triple("Gray", 0, Color(0xFF8E9194)), Triple("Red", 0, Color(0xFFFF5252))),
        listOf(Triple("Orange", 0, Color(0xFFFF9800)), Triple("Yellow", 1, Color(0xFFFFD87C)), Triple("Green", 0, Color(0xFF4CAF50))),
        listOf(Triple("Aqua", 0, Color(0xFF00E5FF)), Triple("Blue", 1, Color(0xFF5EAFFF)), Triple("Purple", 0, Color(0xFF7C4DFF))),
        listOf(Triple("Pink", 0, Color(0xFFFF4081)))
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(1218.refPx)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color(0xFF101112))
    ) {
        // Header (y=1122..1310, h=190px)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.refPx)
        ) {
            Text(
                text = "Filter",
                color = Color.White,
                fontFamily = AmazonEmberFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = (89 / 2.625).dp)
            )

            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = (79 / 2.625).dp, end = (53 / 2.625).dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bds_close),
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Divider 1 (y=1310..1312, h=2px)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((2 / 2.625).dp)
                    .background(Color(0xFF2B2C2D))
                    .align(Alignment.BottomCenter)
            )
        }

        Spacer(modifier = Modifier.height((44 / 2.625).dp))

        // Type section title (y=1366..1405)
        Text(
            text = "Type",
            color = Color.White,
            fontFamily = AmazonEmberFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = Modifier.padding(start = (42 / 2.625).dp)
        )

        Spacer(modifier = Modifier.height((32 / 2.625).dp))

        // Type rows
        typeRows.forEachIndexed { index, rowItems ->
            if (index > 0) Spacer(modifier = Modifier.height((27 / 2.625).dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = (42 / 2.625).dp),
                horizontalArrangement = Arrangement.spacedBy((21 / 2.625).dp)
            ) {
                rowItems.forEach { (type, count) ->
                    val isSelected = selectedTypes.contains(type)
                    Box(
                        modifier = Modifier
                            .height((104 / 2.625).dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Color(0xFF141414) else Color(0xFF28292A))
                            .then(
                                if (isSelected) Modifier.border(width = 1.5.dp, color = Color(0xFFC5C6C7), shape = RoundedCornerShape(6.dp))
                                else Modifier
                            )
                            .clickable {
                                selectedTypes = if (isSelected) selectedTypes - type else selectedTypes + type
                                onApply(selectedTypes, selectedColors)
                            }
                            .padding(horizontal = (48 / 2.625).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$type ($count)",
                            color = if (isSelected) Color.White else Color(0xFFC5C6C7),
                            fontFamily = AmazonEmberFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height((53 / 2.625).dp))

        // Divider 2 (y=1725..1727, h=2px)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((2 / 2.625).dp)
                .background(Color(0xFF2B2C2D))
        )

        Spacer(modifier = Modifier.height((42 / 2.625).dp))

        // Color section title (y=1779..1811)
        Text(
            text = "Color",
            color = Color.White,
            fontFamily = AmazonEmberFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = Modifier.padding(start = (44 / 2.625).dp)
        )

        Spacer(modifier = Modifier.height((32 / 2.625).dp))

        // Color rows
        colorRows.forEachIndexed { index, rowItems ->
            if (index > 0) Spacer(modifier = Modifier.height((27 / 2.625).dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = (42 / 2.625).dp),
                horizontalArrangement = Arrangement.spacedBy((21 / 2.625).dp)
            ) {
                rowItems.forEach { (colorName, count, dotColor) ->
                    val isSelected = selectedColors.contains(colorName.uppercase())
                    Box(
                        modifier = Modifier
                            .height((104 / 2.625).dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Color(0xFF141414) else Color(0xFF28292A))
                            .then(
                                if (isSelected) Modifier.border(width = 1.5.dp, color = Color(0xFFC5C6C7), shape = RoundedCornerShape(6.dp))
                                else Modifier
                            )
                            .clickable {
                                selectedColors = if (isSelected) selectedColors - colorName.uppercase() else selectedColors + colorName.uppercase()
                                onApply(selectedTypes, selectedColors)
                            }
                            .padding(horizontal = (48 / 2.625).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size((36 / 2.625).dp)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                            Spacer(modifier = Modifier.width((33 / 2.625).dp))
                            Text(
                                text = "$colorName ($count)",
                                color = if (isSelected) Color.White else Color(0xFFC5C6C7),
                                fontFamily = AmazonEmberFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "14 Annotations Filter Sheet Preview", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AnnotationsFilterSheetPreview() {
    OpusReaderTheme {
        AnnotationsFilterSheet()
    }
}
