package app.opusreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun OpusBottomSheetHeader(
    title: String,
    modifier: Modifier = Modifier,
    doneButtonText: String? = "DONE",
    onDoneClick: (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = OpusTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (doneButtonText != null && onDoneClick != null) {
                TextButton(
                    onClick = onDoneClick,
                    modifier = Modifier.height(OpusSpacing.minTouchTarget)
                ) {
                    Text(
                        text = doneButtonText,
                        color = OpusTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
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
        HorizontalDivider(color = OpusDivider, thickness = 1.dp)
    }
}

@Composable
fun OpusBottomSheetContainer(
    title: String,
    modifier: Modifier = Modifier,
    doneButtonText: String? = "DONE",
    onDoneClick: (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null,
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
            onCloseClick = onCloseClick
        )
        content()
        Spacer(modifier = Modifier.height(OpusSpacing.large))
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
