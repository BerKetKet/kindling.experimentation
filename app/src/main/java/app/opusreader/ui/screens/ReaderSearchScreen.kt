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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.engine.ReaderSearchResult
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

@Composable
fun ReaderSearchScreen(
    engine: ReaderEngine,
    onBackClick: () -> Unit = {},
    onResultClick: (ReaderSearchResult) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<ReaderSearchResult>>(emptyList()) }
    val isDark = isOpusDarkTheme
    val topSurface = if (isDark) Color(0xFF141414) else Color.White
    val bodySurface = if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5)

    LaunchedEffect(query) {
        results = if (query.isNotBlank()) {
            engine.searchInBook(query)
        } else {
            emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bodySurface)
    ) {
        // Top header block: y=[0, 253] (254 px total height including status bar)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(254.refPx)
                .background(topSurface)
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(44.refPx))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.refPx)
                    .padding(horizontal = 36.refPx),
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
                Spacer(modifier = Modifier.width(16.refPx))
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = OpusTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(24.refPx))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search",
                            style = TextStyle(
                                fontFamily = AmazonEmberFontFamily,
                                color = OpusTextSecondary,
                                fontSize = 15.sp,
                                platformStyle = NoPaddingPlatformStyle
                            )
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusTextPrimary,
                            fontSize = 15.sp,
                            platformStyle = NoPaddingPlatformStyle
                        ),
                        cursorBrush = SolidColor(OpusTextPrimary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = OpusTextSecondary
                        )
                    }
                }
            }
        }

        if (query.isBlank()) {
            // Prompt text: line 1 at y=[375, 411], line 2 at y=[430, 466] (121 px below top bar 254)
            Spacer(modifier = Modifier.height(121.refPx))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 180.refPx),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Find exactly what you're looking for in your book and notes.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = Color(0xFFC2C2C2),
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else if (results.isEmpty()) {
            Spacer(modifier = Modifier.height(121.refPx))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 120.refPx),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "No results found for \"$query\"",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 14.sp,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(results) { res ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                engine.jumpToLocation(res.location)
                                onResultClick(res)
                            }
                            .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium)
                    ) {
                        Text(
                            text = "Location ${res.location} · ${res.chapterTitle}",
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusTextTertiary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = res.previewText,
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusTextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                    HorizontalDivider(color = OpusDivider.copy(alpha = 0.5f), thickness = 0.5.dp)
                }
            }
        }
    }
}

@Preview(name = "12 Reader Search Preview", showBackground = true, backgroundColor = 0xFF333333)
@Composable
fun ReaderSearchScreenPreview() {
    OpusReaderTheme {
        ReaderSearchScreen(engine = FakeReaderEngine())
    }
}
