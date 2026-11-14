package app.opusreader.data

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class GutenbergBook(
    val id: Int,
    val title: String,
    val authors: List<String>,
    val subjects: List<String>,
    val coverUrl: String? = null,
    val epubUrl: String? = null,
    val downloadCount: Int = 0,
    val coverColor: Color = Color(0xFF384F38)
)

interface CatalogRepository {
    val popularBooks: StateFlow<List<GutenbergBook>>
    val topicBooks: StateFlow<Map<String, List<GutenbergBook>>>
    val searchResults: StateFlow<List<GutenbergBook>>
    val isLoading: StateFlow<Boolean>

    suspend fun loadPopularBooks()
    suspend fun loadTopic(topic: String)
    suspend fun searchCatalog(query: String)
    suspend fun downloadBookToLibrary(book: GutenbergBook, bookRepository: BookRepository): Boolean
}

class FakeCatalogRepository : CatalogRepository {

    private val sampleGutenbergList = listOf(
        GutenbergBook(
            id = 84,
            title = "Frankenstein; Or, The Modern Prometheus",
            authors = listOf("Shelley, Mary Wollstonecraft"),
            subjects = listOf("Science fiction", "Gothic fiction"),
            coverUrl = "https://www.gutenberg.org/cache/epub/84/pg84.cover.medium.jpg",
            epubUrl = "https://www.gutenberg.org/ebooks/84.epub3.images",
            downloadCount = 89450,
            coverColor = Color(0xFF2E4034)
        ),
        GutenbergBook(
            id = 1342,
            title = "Pride and Prejudice",
            authors = listOf("Austen, Jane"),
            subjects = listOf("Classic Literature", "Romance"),
            coverUrl = "https://www.gutenberg.org/cache/epub/1342/pg1342.cover.medium.jpg",
            epubUrl = "https://www.gutenberg.org/ebooks/1342.epub3.images",
            downloadCount = 65200,
            coverColor = Color(0xFF4A344A)
        ),
        GutenbergBook(
            id = 2701,
            title = "Moby Dick; Or, The Whale",
            authors = listOf("Melville, Herman"),
            subjects = listOf("Adventure", "Sea stories"),
            coverUrl = "https://www.gutenberg.org/cache/epub/2701/pg2701.cover.medium.jpg",
            epubUrl = "https://www.gutenberg.org/ebooks/2701.epub3.images",
            downloadCount = 42100,
            coverColor = Color(0xFF22364A)
        ),
        GutenbergBook(
            id = 11,
            title = "Alice's Adventures in Wonderland",
            authors = listOf("Carroll, Lewis"),
            subjects = listOf("Fantasy", "Children's literature"),
            coverUrl = "https://www.gutenberg.org/cache/epub/11/pg11.cover.medium.jpg",
            epubUrl = "https://www.gutenberg.org/ebooks/11.epub3.images",
            downloadCount = 38400,
            coverColor = Color(0xFF4C3E28)
        ),
        GutenbergBook(
            id = 345,
            title = "Dracula",
            authors = listOf("Stoker, Bram"),
            subjects = listOf("Horror", "Gothic fiction"),
            coverUrl = "https://www.gutenberg.org/cache/epub/345/pg345.cover.medium.jpg",
            epubUrl = "https://www.gutenberg.org/ebooks/345.epub3.images",
            downloadCount = 36900,
            coverColor = Color(0xFF4A2B2B)
        ),
        GutenbergBook(
            id = 1661,
            title = "The Adventures of Sherlock Holmes",
            authors = listOf("Doyle, Arthur Conan"),
            subjects = listOf("Detective and mystery stories"),
            coverUrl = "https://www.gutenberg.org/cache/epub/1661/pg1661.cover.medium.jpg",
            epubUrl = "https://www.gutenberg.org/ebooks/1661.epub3.images",
            downloadCount = 34100,
            coverColor = Color(0xFF2E3A42)
        ),
        GutenbergBook(
            id = 5200,
            title = "The Metamorphosis",
            authors = listOf("Kafka, Franz"),
            subjects = listOf("Psychological fiction"),
            coverUrl = "https://www.gutenberg.org/cache/epub/5200/pg5200.cover.medium.jpg",
            epubUrl = "https://www.gutenberg.org/ebooks/5200.epub3.images",
            downloadCount = 29500,
            coverColor = Color(0xFF3A3026)
        ),
        GutenbergBook(
            id = 174,
            title = "The Picture of Dorian Gray",
            authors = listOf("Wilde, Oscar"),
            subjects = listOf("Philosophy", "Gothic fiction"),
            coverUrl = "https://www.gutenberg.org/cache/epub/174/pg174.cover.medium.jpg",
            epubUrl = "https://www.gutenberg.org/ebooks/174.epub3.images",
            downloadCount = 28100,
            coverColor = Color(0xFF384F38)
        )
    )

    private val _popularBooks = MutableStateFlow(sampleGutenbergList)
    override val popularBooks: StateFlow<List<GutenbergBook>> = _popularBooks.asStateFlow()

    private val _topicBooks = MutableStateFlow<Map<String, List<GutenbergBook>>>(
        mapOf(
            "Fiction" to sampleGutenbergList.filter { it.subjects.any { s -> s.contains("Fiction", true) } },
            "Fantasy" to sampleGutenbergList.filter { it.subjects.any { s -> s.contains("Fantasy", true) } },
            "Classics" to sampleGutenbergList
        )
    )
    override val topicBooks: StateFlow<Map<String, List<GutenbergBook>>> = _topicBooks.asStateFlow()

    private val _searchResults = MutableStateFlow<List<GutenbergBook>>(emptyList())
    override val searchResults: StateFlow<List<GutenbergBook>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    override suspend fun loadPopularBooks() {
        // Already loaded
    }

    override suspend fun loadTopic(topic: String) {
        val filtered = sampleGutenbergList.filter {
            it.subjects.any { s -> s.contains(topic, ignoreCase = true) }
        }
        _topicBooks.value = _topicBooks.value + (topic to filtered)
    }

    override suspend fun searchCatalog(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        _isLoading.value = true
        _searchResults.value = sampleGutenbergList.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.authors.any { a -> a.contains(query, ignoreCase = true) } ||
                    it.subjects.any { s -> s.contains(query, ignoreCase = true) }
        }
        _isLoading.value = false
    }

    override suspend fun downloadBookToLibrary(
        book: GutenbergBook,
        bookRepository: BookRepository
    ): Boolean {
        val newBook = Book(
            id = "gutenberg_${book.id}",
            title = book.title,
            author = book.authors.firstOrNull() ?: "Unknown Author",
            series = null,
            progressPercent = 0,
            isDownloaded = true,
            fileSize = "2.4 MB",
            coverColor = book.coverColor,
            isRead = false,
            contentType = ContentType.BOOK,
            collectionIds = listOf("classics"),
            lastReadTimestamp = System.currentTimeMillis()
        )
        bookRepository.addBook(newBook)
        bookRepository.addNotification(
            NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                title = "Download Complete",
                description = "${book.title} has been added to your library.",
                dateText = "Just now",
                isRead = false
            )
        )
        return true
    }
}
