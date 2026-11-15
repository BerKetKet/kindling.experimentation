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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.NotificationItem
import app.opusreader.data.SampleBooks
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.isOpusDarkTheme
import app.opusreader.ui.theme.refPx

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem> = SampleBooks.sampleNotifications,
    onBackClick: () -> Unit = {},
    onClearAll: () -> Unit = {},
    onDeleteNotification: (String) -> Unit = {},
    onItemClick: (NotificationItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = isOpusDarkTheme
    val bodyBg = if (isDark) Color(0xFF404040) else Color.White
    val dividerCol = if (isDark) Color(0xFF525252) else Color(0xFFE0E0E0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bodyBg)
    ) {
        // Top header bar: y=[0, 250] (251 px total height)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(251.refPx)
                .background(OpusBackground)
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(58.refPx))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.refPx)
                    .padding(start = 51.refPx, end = 42.refPx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(46.refPx)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = OpusTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(36.refPx))
                Text(
                    text = "Notifications",
                    style = TextStyle(
                        fontFamily = BookerlyFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = OpusTextPrimary,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    modifier = Modifier.weight(1f)
                )
                if (notifications.isNotEmpty()) {
                    IconButton(onClick = onClearAll, modifier = Modifier.size(48.refPx)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear all notifications",
                            tint = OpusTextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clipToBounds()
            ) {
                items(notifications, key = { it.id }) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item) }
                            .padding(horizontal = 42.refPx, vertical = 46.refPx)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(105.refPx)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF7C5944)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "OPUS",
                                    fontFamily = BookerlyFontFamily,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(34.refPx))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = TextStyle(
                                        fontFamily = AmazonEmberFontFamily,
                                        color = OpusTextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        platformStyle = NoPaddingPlatformStyle
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.refPx))
                                Text(
                                    text = item.description,
                                    style = TextStyle(
                                        fontFamily = AmazonEmberFontFamily,
                                        color = OpusTextPrimary,
                                        fontSize = 13.sp,
                                        lineHeight = 17.sp,
                                        platformStyle = NoPaddingPlatformStyle
                                    )
                                )
                                Spacer(modifier = Modifier.height(18.refPx))
                                Text(
                                    text = item.dateText,
                                    style = TextStyle(
                                        fontFamily = AmazonEmberFontFamily,
                                        color = OpusTextSecondary,
                                        fontSize = 11.5.sp,
                                        platformStyle = NoPaddingPlatformStyle
                                    )
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
                    }

                    HorizontalDivider(
                        color = dividerCol,
                        thickness = 3.refPx,
                        modifier = Modifier.padding(horizontal = 32.refPx)
                    )
                }
            }
        }
    }
}

@Preview(name = "09 Notifications Screen Preview", showBackground = true, backgroundColor = 0xFF404040)
@Composable
fun NotificationsScreenPreview() {
    OpusReaderTheme {
        NotificationsScreen()
    }
}
