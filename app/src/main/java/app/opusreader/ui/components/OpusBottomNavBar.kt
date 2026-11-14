package app.opusreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.navigation.NavRoutes
import app.opusreader.ui.theme.OpusBottomBar
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

enum class MainTab(val title: String, val route: String, val icon: ImageVector) {
    HOME("Home", NavRoutes.HOME, Icons.Default.Home),
    LIBRARY("Library", NavRoutes.LIBRARY_GRID, Icons.AutoMirrored.Filled.MenuBook),
    MORE("More", NavRoutes.MORE, Icons.Default.MoreHoriz)
}

@Composable
fun OpusBottomNavBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
    showCoverSpacer: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(OpusBottomBar)
    ) {
        HorizontalDivider(color = OpusDivider.copy(alpha = 0.5f), thickness = 0.5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(OpusSpacing.bottomNavHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Space reserved for floating resume cover on bottom left
            if (showCoverSpacer) {
                Spacer(modifier = Modifier.width(80.dp))
            }

            MainTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onTabSelected(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) OpusTextPrimary else OpusTextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = tab.title,
                            color = if (isSelected) OpusTextPrimary else OpusTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "OpusBottomNavBar Preview", showBackground = true, backgroundColor = 0xFF141414)
@Composable
fun OpusBottomNavBarPreview() {
    OpusReaderTheme {
        OpusBottomNavBar(
            selectedTab = MainTab.LIBRARY,
            onTabSelected = {}
        )
    }
}
