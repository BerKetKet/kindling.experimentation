package app.opusreader.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun OpusCoverTile(
    title: String,
    author: String,
    modifier: Modifier = Modifier,
    tileWidth: Dp = OpusSpacing.gridCoverWidth,
    tileHeight: Dp = OpusSpacing.gridCoverHeight,
    progressPercent: Int = 0,
    isDownloaded: Boolean = true,
    coverColor: Color = OpusSurfaceVariant,
    coverDrawableRes: Int? = null,
    ribbonText: String? = null,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(3.dp)

    Box(
        modifier = modifier
            .width(tileWidth)
            .height(tileHeight)
            .clip(shape)
            .background(coverColor)
            .clickable { onClick() },
        contentAlignment = Alignment.TopCenter
    ) {
        if (coverDrawableRes != null) {
            Image(
                painter = painterResource(id = coverDrawableRes),
                contentDescription = title,
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            // Book cover title placeholder
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = OpusTextPrimary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = author,
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = OpusTextSecondary,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Ribbon in top corner (e.g. SAMPLE or NEW)
            if (ribbonText != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(
                            Color(0xFFCC8800),
                            RoundedCornerShape(bottomEnd = 4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = ribbonText.uppercase(),
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 8.sp
                    )
                }
            }

            // Downloaded badge at bottom-right
            if (isDownloaded) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Downloaded",
                    tint = OpusAccentBlue,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .size(16.dp)
                )
            }

            // Progress bar at very bottom
            if (progressPercent > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
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
    }
}

@Preview(name = "OpusCoverTile Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusCoverTilePreview() {
    OpusReaderTheme {
        OpusCoverTile(
            title = "Gardens of the Moon",
            author = "Steven Erikson",
            progressPercent = 10,
            isDownloaded = true,
            coverColor = Color(0xFF4C3E28),
            onClick = {}
        )
    }
}
