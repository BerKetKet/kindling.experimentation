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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusBorder
import app.opusreader.ui.theme.OpusDragHandle
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusScrim
import app.opusreader.ui.theme.OpusSheetActionBlue
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun OpusBottomSheetHeader(
    title: String?,
    modifier: Modifier = Modifier,
    centerTitle: Boolean = false,
    doneButtonText: String? = "DONE",
    onDoneClick: (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null,
    showDragHandle: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (showDragHandle) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.refPx, bottom = 10.refPx),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 63.refPx, height = 11.refPx)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(OpusDragHandle)
                )
            }
        }

        if (title != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.refPx)
                    .padding(horizontal = 44.refPx),
                contentAlignment = Alignment.CenterStart
            ) {
                if (centerTitle) {
                    Text(
                        text = title,
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = OpusTextPrimary,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!centerTitle) {
                        Text(
                            text = title,
                            fontFamily = BookerlyFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = OpusTextPrimary,
                            fontSize = 17.sp,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    if (doneButtonText != null && onDoneClick != null) {
                        Text(
                            text = doneButtonText,
                            fontFamily = AmazonEmberFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = OpusSheetActionBlue,
                            fontSize = 13.5.sp,
                            modifier = Modifier
                                .clickable(onClick = onDoneClick)
                                .padding(vertical = 6.dp, horizontal = 4.dp)
                        )
                    } else if (onCloseClick != null) {
                        IconButton(
                            onClick = onCloseClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = OpusTextPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
            HorizontalDivider(color = OpusBorder, thickness = 3.refPx)
        }
    }
}

@Composable
fun OpusBottomSheetContainer(
    title: String?,
    modifier: Modifier = Modifier,
    centerTitle: Boolean = false,
    doneButtonText: String? = "DONE",
    onDoneClick: (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null,
    showDragHandle: Boolean = true,
    surfaceColor: Color = OpusSurface,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(surfaceColor)
    ) {
        OpusBottomSheetHeader(
            title = title,
            centerTitle = centerTitle,
            doneButtonText = doneButtonText,
            onDoneClick = onDoneClick,
            onCloseClick = onCloseClick,
            showDragHandle = showDragHandle
        )
        content()
    }
}

@Composable
fun OpusModalBottomSheet(
    title: String?,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    centerTitle: Boolean = false,
    doneButtonText: String? = "DONE",
    showDragHandle: Boolean = false,
    surfaceColor: Color = OpusSurface,
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
                centerTitle = centerTitle,
                modifier = modifier,
                doneButtonText = doneButtonText,
                onDoneClick = onDismissRequest,
                onCloseClick = onDismissRequest,
                showDragHandle = showDragHandle,
                surfaceColor = surfaceColor,
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
