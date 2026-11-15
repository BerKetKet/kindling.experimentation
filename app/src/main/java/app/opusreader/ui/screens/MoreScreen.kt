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
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.components.KindleNotebookIcon
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusHighlightBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.isOpusDarkTheme
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
    val isDark = isOpusDarkTheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
            .clipToBounds()
            .verticalScroll(rememberScrollState())
    ) {
        OpusTopBar(
            title = "More",
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = OpusTextPrimary
                    )
                }
            }
        )

        // Search pill: y=[251, 355], h=105 px
        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Kindle"
        )

        // Gap from search bar bottom (355) to "Sync" header top (499): 143 px
        Spacer(modifier = Modifier.height(143.refPx))

        // Section 1: "Sync" (y=[499, 538], h=40 px)
        SectionHeader("Sync")

        // In light_03_more.png, the Sync row has a #0274E6 highlight block at y=[577, 765]; in dark 03_more.png it is transparent
        if (!isDark) {
            Spacer(modifier = Modifier.height(38.refPx))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(189.refPx)
                    .background(OpusHighlightBlue)
                    .clickable {
                        val formatter = SimpleDateFormat("MM/dd/yyyy, h:mm a", Locale.getDefault())
                        lastSyncedText = formatter.format(Date())
                    }
                    .padding(horizontal = 47.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 42.refPx)
                ) {
                    Text(
                        text = "Sync now",
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = Color.White,
                            fontSize = 14.sp,
                            platformStyle = NoPaddingPlatformStyle
                        )
                    )
                    Spacer(modifier = Modifier.height(8.refPx))
                    Text(
                        text = "Last synced on $lastSyncedText",
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = Color(0xFFCAE2FA),
                            fontSize = 12.5.sp,
                            platformStyle = NoPaddingPlatformStyle
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(100.refPx))
        } else {
            // Dark mode: Row 1 at y=[631, 726] (92 px gap from Section 1 bottom 538)
            Spacer(modifier = Modifier.height(92.refPx))
            MoreItemRow(
                icon = Icons.Default.Sync,
                title = "Sync now",
                subtitle = "Last synced on $lastSyncedText",
                onClick = {
                    val formatter = SimpleDateFormat("MM/dd/yyyy, h:mm a", Locale.getDefault())
                    lastSyncedText = formatter.format(Date())
                }
            )
            // Gap from Row 1 bottom (726) to Section 2 top (865): 138 px
            Spacer(modifier = Modifier.height(138.refPx))
        }

        // Section 2: "Challenges" (y=[865, 906], h=42 px)
        SectionHeader("Challenges")
        Spacer(modifier = Modifier.height(90.refPx))

        // Row 2: Reading Challenges (y=[997, 1094], h=98 px)
        MoreItemRow(
            icon = Icons.Outlined.LocalFireDepartment,
            title = "Reading Challenges",
            subtitle = "Previously Reading Insights",
            onClick = onChallengesClick
        )

        // Gap from Row 2 bottom (1094) to Section 3 top (1235): 140 px
        Spacer(modifier = Modifier.height(140.refPx))

        // Section 3: "Your content" (y=[1235, 1265], h=31 px)
        SectionHeader("Your content")
        Spacer(modifier = Modifier.height(97.refPx))

        // Row 3: Your Lists (y=[1363, 1409], h=47 px)
        MoreSingleRow(
            icon = Icons.Outlined.FavoriteBorder,
            title = "Your Lists",
            onClick = onListsClick
        )

        Spacer(modifier = Modifier.height(97.refPx))

        // Row 4: Notebooks (y=[1507, 1559], h=53 px)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.refPx)
                .clickable(onClick = onNotebooksClick)
                .padding(horizontal = 47.refPx),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KindleNotebookIcon(
                tint = OpusTextSecondary,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = "Notebooks",
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 14.sp,
                    platformStyle = NoPaddingPlatformStyle
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 42.refPx)
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontFamily = AmazonEmberFontFamily,
            color = OpusTextPrimary,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Bold,
            platformStyle = NoPaddingPlatformStyle
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.refPx)
            .padding(horizontal = 44.refPx)
    )
}

@Composable
private fun MoreItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(97.refPx)
            .clickable(onClick = onClick)
            .padding(horizontal = 47.refPx),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OpusTextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 42.refPx)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
            Spacer(modifier = Modifier.height(8.refPx))
            Text(
                text = subtitle,
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextSecondary,
                    fontSize = 12.5.sp,
                    platformStyle = NoPaddingPlatformStyle
                )
            )
        }
    }
}

@Composable
private fun MoreSingleRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.refPx)
            .clickable(onClick = onClick)
            .padding(horizontal = 47.refPx),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OpusTextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = title,
            style = TextStyle(
                fontFamily = AmazonEmberFontFamily,
                color = OpusTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                platformStyle = NoPaddingPlatformStyle
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 42.refPx)
        )
    }
}

@Preview(name = "03 More Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun MoreScreenPreview() {
    OpusReaderTheme {
        MoreScreen()
    }
}
