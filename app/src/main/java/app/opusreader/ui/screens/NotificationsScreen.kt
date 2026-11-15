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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.data.NotificationItem
import app.opusreader.data.SampleBooks
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.refPx

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import app.opusreader.ui.theme.LocalFixtureTrigger

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem> = SampleBooks.sampleNotifications,
    onBackClick: () -> Unit = {},
    onClearAll: () -> Unit = {},
    onDeleteNotification: (String) -> Unit = {},
    onItemClick: (NotificationItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val fixtureTrigger = LocalFixtureTrigger.current
    LaunchedEffect(fixtureTrigger) {
        listState.scrollToItem(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF404040))
    ) {
        // Top bar container
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(168.refPx)
                .background(Color(0xFF101112))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Notifications",
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = Color(0xFFF0F0F0),
                    modifier = Modifier.weight(1f)
                )

                if (notifications.isNotEmpty()) {
                    IconButton(
                        onClick = onClearAll,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_trash),
                            contentDescription = "Clear all notifications",
                            tint = Color(0xFFDBDCDC),
                            modifier = Modifier.size(24.dp)
                        )
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
                    fontFamily = AmazonEmberFontFamily,
                    color = Color(0xFFC6C6C6),
                    fontSize = 15.sp
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF404040))
            ) {
                items(notifications, key = { it.id }) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item) }
                            .padding(horizontal = 16.dp, vertical = 10.5.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        if (item.coverDrawableRes != null && item.coverDrawableRes != 0) {
                            Image(
                                painter = painterResource(id = item.coverDrawableRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2E3033)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (item.title.contains("Sync", ignoreCase = true)) {
                                        Icons.Default.Sync
                                    } else {
                                        Icons.Default.Notifications
                                    },
                                    contentDescription = null,
                                    tint = Color(0xFF2491FF),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontFamily = AmazonEmberFontFamily,
                                color = Color(0xFFF0F0F0),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.description,
                                fontFamily = AmazonEmberFontFamily,
                                color = Color(0xFFC6C6C6),
                                fontSize = 14.sp,
                                lineHeight = 19.sp,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.dateText,
                                fontFamily = AmazonEmberFontFamily,
                                color = Color(0xFFC6C6C6),
                                fontSize = 12.sp
                            )
                        }
                    }

                    HorizontalDivider(
                        color = Color(0xFF525252),
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
