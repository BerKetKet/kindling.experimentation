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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.engine.ReaderSearchResult
import app.opusreader.ui.theme.OpusReaderTheme
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
            .background(Color(0xFF333333))
    ) {
        // Top Bar: y=0..253 (height 254 px, background #141414)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(254.refPx)
                .background(Color(0xFF141414))
        ) {
            // Status bar space (y=0..90)
            Spacer(modifier = Modifier.height(90.refPx))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(164.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button (width 132 px, center cx=66 px)
                Box(
                    modifier = Modifier
                        .width(132.refPx)
                        .height(164.refPx)
                        .clickable(onClick = onBackClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_reader_back),
                        contentDescription = "Back to Reader",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Search field / Title
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    textStyle = TextStyle(
                        color = Color(0xFFEEEEEE),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    cursorBrush = SolidColor(Color(0xFF29ABE2)),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.refPx),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = "SEARCH",
                                color = Color(0xFFEEEEEE),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 0.5.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        if (query.isBlank()) {
            // Empty State matching 12_reader-search.png
            Spacer(modifier = Modifier.height(110.refPx))
            Text(
                text = "Find words and phrases in this book",
                color = Color(0xFFC2C2C2),
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.refPx))
            Text(
                text = "or in your entire library",
                color = Color(0xFFC2C2C2),
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No results found for \"$query\"",
                    color = Color(0xFFC2C2C2),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(results) { res ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onResultClick(res) }
                            .padding(horizontal = 24.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = res.chapterTitle,
                            color = Color(0xFFA6A6A6),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = res.previewText,
                            color = Color(0xFFEEEEEE),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Location ${res.location}",
                            color = Color(0xFF888888),
                            fontSize = 11.sp
                        )
                    }
                    HorizontalDivider(color = Color(0xFF404040), thickness = 1.refPx)
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
