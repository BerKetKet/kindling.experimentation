package app.opusreader

import app.opusreader.data.ContentType
import app.opusreader.data.FakeBookRepository
import app.opusreader.data.LibraryFilterState
import app.opusreader.data.LibrarySortOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeBookRepositoryTest {

    private lateinit var repository: FakeBookRepository

    @Before
    fun setUp() {
        repository = FakeBookRepository()
    }

    @Test
    fun testInitialBooksLoaded() {
        val books = repository.allBooks.value
        assertEquals(10, books.size)
    }

    @Test
    fun testFilterOnlyReadBooks() {
        repository.setFilter(LibraryFilterState(showRead = true, showUnread = false))
        val filtered = repository.filteredBooks.value
        assertTrue(filtered.isNotEmpty())
        assertTrue(filtered.all { it.isRead })
    }

    @Test
    fun testFilterOnlyUnreadBooks() {
        repository.setFilter(LibraryFilterState(showRead = false, showUnread = true))
        val filtered = repository.filteredBooks.value
        assertTrue(filtered.isNotEmpty())
        assertTrue(filtered.all { !it.isRead })
    }

    @Test
    fun testFilterContentTypeSamplesOnly() {
        repository.setFilter(
            LibraryFilterState(
                showBooks = false,
                showSamples = true,
                showDocs = false
            )
        )
        val filtered = repository.filteredBooks.value
        assertTrue(filtered.all { it.contentType == ContentType.SAMPLE })
    }

    @Test
    fun testSortByTitle() {
        repository.setSortOrder(LibrarySortOrder.TITLE)
        val titles = repository.filteredBooks.value.map { it.title.lowercase() }
        val sortedTitles = titles.sorted()
        assertEquals(sortedTitles, titles)
    }

    @Test
    fun testSortByAuthorAZ() {
        repository.setSortOrder(LibrarySortOrder.AUTHOR_AZ)
        val authors = repository.filteredBooks.value.map { it.author.lowercase() }
        val sortedAuthors = authors.sorted()
        assertEquals(sortedAuthors, authors)
    }

    @Test
    fun testSortByAuthorZA() {
        repository.setSortOrder(LibrarySortOrder.AUTHOR_ZA)
        val authors = repository.filteredBooks.value.map { it.author.lowercase() }
        val sortedAuthors = authors.sortedDescending()
        assertEquals(sortedAuthors, authors)
    }

    @Test
    fun testCreateAndDeleteCollection() {
        val initialCount = repository.collections.value.size
        val created = repository.createCollection("Custom SciFi")
        assertEquals(initialCount + 1, repository.collections.value.size)
        assertEquals("Custom SciFi", created.name)

        repository.deleteCollection(created.id)
        assertEquals(initialCount, repository.collections.value.size)
    }

    @Test
    fun testUpdateProgressMarksRead() {
        val bookId = "gardens_of_the_moon"
        repository.updateProgress(bookId, 100)
        val book = repository.allBooks.value.first { it.id == bookId }
        assertTrue(book.isRead)
        assertEquals(100, book.progressPercent)
    }

    @Test
    fun testAddBookAndNotification() {
        val newBook = app.opusreader.data.Book(
            id = "test_custom_book",
            title = "Test Novel",
            author = "Test Author",
            progressPercent = 0,
            isDownloaded = true
        )
        val initialSize = repository.allBooks.value.size
        repository.addBook(newBook)
        assertEquals(initialSize + 1, repository.allBooks.value.size)
        assertEquals("test_custom_book", repository.allBooks.value.first().id)

        val newNotif = app.opusreader.data.NotificationItem(
            id = "test_notif",
            title = "Imported",
            description = "Test Novel imported",
            dateText = "Just now"
        )
        val initialNotifs = repository.notifications.value.size
        repository.addNotification(newNotif)
        assertEquals(initialNotifs + 1, repository.notifications.value.size)
        assertEquals("test_notif", repository.notifications.value.first().id)
    }

    @Test
    fun testToggleRead() {
        val bookId = "gardens_of_the_moon"
        val initialRead = repository.allBooks.value.first { it.id == bookId }.isRead
        repository.toggleRead(bookId)
        val toggled = repository.allBooks.value.first { it.id == bookId }.isRead
        assertEquals(!initialRead, toggled)
    }

    @Test
    fun testRenameCollection() {
        val created = repository.createCollection("Old Name")
        repository.renameCollection(created.id, "New Name")
        val updated = repository.collections.value.first { it.id == created.id }
        assertEquals("New Name", updated.name)
    }

    @Test
    fun testMarkAllNotificationsRead() {
        repository.markAllNotificationsRead()
        val allRead = repository.notifications.value.all { it.isRead }
        assertTrue(allRead)
    }
}
