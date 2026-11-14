package app.opusreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

@Composable
fun OpusCheckboxRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .padding(horizontal = OpusSpacing.screenMarginHorizontal, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontFamily = AmazonEmberFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = OpusTextPrimary
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontFamily = AmazonEmberFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    color = OpusTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.width(OpusSpacing.medium))

        // Measured square checkbox (53 px = 20.2 dp)
        val boxSize = 53.refPx
        val boxShape = RoundedCornerShape(2.dp)

        Box(
            modifier = Modifier
                .size(boxSize)
                .clip(boxShape)
                .then(
                    if (checked) {
                        Modifier.background(OpusAccentBlue)
                    } else {
                        Modifier
                            .background(Color.Transparent)
                            .border(1.5.dp, OpusTextSecondary, boxShape)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Preview(name = "OpusCheckboxRow Checked", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusCheckboxRowCheckedPreview() {
    OpusReaderTheme {
        OpusCheckboxRow(
            title = "Download to SD card",
            subtitle = "Save downloaded books to external SD card storage",
            checked = true,
            onCheckedChange = {}
        )
    }
}

@Preview(name = "OpusCheckboxRow Unchecked", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusCheckboxRowUncheckedPreview() {
    OpusReaderTheme {
        OpusCheckboxRow(
            title = "Whispersync for Books",
            subtitle = "Automatically synchronize your reading progress",
            checked = false,
            onCheckedChange = {}
        )
    }
}
