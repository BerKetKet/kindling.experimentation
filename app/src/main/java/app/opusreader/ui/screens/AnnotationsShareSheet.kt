package app.opusreader.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusScrim
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFormat by remember { mutableStateOf(ExportFormat.MARKDOWN) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OpusScrim)
            .clickable(onClick = onCloseClick),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(923.refPx)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color(0xFF1A1B1C))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(169.refPx)
                        .padding(horizontal = 42.refPx),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Export Annotations",
                        style = TextStyle(
                            fontFamily = AmazonEmberFontFamily,
                            color = OpusTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            platformStyle = NoPaddingPlatformStyle
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(48.refPx)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = OpusTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider(color = OpusDivider, thickness = 3.refPx)

                Spacer(modifier = Modifier.height(38.refPx))
                Text(
                    text = "Export your notes and highlights as a document or share to other apps.",
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 13.5.sp,
                        lineHeight = 18.sp,
                        platformStyle = NoPaddingPlatformStyle
                    ),
                    modifier = Modifier.padding(horizontal = 44.refPx)
                )

                Spacer(modifier = Modifier.height(24.refPx))

                ExportFormat.entries.forEach { format ->
                    val isSelected = selectedFormat == format
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.refPx)
                            .clickable { selectedFormat = format }
                            .padding(horizontal = 44.refPx),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = format.displayName,
                            style = TextStyle(
                                fontFamily = AmazonEmberFontFamily,
                                color = OpusTextPrimary,
                                fontSize = 14.sp,
                                platformStyle = NoPaddingPlatformStyle
                            ),
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

                Spacer(modifier = Modifier.height(42.refPx))

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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6F2FF)),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 44.refPx)
                        .height(126.refPx)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color(0xFF1A1B1C),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(OpusSpacing.small))
                    Text(
                        text = "SHARE VIA ANDROID",
                        color = Color(0xFF1A1B1C),
                        fontFamily = AmazonEmberFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.refPx)
                    .background(Color(0xFF1A1B1C))
            )
        }
    }
}

@Preview(name = "16 Annotations Share Preview", showBackground = true, backgroundColor = 0xFF1A1B1C)
@Composable
fun AnnotationsShareSheetPreview() {
    OpusReaderTheme {
        AnnotationsShareSheet()
    }
}
