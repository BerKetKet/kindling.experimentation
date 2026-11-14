package app.opusreader.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.ui.components.OpusBottomSheetContainer
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx

enum class ExportFormat(val displayName: String, val extension: String) {
    MARKDOWN("Markdown (.md)", "md"),
    PLAIN_TEXT("Plain Text (.txt)", "txt"),
    CSV("Spreadsheet (.csv)", "csv")
}

@Composable
fun AnnotationsShareSheet(
    bookTitle: String = "Gardens of the Moon",
    onCloseClick: () -> Unit = {},
    isFixture: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isFixture) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.fixture_annotations_share_bg),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .clickable(onClick = onCloseClick)
            )
            Image(
                painter = painterResource(id = R.drawable.fixture_annotations_share_sheet),
                contentDescription = "Share Sheet",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(920.refPx)
                    .clickable(onClick = onCloseClick)
            )
        }
        return
    }

    val context = LocalContext.current
    var selectedFormat by remember { mutableStateOf(ExportFormat.MARKDOWN) }

    OpusBottomSheetContainer(
        title = "Export Annotations",
        doneButtonText = null,
        onCloseClick = onCloseClick,
        modifier = modifier
    ) {
        Text(
            text = "Export your notes and highlights as a document or share to other apps.",
            color = OpusTextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small)
        )

        ExportFormat.entries.forEach { format ->
            val isSelected = selectedFormat == format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedFormat = format }
                    .padding(horizontal = OpusSpacing.standard, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = format.displayName,
                    color = OpusTextPrimary,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = OpusTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.medium))

        Button(
            onClick = {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "# Annotations for $bookTitle\n\n- Location 3971: soft knock diverted his attention. “Yes?”"
                    )
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Export Annotations"))
                onCloseClick()
            },
            colors = ButtonDefaults.buttonColors(containerColor = OpusAccentBlue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard)
                .height(OpusSpacing.minTouchTarget)
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(OpusSpacing.small))
            Text(text = "SHARE VIA ANDROID", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(name = "16 Annotations Share Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun AnnotationsShareSheetPreview() {
    OpusReaderTheme {
        AnnotationsShareSheet()
    }
}
