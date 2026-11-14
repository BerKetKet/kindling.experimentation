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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary
import app.opusreader.ui.theme.refPx

@Composable
fun OpusListRow(
    title: String,
    author: String,
    modifier: Modifier = Modifier,
    metaInfo: String? = null,
    progressPercent: Int = 0,
    isDownloaded: Boolean = true,
    coverColor: Color = OpusSurfaceVariant,
    onClick: () -> Unit = {},
    onMoreClick: (() -> Unit)? = null,
    onDownloadClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(OpusSpacing.listRowPitch)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 53.refPx, end = OpusSpacing.screenMarginHorizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book cover thumbnail (168px x 253px)
            Box(
                modifier = Modifier
                    .width(OpusSpacing.listCoverWidth)
                    .height(OpusSpacing.listCoverHeight)
                    .clip(RoundedCornerShape(3.dp))
                    .background(coverColor),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Book title placeholder
                Text(
                    text = title.take(24),
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = OpusTextPrimary.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(4.dp)
                )

                // Progress line
                if (progressPercent > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(Color(0xFF333333))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressPercent / 100f)
                                .background(OpusAccentBlue)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(OpusSpacing.standard))

            // Book details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = OpusTextPrimary,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = author.uppercase(),
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = OpusTextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (metaInfo != null || progressPercent > 0) {
                    Spacer(modifier = Modifier.height(3.dp))
                    val displayMeta = when {
                        metaInfo != null && progressPercent > 0 -> "$metaInfo · $progressPercent%"
                        metaInfo != null -> metaInfo
                        else -> "$progressPercent%"
                    }
                    Text(
                        text = displayMeta,
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = OpusTextTertiary,
                        fontSize = 11.sp
                    )
                }
            }

            // Status and actions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isDownloaded) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Downloaded",
                        tint = OpusAccentBlue,
                        modifier = Modifier.size(18.dp)
                    )
                } else if (onDownloadClick != null) {
                    IconButton(
                        onClick = onDownloadClick,
                        modifier = Modifier.size(OpusSpacing.minTouchTarget)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "Download book",
                            tint = OpusTextSecondary
                        )
                    }
                }

                if (onMoreClick != null) {
                    IconButton(
                        onClick = onMoreClick,
                        modifier = Modifier.size(OpusSpacing.minTouchTarget)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = OpusTextSecondary
                        )
                    }
                }
            }
        }

        // 3 px divider matching reference anchor (y: 318 row pitch)
        HorizontalDivider(
            color = OpusBorder,
            thickness = OpusSpacing.listDividerHeight
        )
    }
}

@Preview(name = "OpusListRow Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusListRowPreview() {
    OpusReaderTheme {
        OpusListRow(
            title = "Gardens of the Moon",
            author = "Steven Erikson",
            metaInfo = "Read · Kindle Edition",
            progressPercent = 10,
            isDownloaded = true
        )
    }
}
