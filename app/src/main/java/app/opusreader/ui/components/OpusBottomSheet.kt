package app.opusreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusDragHandle
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusScrim
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun OpusBottomSheetHeader(
    title: String,
    modifier: Modifier = Modifier,
    doneButtonText: String? = "DONE",
    onDoneClick: (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null,
    showDragHandle: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (showDragHandle) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(OpusDragHandle)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.screenMarginHorizontal, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontFamily = BookerlyFontFamily,
                fontWeight = FontWeight.Bold,
                color = OpusTextPrimary,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            if (doneButtonText != null && onDoneClick != null) {
                TextButton(
                    onClick = onDoneClick,
                    modifier = Modifier.height(OpusSpacing.minTouchTarget)
                ) {
                    Text(
                        text = doneButtonText,
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA3D7F5),
                        fontSize = 14.sp
                    )
                }
            } else if (onCloseClick != null) {
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier.size(OpusSpacing.minTouchTarget)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = OpusTextSecondary
                    )
                }
            }
        }
        HorizontalDivider(color = OpusBorder, thickness = 3.refPx)
    }
}

@Composable
fun OpusBottomSheetContainer(
    title: String,
    modifier: Modifier = Modifier,
    doneButtonText: String? = "DONE",
    onDoneClick: (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null,
    showDragHandle: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(OpusSurface)
    ) {
        OpusBottomSheetHeader(
            title = title,
            doneButtonText = doneButtonText,
            onDoneClick = onDoneClick,
            onCloseClick = onCloseClick,
            showDragHandle = showDragHandle
        )
        content()
        Spacer(modifier = Modifier.height(OpusSpacing.large))
    }
}

/**
 * Modal bottom sheet overlay with scrim and bottom-anchored container.
 */
@Composable
fun OpusModalBottomSheet(
    title: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    doneButtonText: String? = "DONE",
    showDragHandle: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OpusScrim)
            .clickable(onClick = onDismissRequest),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(modifier = Modifier.clickable(enabled = false) {}) {
            OpusBottomSheetContainer(
                title = title,
                modifier = modifier,
                doneButtonText = doneButtonText,
                onDoneClick = onDismissRequest,
                onCloseClick = onDismissRequest,
                showDragHandle = showDragHandle,
                content = content
            )
        }
    }
}

@Preview(name = "OpusBottomSheet Header Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusBottomSheetHeaderPreview() {
    OpusReaderTheme {
        OpusBottomSheetHeader(
            title = "View",
            doneButtonText = "DONE",
            onDoneClick = {}
        )
    }
}
