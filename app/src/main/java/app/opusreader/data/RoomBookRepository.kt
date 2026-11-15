package app.opusreader.data

import android.content.Context
import app.opusreader.data.db.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RoomBookRepository(
    private val db: AppDatabase,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : BookRepository {

    private val _filterState = MutableStateFlow(LibraryFilterState())
    override val filterState: StateFlow<LibraryFilterState> = _filterState.asStateFlow()

    private val _sortOrder = MutableStateFlow(LibrarySortOrder.MOST_RECENT)
    override val sortOrder: StateFlow<LibrarySortOrder> = _sortOrder.asStateFlow()

    private val _viewMode = MutableStateFlow(LibraryViewMode.GRID)
    override val viewMode: StateFlow<LibraryViewMode> = _viewMode.asStateFlow()

    private val _collections = MutableStateFlow<List<CollectionItem>>(SampleBooks.sampleCollections)
    override val collections: StateFlow<List<CollectionItem>> = _collections.asStateFlow()

    private val _allBooks = MutableStateFlow<List<Book>>(SampleBooks.sampleList)
    override val allBooks: StateFlow<List<Book>> = _allBooks.asStateFlow()

    private val _filteredBooks = MutableStateFlow<List<Book>>(computeFilteredBooks(SampleBooks.sampleList, _filterState.value, _sortOrder.value))
    override val filteredBooks: StateFlow<List<Book>> = _filteredBooks.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(SampleBooks.sampleNotifications)
    override val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    init {
        scope.launch {
            // Seed sample books if database is empty
            val initialBookList = db.bookDao().getAllBooksFlow().first()
            if (initialBookList.isEmpty()) {
                db.bookDao().insertBooks(SampleBooks.sampleList.map { BookEntity.fromBook(it) })
                SampleBooks.sampleCollections.forEach { col ->
                    db.collectionDao().insertCollection(CollectionEntity(id = col.id, name = col.name))
                    col.bookIds.forEach { bId ->
                        db.collectionDao().insertCrossRef(BookCollectionCrossRef(bookId = bId, collectionId = col.id))
                    }
                }
                db.notificationDao().insertNotifications(SampleBooks.sampleNotifications.map {
                    NotificationEntity.fromNotificationItem(it)
                })
            }

            // Observe books & collection cross-refs
            combine(
                db.bookDao().getAllBooksFlow(),
                db.collectionDao().getAllCrossRefsFlow()
            ) { entities, crossRefs ->
                val colMap = crossRefs.groupBy({ it.bookId }, { it.collectionId })
                entities.map { it.toBook(colMap[it.id] ?: emptyList()) }
            }.collect { books ->
                _allBooks.value = books
                refreshFilteredBooks()
            }
        }

        scope.launch {
            // Observe collections
            combine(
                db.collectionDao().getAllCollectionsFlow(),
                db.collectionDao().getAllCrossRefsFlow()
            ) { cols, refs ->
                val bookIdsByCol = refs.groupBy({ it.collectionId }, { it.bookId })
                cols.map { col ->
                    val ids = bookIdsByCol[col.id] ?: emptyList()
                    CollectionItem(
                        id = col.id,
                        name = col.name,
                        bookCount = ids.size,
                        bookIds = ids
                    )
                }
            }.collect {
                _collections.value = it
            }
        }

        scope.launch {
            // Observe notifications
            db.notificationDao().getAllNotificationsFlow().collect { entities ->
                _notifications.value = entities.map { it.toNotificationItem() }
            }
        }
    }

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
        val item = CollectionItem(id = newId, name = name, bookCount = 0)
        scope.launch {
            db.collectionDao().insertCollection(CollectionEntity(id = newId, name = name))
        }
        return item
    }

    override fun deleteCollection(id: String) {
        scope.launch {
            db.collectionDao().deleteCollection(id)
        }
    }

    override fun renameCollection(id: String, newName: String) {
        scope.launch {
            db.collectionDao().renameCollection(id, newName)
        }
    }

    override fun toggleBookInCollection(bookId: String, collectionId: String) {
        scope.launch {
            val refs = db.collectionDao().getAllCrossRefsFlow().first()
            val exists = refs.any { it.bookId == bookId && it.collectionId == collectionId }
            if (exists) {
                db.collectionDao().removeBookFromCollection(bookId, collectionId)
            } else {
                db.collectionDao().insertCrossRef(BookCollectionCrossRef(bookId = bookId, collectionId = collectionId))
            }
        }
    }

    override fun toggleDownload(bookId: String) {
        scope.launch {
            val book = db.bookDao().getBookById(bookId)
            if (book != null) {
                db.bookDao().updateDownloadState(bookId, !book.isDownloaded)
            }
        }
    }

    override fun toggleRead(bookId: String) {
        scope.launch {
            val book = db.bookDao().getBookById(bookId)
            if (book != null) {
                db.bookDao().updateReadState(bookId, !book.isRead)
            }
        }
    }

    fun markRead(bookId: String, isRead: Boolean) {
        scope.launch {
            db.bookDao().updateReadState(bookId, isRead)
        }
    }

    override fun updateProgress(bookId: String, progress: Int) {
        scope.launch {
            db.bookDao().updateProgress(bookId, progress)
        }
    }

    override fun addBook(book: Book) {
        scope.launch {
            db.bookDao().insertBook(BookEntity.fromBook(book))
            book.collectionIds.forEach { cId ->
                db.collectionDao().insertCrossRef(BookCollectionCrossRef(bookId = book.id, collectionId = cId))
            }
        }
    }

    override fun addNotification(notification: NotificationItem) {
        scope.launch {
            db.notificationDao().insertNotifications(listOf(NotificationEntity.fromNotificationItem(notification)))
        }
    }

    override fun deleteNotification(id: String) {
        scope.launch {
            db.notificationDao().deleteNotificationById(id)
        }
    }

    override fun clearAllNotifications() {
        scope.launch {
            db.notificationDao().clearAll()
        }
    }

    override fun markAllNotificationsRead() {
        scope.launch {
            db.notificationDao().markAllAsRead()
        }
    }

    private fun refreshFilteredBooks() {
        _filteredBooks.value = computeFilteredBooks(_allBooks.value, _filterState.value, _sortOrder.value)
    }

    private fun computeFilteredBooks(
        books: List<Book>,
        filter: LibraryFilterState,
        sort: LibrarySortOrder
    ): List<Book> {
        var list = books

        if (filter.showDownloaded) {
            list = list.filter { it.isDownloaded }
        }
        if (filter.showRead && !filter.showUnread) {
            list = list.filter { it.isRead }
        } else if (filter.showUnread && !filter.showRead) {
            list = list.filter { !it.isRead }
        }
        val typeFilters = mutableListOf<ContentType>()
        if (filter.showBooks) typeFilters.add(ContentType.BOOK)
        if (filter.showSamples) typeFilters.add(ContentType.SAMPLE)
        if (filter.showDocs) typeFilters.add(ContentType.DOC)
        if (typeFilters.isNotEmpty()) {
            list = list.filter { it.contentType in typeFilters }
        }

        return when (sort) {
            LibrarySortOrder.MOST_RECENT -> list.sortedByDescending { it.lastReadTimestamp }
            LibrarySortOrder.TITLE -> list.sortedBy { it.title.lowercase() }
            LibrarySortOrder.AUTHOR_AZ -> list.sortedBy { it.author.lowercase() }
            LibrarySortOrder.AUTHOR_ZA -> list.sortedByDescending { it.author.lowercase() }
        }
    }

    companion object {
        fun create(context: Context): RoomBookRepository {
            return RoomBookRepository(AppDatabase.getInstance(context))
        }
    }
}
