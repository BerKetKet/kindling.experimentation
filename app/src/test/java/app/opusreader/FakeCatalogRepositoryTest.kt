package app.opusreader

import app.opusreader.data.FakeBookRepository
import app.opusreader.data.FakeCatalogRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeCatalogRepositoryTest {

    private lateinit var catalogRepository: FakeCatalogRepository
    private lateinit var bookRepository: FakeBookRepository

    @Before
    fun setUp() {
        catalogRepository = FakeCatalogRepository()
        bookRepository = FakeBookRepository()
    }

    @Test
    fun testPopularBooksLoaded() {
        val books = catalogRepository.popularBooks.value
        assertTrue(books.isNotEmpty())
        assertTrue(books.any { it.title.contains("Frankenstein") })
    }

    @Test
    fun testTopicFiltering() {
        val topics = catalogRepository.topicBooks.value
        assertTrue(topics.containsKey("Fiction"))
        assertTrue(topics.containsKey("Fantasy"))
        assertTrue(topics.containsKey("Classics"))
        val fictionBooks = topics["Fiction"] ?: emptyList()
        assertTrue(fictionBooks.isNotEmpty())
    }

    @Test
    fun testSearchCatalog() = runBlocking {
        catalogRepository.searchCatalog("Kafka")
        val results = catalogRepository.searchResults.value
        assertEquals(1, results.size)
        assertEquals("The Metamorphosis", results.first().title)

        catalogRepository.searchCatalog("")
        assertTrue(catalogRepository.searchResults.value.isEmpty())
    }

    @Test
    fun testDownloadBookToLibrary() = runBlocking {
        val popular = catalogRepository.popularBooks.value.first()
        val initialCount = bookRepository.allBooks.value.size
        val success = catalogRepository.downloadBookToLibrary(popular, bookRepository)
        assertTrue(success)
        assertEquals(initialCount + 1, bookRepository.allBooks.value.size)

        val added = bookRepository.allBooks.value.first { it.id == "gutenberg_${popular.id}" }
        assertNotNull(added)
        assertEquals(popular.title, added.title)
        assertTrue(added.isDownloaded)

        // Notification added
        val latestNotif = bookRepository.notifications.value.first()
        assertTrue(latestNotif.title.contains("Download Complete"))
    }
}
