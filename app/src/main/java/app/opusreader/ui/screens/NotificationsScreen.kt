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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.NotificationItem
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem> = SampleBooks.sampleNotifications,
    onBackClick: () -> Unit = {},
    onClearAll: () -> Unit = {},
    onDeleteNotification: (String) -> Unit = {},
    onItemClick: (NotificationItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
    ) {
        OpusTopBar(
            title = "Notifications",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navigationIconDescription = "Back",
            onNavigationClick = onBackClick,
            actions = {
                if (notifications.isNotEmpty()) {
                    IconButton(onClick = onClearAll) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear all notifications",
                            tint = OpusTextPrimary
                        )
                    }
                }
            }
        )

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No notifications",
                    color = OpusTextSecondary,
                    fontSize = 15.sp
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notifications, key = { it.id }) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item) }
                            .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                color = OpusTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(OpusSpacing.xxsmall))
                            Text(
                                text = item.description,
                                color = OpusTextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(OpusSpacing.xxsmall))
                            Text(
                                text = item.dateText,
                                color = OpusTextTertiary,
                                fontSize = 11.sp
                            )
                        }

                        IconButton(
                            onClick = { onDeleteNotification(item.id) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete notification",
                                tint = OpusTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        color = OpusDivider.copy(alpha = 0.5f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = OpusSpacing.standard)
                    )
                }
            }
        }
    }
}

@Preview(name = "09 Notifications Screen Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun NotificationsScreenPreview() {
    OpusReaderTheme {
        NotificationsScreen()
    }
}
