package app.opusreader.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary
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
