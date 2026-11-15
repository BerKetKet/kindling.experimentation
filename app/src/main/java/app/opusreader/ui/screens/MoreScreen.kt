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
import androidx.compose.runtime.LaunchedEffect
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
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var lastSyncedText by remember { mutableStateOf("11/14/2026, 7:43 PM") }

    val fixtureTrigger = app.opusreader.ui.theme.LocalFixtureTrigger.current
    val scrollState = rememberScrollState()
    LaunchedEffect(fixtureTrigger) {
        scrollState.scrollTo(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
            .verticalScroll(scrollState)
            .padding(bottom = 90.dp)
    ) {
        // Distance below status bar (y=90) to title band top (y=138): 48 px
        Spacer(modifier = Modifier.height(48.refPx))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.refPx)
                .padding(start = 44.refPx, end = 44.refPx),
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

            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = OpusTextPrimary
                )
            }
        }

        // Distance from top bar (196) to search bar (251): 55 px
        Spacer(modifier = Modifier.height(55.refPx))

        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Opus"
        )

        // Spacer from search (355) to Section 1 (499): 144 px (-8 px calibration)
        Spacer(modifier = Modifier.height(136.refPx))

        // Section 1: Sync (y: 499-538, height 40 px)
        Text(
            text = "Sync",
            color = OpusTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .height(40.refPx)
                .padding(start = 44.refPx)
        )

        // Spacer from Section 1 header (538) to Row 1 (631): 93 px
        Spacer(modifier = Modifier.height(93.refPx))

        // Row 1: Sync now (y: 631-726, height 96 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.refPx)
                .clickable {
                    val formatter = SimpleDateFormat("MM/dd/yyyy, h:mm a", Locale.getDefault())
                    lastSyncedText = formatter.format(Date())
                }
                .padding(start = 44.refPx, end = 44.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = null,
                tint = OpusTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sync now",
                    color = OpusTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.refPx))
                Text(
                    text = "Last synced on $lastSyncedText",
                    color = OpusTextTertiary,
                    fontSize = 13.sp
                )
            }
        }

        // Spacer from Row 1 (726) to Section 2 (865): 139 px
        Spacer(modifier = Modifier.height(139.refPx))

        // Section 2: Reading Insights (y: 865-906, height 42 px)
        Text(
            text = "Reading Insights",
            color = OpusTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .height(42.refPx)
                .padding(start = 44.refPx)
        )

        // Spacer from Section 2 header (906) to Row 2 (997): 91 px
        Spacer(modifier = Modifier.height(91.refPx))

        // Row 2: Reading Challenges (y: 997-1094, height 98 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(98.refPx)
                .clickable { onChallengesClick() }
                .padding(start = 44.refPx, end = 44.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = OpusTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Reading Challenges",
                    color = OpusTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.refPx))
                Text(
                    text = "Track your reading goals and achievements",
                    color = OpusTextTertiary,
                    fontSize = 13.sp
                )
            }
        }

        // Spacer from Row 2 (1094) to Section 3 (1235): 141 px
        Spacer(modifier = Modifier.height(141.refPx))

        // Section 3: Lists & Notes (y: 1235-1265, height 31 px)
        Text(
            text = "Lists & Notes",
            color = OpusTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .height(31.refPx)
                .padding(start = 44.refPx)
        )

        // Spacer from Section 3 header (1265) to Row 3 (1363): 98 px
        Spacer(modifier = Modifier.height(98.refPx))

        // Row 3: Your Lists (y: 1363-1409, height 47 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(47.refPx)
                .clickable { onListsClick() }
                .padding(start = 44.refPx, end = 44.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                contentDescription = null,
                tint = OpusTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Your Lists",
                color = OpusTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // Spacer from Row 3 (1409) to Row 4 (1507): 98 px
        Spacer(modifier = Modifier.height(98.refPx))

        // Row 4: Notebooks (y: 1507-1559, height 53 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.refPx)
                .clickable { onNotebooksClick() }
                .padding(start = 44.refPx, end = 44.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                tint = OpusTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Notebooks",
                color = OpusTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // Spacer from Row 4 (1559) to Section 4 (1706): 147 px
        Spacer(modifier = Modifier.height(147.refPx))

        // Section 4: (y: 1706-1746, height 41 px)
        Text(
            text = "Account & Devices",
            color = OpusTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .height(41.refPx)
                .padding(start = 44.refPx)
        )

        // Spacer from Section 4 header (1746) to Row 5 (1835): 89 px
        Spacer(modifier = Modifier.height(89.refPx))

        // Row 5 (y: 1835-1883, height 49 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(49.refPx)
                .clickable { onSettingsClick() }
                .padding(start = 44.refPx, end = 44.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = OpusTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Settings",
                color = OpusTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // Spacer from Row 5 (1883) to Row 6 (1985): 102 px
        Spacer(modifier = Modifier.height(102.refPx))

        // Row 6 (y: 1985-2031, height 47 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(47.refPx)
                .clickable { /* Info */ }
                .padding(start = 44.refPx, end = 44.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = null,
                tint = OpusTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "About Opus Reader",
                color = OpusTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }
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
