package app.opusreader.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.Book
import app.opusreader.data.CollectionItem
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.BookerlyFontFamily
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun AddToCollectionDialog(
    book: Book,
    collections: List<CollectionItem>,
    onDismissRequest: () -> Unit,
    onToggleCollection: (collectionId: String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Add to Collection",
                fontFamily = BookerlyFontFamily,
                fontWeight = FontWeight.Bold,
                color = OpusTextPrimary,
                fontSize = 18.sp
            )
        },
        text = {
            if (collections.isEmpty()) {
                Text(
                    text = "No collections available. Create a collection from the Library Collections view.",
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextSecondary,
                    fontSize = 14.sp
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(collections, key = { it.id }) { col ->
                        val isMember = col.bookIds.contains(book.id) || book.collectionIds.contains(col.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleCollection(col.id) }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = col.name,
                                fontFamily = AmazonEmberFontFamily,
                                color = OpusTextPrimary,
                                fontSize = 15.sp,
                                modifier = Modifier.weight(1f)
                            )
                            if (isMember) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = OpusAccentBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("DONE", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = OpusSurface
    )
}
