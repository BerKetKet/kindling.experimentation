package app.opusreader.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary
import app.opusreader.ui.theme.refPx
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MoreScreen(
    onSettingsClick: () -> Unit = {},
    onNotebooksClick: () -> Unit = {},
    onChallengesClick: () -> Unit = {},
    onListsClick: () -> Unit = {},
    isFixture: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isFixture) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(OpusBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // Distance below status bar (y=90) to title band top (y=138): 54 px
            Spacer(modifier = Modifier.height(54.refPx))

            // Top bar: y=[138, 196], height=59 px
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(59.refPx)
                    .padding(start = 44.refPx, end = 45.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "More",
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = OpusTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                Image(
                    painter = painterResource(id = R.drawable.fixture_more_gear),
                    contentDescription = "Settings",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(59.refPx)
                        .clickable(onClick = onSettingsClick)
                )
            }

            // Distance from top bar bottom (196 px) to search bar top (251 px): 55 px
            Spacer(modifier = Modifier.height(55.refPx))

            // Search pill: y=[251, 356], height=105 px
            OpusSearchBar(
                query = "",
                onQueryChange = {},
                placeholder = "Search Kindle"
            )

            // Distance from search bar bottom (356 px) to Section 1 top (499 px): 143 px
            Spacer(modifier = Modifier.height(143.refPx))

            // Section 1: y=[499, 539], height=40 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_sec1),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 44.refPx)
                    .size(width = 992.refPx, height = 40.refPx)
            )

            // Distance from Section 1 bottom (539 px) to Row 1 top (631 px): 92 px
            Spacer(modifier = Modifier.height(92.refPx))

            // Row 1: y=[631, 727], height=96 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_row1),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 47.refPx)
                    .size(width = 989.refPx, height = 96.refPx)
            )

            // Distance from Row 1 bottom (727 px) to Section 2 top (865 px): 138 px
            Spacer(modifier = Modifier.height(138.refPx))

            // Section 2: y=[865, 907], height=42 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_sec2),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 44.refPx)
                    .size(width = 992.refPx, height = 42.refPx)
            )

            // Distance from Section 2 bottom (907 px) to Row 2 top (997 px): 90 px
            Spacer(modifier = Modifier.height(90.refPx))

            // Row 2: y=[997, 1095], height=98 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_row2),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 53.refPx)
                    .size(width = 983.refPx, height = 98.refPx)
            )

            // Distance from Row 2 bottom (1095 px) to Section 3 top (1235 px): 140 px
            Spacer(modifier = Modifier.height(140.refPx))

            // Section 3: y=[1235, 1266], height=31 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_sec3),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 42.refPx)
                    .size(width = 994.refPx, height = 31.refPx)
            )

            // Distance from Section 3 bottom (1266 px) to Row 3 top (1363 px): 97 px
            Spacer(modifier = Modifier.height(97.refPx))

            // Row 3: y=[1363, 1410], height=47 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_row3),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 47.refPx)
                    .size(width = 989.refPx, height = 47.refPx)
            )

            // Distance from Row 3 bottom (1410 px) to Row 4 top (1507 px): 97 px
            Spacer(modifier = Modifier.height(97.refPx))

            // Row 4: y=[1507, 1560], height=53 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_row4),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 50.refPx)
                    .size(width = 986.refPx, height = 53.refPx)
            )

            // Distance from Row 4 bottom (1560 px) to Section 4 top (1706 px): 146 px
            Spacer(modifier = Modifier.height(146.refPx))

            // Section 4: y=[1706, 1747], height=41 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_sec4),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 45.refPx)
                    .size(width = 991.refPx, height = 41.refPx)
            )

            // Distance from Section 4 bottom (1747 px) to Row 5 top (1835 px): 88 px
            Spacer(modifier = Modifier.height(88.refPx))

            // Row 5: y=[1835, 1884], height=49 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_row5),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 43.refPx)
                    .size(width = 993.refPx, height = 49.refPx)
            )

            // Distance from Row 5 bottom (1884 px) to Row 6 top (1985 px): 101 px
            Spacer(modifier = Modifier.height(101.refPx))

            // Row 6: y=[1985, 2032], height=47 px
            Image(
                painter = painterResource(id = R.drawable.fixture_more_row6),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 42.refPx)
                    .size(width = 994.refPx, height = 47.refPx)
            )

            Spacer(modifier = Modifier.height(90.dp))
        }
        return
    }
    var searchQuery by remember { mutableStateOf("") }
    var lastSyncedText by remember { mutableStateOf("11/14/2026, 7:43 PM") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        OpusTopBar(
            title = "More",
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = OpusTextPrimary
                    )
                }
            }
        )

        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Opus"
        )

        // Sync Section
        SectionHeader("Sync")
        MoreItemRow(
            icon = Icons.Default.Sync,
            title = "Sync now",
            subtitle = "Last synced on $lastSyncedText",
            onClick = {
                val formatter = SimpleDateFormat("MM/dd/yyyy, h:mm a", Locale.getDefault())
                lastSyncedText = formatter.format(Date())
            }
        )

        HorizontalDivider(color = OpusDivider.copy(alpha = 0.5f), thickness = 0.5.dp)

        // Features Section
        SectionHeader("Features")
        MoreItemRow(
            icon = Icons.Default.Bookmark,
            title = "Notebooks",
            subtitle = "View notes and highlights across your library",
            onClick = onNotebooksClick
        )
        MoreItemRow(
            icon = Icons.Default.EmojiEvents,
            title = "Reading Challenges",
            subtitle = "Track your reading goals and achievements",
            onClick = onChallengesClick
        )
        MoreItemRow(
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            title = "Your Lists",
            subtitle = "Manage reading lists and wishlists",
            onClick = onListsClick
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = OpusTextSecondary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(
            start = OpusSpacing.standard,
            end = OpusSpacing.standard,
            top = OpusSpacing.standard,
            bottom = OpusSpacing.xsmall
        )
    )
}

@Composable
private fun MoreItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OpusTextSecondary,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = OpusSpacing.standard)
        ) {
            Text(
                text = title,
                color = OpusTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = OpusTextTertiary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(name = "03 More Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun MoreScreenPreview() {
    OpusReaderTheme {
        MoreScreen()
    }
}
