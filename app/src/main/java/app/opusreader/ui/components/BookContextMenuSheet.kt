package app.opusreader.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.OpusTextPrimary

@Composable
fun BookContextMenuSheet(
    book: Book,
    onDismissRequest: () -> Unit,
    onToggleRead: () -> Unit,
    onAddToCollection: () -> Unit,
    onToggleDownload: () -> Unit,
    onBookDetails: () -> Unit
) {
    OpusModalBottomSheet(
        title = book.title,
        onDismissRequest = onDismissRequest,
        showDragHandle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            ContextMenuItem(
                icon = if (book.isRead) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                title = if (book.isRead) "Mark as unread" else "Mark as read",
                onClick = {
                    onToggleRead()
                    onDismissRequest()
                }
            )
            ContextMenuItem(
                icon = Icons.Default.CollectionsBookmark,
                title = "Add to collection",
                onClick = {
                    onDismissRequest()
                    onAddToCollection()
                }
            )
            ContextMenuItem(
                icon = if (book.isDownloaded) Icons.Default.DeleteOutline else Icons.Default.FileDownload,
                title = if (book.isDownloaded) "Remove download" else "Download book",
                onClick = {
                    onToggleDownload()
                    onDismissRequest()
                }
            )
            ContextMenuItem(
                icon = Icons.Default.Info,
                title = "Book details",
                onClick = {
                    onDismissRequest()
                    onBookDetails()
                }
            )
        }
    }
}

@Composable
private fun ContextMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OpusTextPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontFamily = AmazonEmberFontFamily,
            color = OpusTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
