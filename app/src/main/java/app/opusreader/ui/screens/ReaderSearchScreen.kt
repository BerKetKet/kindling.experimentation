package app.opusreader.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.engine.ReaderSearchResult
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary
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
            .background(OpusBackground)
    ) {
        OpusTopBar(
            title = "Search Book",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navigationIconDescription = "Back to Reader",
            onNavigationClick = onBackClick
        )

        OpusSearchBar(
            query = query,
            onQueryChange = { query = it },
            placeholder = "Search in book and notes"
        )

        if (query.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(OpusSpacing.xlarge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Find exactly what you're looking for in your book and notes.",
                    color = OpusTextSecondary,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        } else if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(OpusSpacing.xlarge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No results found for \"$query\"",
                    color = OpusTextSecondary,
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
                            .clickable {
                                engine.jumpToLocation(res.location)
                                onResultClick(res)
                            }
                            .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium)
                    ) {
                        Text(
                            text = "Location ${res.location} · ${res.chapterTitle}",
                            color = OpusTextTertiary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(OpusSpacing.xxsmall))
                        Text(
                            text = res.previewText,
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

@Preview(name = "12 Reader Search Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun ReaderSearchScreenPreview() {
    OpusReaderTheme {
        ReaderSearchScreen(engine = FakeReaderEngine())
    }
}
