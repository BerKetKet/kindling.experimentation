package app.opusreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTopBar

@Composable
fun OpusTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    navigationIconDescription: String? = "Navigate back",
    onNavigationClick: (() -> Unit)? = null,
    backgroundColor: Color = OpusTopBar,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(OpusSpacing.topBarHeight)
            .background(backgroundColor)
            .padding(horizontal = OpusSpacing.small),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(
                    onClick = onNavigationClick,
                    modifier = Modifier.size(OpusSpacing.minTouchTarget)
                ) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconDescription,
                        tint = OpusTextPrimary
                    )
                }
            }

            Text(
                text = title,
                color = OpusTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = OpusSpacing.small)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
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
