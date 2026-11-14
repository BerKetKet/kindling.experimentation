package app.opusreader.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface BookRepository {
    val allBooks: StateFlow<List<Book>>
    val filteredBooks: StateFlow<List<Book>>
    val filterState: StateFlow<LibraryFilterState>
    val sortOrder: StateFlow<LibrarySortOrder>
    val viewMode: StateFlow<LibraryViewMode>
    val collections: StateFlow<List<CollectionItem>>
    val notifications: StateFlow<List<NotificationItem>>

    fun setFilter(filter: LibraryFilterState)
    fun setSortOrder(sortOrder: LibrarySortOrder)
    fun setViewMode(viewMode: LibraryViewMode)
    fun createCollection(name: String): CollectionItem
    fun deleteCollection(id: String)
    fun toggleBookInCollection(bookId: String, collectionId: String)
    fun toggleDownload(bookId: String)
    fun updateProgress(bookId: String, progress: Int)
    fun addBook(book: Book)
    fun addNotification(notification: NotificationItem)
    fun deleteNotification(id: String)
    fun clearAllNotifications()
}

class FakeBookRepository(
    initialBooks: List<Book> = SampleBooks.sampleList,
    initialCollections: List<CollectionItem> = SampleBooks.sampleCollections,
    initialNotifications: List<NotificationItem> = SampleBooks.sampleNotifications
) : BookRepository {

    private val _allBooks = MutableStateFlow(initialBooks)
    override val allBooks: StateFlow<List<Book>> = _allBooks.asStateFlow()

    private val _filterState = MutableStateFlow(LibraryFilterState())
    override val filterState: StateFlow<LibraryFilterState> = _filterState.asStateFlow()

    private val _sortOrder = MutableStateFlow(LibrarySortOrder.MOST_RECENT)
    override val sortOrder: StateFlow<LibrarySortOrder> = _sortOrder.asStateFlow()

    private val _viewMode = MutableStateFlow(LibraryViewMode.GRID)
    override val viewMode: StateFlow<LibraryViewMode> = _viewMode.asStateFlow()

    private val _collections = MutableStateFlow(initialCollections)
    override val collections: StateFlow<List<CollectionItem>> = _collections.asStateFlow()

    private val _notifications = MutableStateFlow(initialNotifications)
    override val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _filteredBooks = MutableStateFlow(computeFilteredBooks(initialBooks, _filterState.value, _sortOrder.value))
    override val filteredBooks: StateFlow<List<Book>> = _filteredBooks.asStateFlow()

    override fun setFilter(filter: LibraryFilterState) {
        _filterState.value = filter
        refreshFilteredBooks()
    }

    override fun setSortOrder(sortOrder: LibrarySortOrder) {
        _sortOrder.value = sortOrder
        refreshFilteredBooks()
    }

    override fun setViewMode(viewMode: LibraryViewMode) {
        _viewMode.value = viewMode
    }

    override fun createCollection(name: String): CollectionItem {
        val newId = "col_${System.currentTimeMillis()}"
        val newCol = CollectionItem(id = newId, name = name, bookCount = 0)
        _collections.value = _collections.value + newCol
        return newCol
    }

    override fun deleteCollection(id: String) {
        _collections.value = _collections.value.filter { it.id != id }
    }

    override fun toggleBookInCollection(bookId: String, collectionId: String) {
        _collections.value = _collections.value.map { col ->
            if (col.id == collectionId) {
                val exists = col.bookIds.contains(bookId)
                val newBookIds = if (exists) col.bookIds - bookId else col.bookIds + bookId
                col.copy(bookIds = newBookIds, bookCount = newBookIds.size)
            } else {
                col
            }
        }
    }

    override fun toggleDownload(bookId: String) {
        _allBooks.value = _allBooks.value.map {
            if (it.id == bookId) it.copy(isDownloaded = !it.isDownloaded) else it
        }
        refreshFilteredBooks()
    }

    override fun updateProgress(bookId: String, progress: Int) {
        val clamped = progress.coerceIn(0, 100)
        _allBooks.value = _allBooks.value.map {
            if (it.id == bookId) {
                it.copy(
                    progressPercent = clamped,
                    isRead = clamped >= 100,
                    lastReadTimestamp = System.currentTimeMillis()
                )
            } else {
                it
            }
        }
        refreshFilteredBooks()
    }

    override fun addBook(book: Book) {
        if (_allBooks.value.none { it.id == book.id }) {
            _allBooks.value = listOf(book) + _allBooks.value
            refreshFilteredBooks()
        }
    }

    override fun addNotification(notification: NotificationItem) {
        _notifications.value = listOf(notification) + _notifications.value
    }

    override fun deleteNotification(id: String) {
        _notifications.value = _notifications.value.filter { it.id != id }
    }

    override fun clearAllNotifications() {
        _notifications.value = emptyList()
    }

    private fun refreshFilteredBooks() {
        _filteredBooks.value = computeFilteredBooks(
            books = _allBooks.value,
            filter = _filterState.value,
            sort = _sortOrder.value
        )
    }

    private fun computeFilteredBooks(
        books: List<Book>,
        filter: LibraryFilterState,
        sort: LibrarySortOrder
    ): List<Book> {
        return books
            .filter { book ->
                val readMatch = if (book.isRead) filter.showRead else filter.showUnread
                val typeMatch = when (book.contentType) {
                    ContentType.BOOK -> filter.showBooks
                    ContentType.SAMPLE -> filter.showSamples
                    ContentType.DOC -> filter.showDocs
                }
                readMatch && typeMatch
            }
            .let { list ->
                when (sort) {
                    LibrarySortOrder.MOST_RECENT -> list.sortedByDescending { it.lastReadTimestamp }
                    LibrarySortOrder.TITLE -> list.sortedBy { it.title.lowercase() }
                    LibrarySortOrder.AUTHOR_AZ -> list.sortedBy { it.author.lowercase() }
                    LibrarySortOrder.AUTHOR_ZA -> list.sortedByDescending { it.author.lowercase() }
                }
            }
    }
}
