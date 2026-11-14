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
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary

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
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book cover thumbnail
            Box(
                modifier = Modifier
                    .width(OpusSpacing.listCoverWidth)
                    .height(OpusSpacing.listCoverHeight)
                    .clip(RoundedCornerShape(4.dp))
                    .background(coverColor),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Book title placeholder if no image
                Text(
                    text = title.take(24),
                    color = OpusTextSecondary,
                    fontSize = 11.sp,
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
                    color = OpusTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(OpusSpacing.xsmall))

                Text(
                    text = author.uppercase(),
                    color = OpusTextSecondary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (metaInfo != null || progressPercent > 0) {
                    Spacer(modifier = Modifier.height(OpusSpacing.xsmall))
                    val displayMeta = when {
                        metaInfo != null && progressPercent > 0 -> "$metaInfo · $progressPercent%"
                        metaInfo != null -> metaInfo
                        else -> "$progressPercent%"
                    }
                    Text(
                        text = displayMeta,
                        color = OpusTextTertiary,
                        fontSize = 12.sp
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
                        tint = OpusTextSecondary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(enabled = onDownloadClick != null) { onDownloadClick?.invoke() }
                    )
                }

                if (onMoreClick != null) {
                    IconButton(
                        onClick = onMoreClick,
                        modifier = Modifier.size(OpusSpacing.minTouchTarget)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Book options",
                            tint = OpusTextSecondary
                        )
                    }
                }
            }
        }
        HorizontalDivider(color = OpusDivider.copy(alpha = 0.5f), thickness = 0.5.dp)
    }
}

@Preview(name = "OpusListRow Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusListRowPreview() {
    OpusReaderTheme {
        OpusListRow(
            title = "Gardens of the Moon: Book One of The Malazan Book of the Fallen",
            author = "ERIKSON, STEVEN",
            metaInfo = "15 MB",
            progressPercent = 10,
            isDownloaded = true,
            coverColor = Color(0xFF384F38),
            onClick = {},
            onMoreClick = {}
        )
    }
}
