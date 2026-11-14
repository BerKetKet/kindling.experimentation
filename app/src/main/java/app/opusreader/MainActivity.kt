package app.opusreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.components.OpusCoverTile
import app.opusreader.ui.components.OpusListRow
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusToggleRow
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.components.ToggleType
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpusReaderTheme {
                Phase0ShowcaseScreen()
            }
        }
    }
}

@Composable
fun Phase0ShowcaseScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var toggleState by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OpusBackground)
            .verticalScroll(rememberScrollState())
    ) {
        OpusTopBar(title = "Opus Reader")

        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Library"
        )

        Column(
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small)
        ) {
            Text(
                text = "Shared Components Showcase",
                color = OpusTextPrimary,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = OpusSpacing.small)
            )

            OpusCoverTile(
                title = "Gardens of the Moon",
                author = "Steven Erikson",
                progressPercent = 10,
                coverColor = Color(0xFF384F38)
            )
        }

        OpusListRow(
            title = "Gardens of the Moon: Book One of The Malazan Book of the Fallen",
            author = "ERIKSON, STEVEN",
            metaInfo = "15 MB",
            progressPercent = 10,
            coverColor = Color(0xFF384F38)
        )

        OpusToggleRow(
            title = "Continuous Scrolling",
            subtitle = "Read by scrolling vertically.",
            checked = toggleState,
            toggleType = ToggleType.SWITCH,
            onCheckedChange = { toggleState = it }
        )
    }
}

@Preview(name = "Phase 0 Showcase Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun Phase0ShowcasePreview() {
    OpusReaderTheme {
        Phase0ShowcaseScreen()
    }
}
