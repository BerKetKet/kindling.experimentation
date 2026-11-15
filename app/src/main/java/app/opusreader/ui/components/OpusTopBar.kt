package app.opusreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTopBar
import app.opusreader.ui.theme.refPx

@Composable
fun OpusTopBar(
    title: String,
    modifier: Modifier = Modifier,
    centerTitle: Boolean = false,
    navigationIcon: ImageVector? = null,
    navigationIconDescription: String? = "Navigate back",
    onNavigationClick: (() -> Unit)? = null,
    backgroundColor: Color = OpusTopBar,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .statusBarsPadding()
    ) {
        // Measured distance from status bar bottom (y=90) to title band top (y=145): 55 px
        Spacer(modifier = Modifier.height(55.refPx))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.refPx)
                .padding(
                    start = if (navigationIcon != null) 44.refPx else 44.refPx,
                    end = 44.refPx
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            if (centerTitle) {
                Text(
                    text = title,
                    fontFamily = BookerlyFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = OpusTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navigationIcon != null && onNavigationClick != null) {
                    IconButton(
                        onClick = onNavigationClick,
                        modifier = Modifier.size(48.refPx)
                    ) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = navigationIconDescription,
                            tint = OpusTextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(36.refPx))
                }

                if (!centerTitle) {
                    Text(
                        text = title,
                        fontFamily = BookerlyFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = OpusTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }

        // Measured distance from title band bottom (y=196) to search bar top (y=254): 58 px
        Spacer(modifier = Modifier.height(58.refPx))
    }
}

@Preview(name = "OpusTopBar with Back Button", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusTopBarBackPreview() {
    OpusReaderTheme {
        OpusTopBar(
            title = "Settings",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = {}
        )
    }
}

@Preview(name = "OpusTopBar Title Only", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusTopBarTitlePreview() {
    OpusReaderTheme {
        OpusTopBar(
            title = "Library"
        )
    }
}
