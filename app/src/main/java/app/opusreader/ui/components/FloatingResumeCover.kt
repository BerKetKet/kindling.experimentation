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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun FloatingResumeCover(
    title: String,
    author: String,
    progressPercent: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    coverColor: Color = Color(0xFF4C3E28),
    coverDrawableRes: Int? = null
) {
    val shape = RoundedCornerShape(3.dp)
    val elevation = if (coverDrawableRes != null) 0.dp else 8.dp

    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape)
            .width(OpusSpacing.floatingCoverWidth)
            .height(OpusSpacing.floatingCoverHeight)
            .clip(shape)
            .background(coverColor)
            .clickable { onClick() },
        contentAlignment = Alignment.BottomCenter
    ) {
        if (coverDrawableRes != null) {
            Image(
                painter = painterResource(id = coverDrawableRes),
                contentDescription = title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = OpusTextPrimary,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = author,
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = OpusTextSecondary,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (progressPercent > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color(0xFF222222))
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

@Preview(name = "FloatingResumeCover Preview", showBackground = true, backgroundColor = 0xFF141414)
@Composable
fun FloatingResumeCoverPreview() {
    OpusReaderTheme {
        FloatingResumeCover(
            title = "Gardens of the Moon",
            author = "STEVEN ERIKSON",
            progressPercent = 10,
            onClick = {}
        )
    }
}
