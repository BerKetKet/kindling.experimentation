package app.opusreader

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import app.opusreader.data.Book
import app.opusreader.data.BookRepository
import app.opusreader.data.CatalogRepository
import app.opusreader.data.ContentType
import app.opusreader.data.FakeBookRepository
import app.opusreader.data.FakeCatalogRepository
import app.opusreader.data.NotificationItem
import app.opusreader.navigation.OpusNavHost
import app.opusreader.ui.theme.OpusReaderTheme

class MainActivity : ComponentActivity() {

    private val bookRepository: BookRepository = FakeBookRepository()
    private val catalogRepository: CatalogRepository = FakeCatalogRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        enableEdgeToEdge()
        setContent {
            OpusReaderTheme {
                OpusNavHost(
                    bookRepository = bookRepository,
                    catalogRepository = catalogRepository
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        val action = intent.action
        val uri: Uri? = when (action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(Intent.EXTRA_STREAM)
                }
            }
            else -> null
        }

        if (uri != null) {
            val fileName = queryDisplayName(uri) ?: "Imported Book"
            val cleanTitle = fileName.substringBeforeLast('.')
            val newBook = Book(
                id = "import_${System.currentTimeMillis()}",
                title = cleanTitle,
                author = "Imported File",
                series = null,
                progressPercent = 0,
                isDownloaded = true,
                fileSize = "3.2 MB",
                coverColor = Color(0xFF2E4034),
                isRead = false,
                contentType = if (fileName.endsWith(".pdf", ignoreCase = true)) ContentType.DOC else ContentType.BOOK,
                collectionIds = emptyList(),
                lastReadTimestamp = System.currentTimeMillis()
            )
            bookRepository.addBook(newBook)
            bookRepository.addNotification(
                NotificationItem(
                    id = "notif_${System.currentTimeMillis()}",
                    title = "Import Complete",
                    description = "$cleanTitle has been imported into your library.",
                    dateText = "Just now",
                    isRead = false
                )
            )
        }
    }

    private fun queryDisplayName(uri: Uri): String? {
        return runCatching {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    cursor.getString(nameIndex)
                } else null
            } ?: uri.lastPathSegment
        }.getOrNull()
    }
}

@Preview(name = "Main Activity Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun MainActivityPreview() {
    OpusReaderTheme {
        OpusNavHost()
    }
}
