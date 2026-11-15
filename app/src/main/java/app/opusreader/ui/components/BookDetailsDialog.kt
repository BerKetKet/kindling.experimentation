package app.opusreader.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun BookDetailsDialog(
    book: Book,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Book Details",
                fontFamily = BookerlyFontFamily,
                fontWeight = FontWeight.Bold,
                color = OpusTextPrimary,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Title: ${book.title}", fontFamily = AmazonEmberFontFamily, color = OpusTextPrimary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Author: ${book.author}", fontFamily = AmazonEmberFontFamily, color = OpusTextSecondary, fontSize = 13.sp)
                if (book.series != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Series: ${book.series}", fontFamily = AmazonEmberFontFamily, color = OpusTextSecondary, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("File size: ${book.fileSize}", fontFamily = AmazonEmberFontFamily, color = OpusTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Progress: ${book.progressPercent}%", fontFamily = AmazonEmberFontFamily, color = OpusTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Status: ${if (book.isRead) "Read" else "Unread"}", fontFamily = AmazonEmberFontFamily, color = OpusTextSecondary, fontSize = 13.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("CLOSE", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = OpusSurface
    )
}
