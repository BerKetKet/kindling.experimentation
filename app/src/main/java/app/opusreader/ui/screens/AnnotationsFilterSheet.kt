package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.HighlightAqua
import app.opusreader.ui.theme.HighlightBlue
import app.opusreader.ui.theme.HighlightDefault
import app.opusreader.ui.theme.HighlightGray
import app.opusreader.ui.theme.HighlightGreen
import app.opusreader.ui.theme.HighlightOrange
import app.opusreader.ui.theme.HighlightPurple
import app.opusreader.ui.theme.HighlightRed
import app.opusreader.ui.theme.HighlightYellow
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusScrim
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.refPx

@OptIn(ExperimentalLayoutApi::class)
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

    val types = listOf(
        "Favorite" to 0,
        "Note" to 0,
        "Highlight" to 1,
        "Underline" to 0,
        "Outline" to 0,
        "Bookmark" to 1
    )

    val colors = listOf(
        Triple("Default", HighlightDefault, 0),
        Triple("Gray", HighlightGray, 0),
        Triple("Red", HighlightRed, 0),
        Triple("Orange", HighlightOrange, 0),
        Triple("Yellow", HighlightYellow, 1),
        Triple("Green", HighlightGreen, 0),
        Triple("Aqua", HighlightAqua, 0),
        Triple("Blue", HighlightBlue, 1),
        Triple("Purple", HighlightPurple, 0)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OpusScrim)
            .clickable(onClick = { onApply(selectedTypes, selectedColors) }),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            // Sheet container: y=[1190, 2276], h=1087 px, surface #101112
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1087.refPx)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color(0xFF101112))
                    .clipToBounds()
            ) {
                // Header row: centered "Filter" + right Close 'X' at y=[1212, 1253]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.refPx)
                        .padding(horizontal = 42.refPx),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Filter",
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
                        onClick = { onApply(selectedTypes, selectedColors) },
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

                // Divider 1 at y=[1310, 1312]
                HorizontalDivider(color = OpusDivider, thickness = 3.refPx)

                // "Type" section header at y=[1366, 1405]
                Spacer(modifier = Modifier.height(54.refPx))
                Text(
                    text = "Type",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    modifier = Modifier.padding(horizontal = 42.refPx)
                )
                Spacer(modifier = Modifier.height(32.refPx))

                // Type chips: Row 1 y=[1437, 1541] (105px), Row 2 y=[1568, 1672] (105px), vertical gap 26px
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 42.refPx),
                    horizontalArrangement = Arrangement.spacedBy(26.refPx),
                    verticalArrangement = Arrangement.spacedBy(26.refPx)
                ) {
                    types.forEach { (type, count) ->
                        val isSelected = selectedTypes.contains(type)
                        val shape = RoundedCornerShape(4.dp)
                        Box(
                            modifier = Modifier
                                .height(105.refPx)
                                .clip(shape)
                                .background(Color(0xFF28292A))
                                .border(
                                    width = if (isSelected) 3.refPx else 2.refPx,
                                    color = if (isSelected) OpusTextPrimary else Color(0xFF8E9194),
                                    shape = shape
                                )
                                .clickable {
                                    selectedTypes = if (isSelected) selectedTypes - type else selectedTypes + type
                                }
                                .padding(horizontal = 36.refPx),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$type ($count)",
                                style = TextStyle(
                                    fontFamily = AmazonEmberFontFamily,
                                    color = OpusTextPrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    platformStyle = NoPaddingPlatformStyle
                                )
                            )
                        }
                    }
                }

                // Divider 2 at y=[1725, 1727] (53 px gap)
                Spacer(modifier = Modifier.height(53.refPx))
                HorizontalDivider(color = OpusDivider, thickness = 3.refPx)

                // "Color" section header at y=[1779, 1811]
                Spacer(modifier = Modifier.height(52.refPx))
                Text(
                    text = "Color",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    modifier = Modifier.padding(horizontal = 44.refPx)
                )
                Spacer(modifier = Modifier.height(41.refPx))

                // Color chips: rows at y=1852, 1983, 2114, 2245 (105px height, 26px gap)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 42.refPx),
                    horizontalArrangement = Arrangement.spacedBy(26.refPx),
                    verticalArrangement = Arrangement.spacedBy(26.refPx)
                ) {
                    colors.forEach { (name, color, count) ->
                        val isSelected = selectedColors.contains(name.uppercase())
                        val shape = RoundedCornerShape(4.dp)
                        Row(
                            modifier = Modifier
                                .height(105.refPx)
                                .clip(shape)
                                .background(Color(0xFF28292A))
                                .border(
                                    width = if (isSelected) 3.refPx else 2.refPx,
                                    color = if (isSelected) OpusTextPrimary else Color(0xFF8E9194),
                                    shape = shape
                                )
                                .clickable {
                                    val upper = name.uppercase()
                                    selectedColors = if (isSelected) selectedColors - upper else selectedColors + upper
                                }
                                .padding(horizontal = 32.refPx),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.refPx)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                            Spacer(modifier = Modifier.width(20.refPx))
                            Text(
                                text = "$name ($count)",
                                style = TextStyle(
                                    fontFamily = AmazonEmberFontFamily,
                                    color = OpusTextPrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    platformStyle = NoPaddingPlatformStyle
                                )
                            )
                        }
                    }
                }
            }

            // Bottom gesture bar area: y=[2277, 2339] (63 px)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.refPx)
                    .background(Color(0xFF101112))
            )
        }
    }
}

@Preview(name = "14 Annotations Filter Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun AnnotationsFilterSheetPreview() {
    OpusReaderTheme {
        AnnotationsFilterSheet()
    }
}
