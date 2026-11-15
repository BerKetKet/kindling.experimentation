package app.opusreader.data

import androidx.compose.ui.graphics.Color

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val series: String? = null,
    val progressPercent: Int = 0,
    val isDownloaded: Boolean = true,
    val fileSize: String = "15 MB",
    val coverColor: Color = Color(0xFF384F38),
    val coverDrawableRes: Int? = null,
    val isRead: Boolean = false,
    val contentType: ContentType = ContentType.BOOK,
    val collectionIds: List<String> = emptyList(),
    val lastReadTimestamp: Long = 0L
)

enum class ContentType {
    BOOK, SAMPLE, DOC
}

enum class LibrarySortOrder(val displayName: String) {
    MOST_RECENT("Most Recent"),
    TITLE("Title"),
    AUTHOR_AZ("Author (A-Z)"),
    AUTHOR_ZA("Author (Z-A)")
}

enum class LibraryViewMode {
    GRID, LIST, COLLECTIONS
}

data class LibraryFilterState(
    val showRead: Boolean = true,
    val showUnread: Boolean = true,
    val showBooks: Boolean = true,
    val showSamples: Boolean = true,
    val showDocs: Boolean = true
)

data class CollectionItem(
    val id: String,
    val name: String,
    val bookCount: Int = 0,
    val bookIds: List<String> = emptyList()
)

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val dateText: String,
    val isRead: Boolean = false
)

object SampleBooks {
    val sampleList = listOf(
        Book(
            id = "gardens_of_the_moon",
            title = "Gardens of the Moon: Book One of The Malazan Book of the Fallen",
            author = "Erikson, Steven",
            series = "The Malazan Book of the Fallen",
            progressPercent = 10,
            isDownloaded = true,
            fileSize = "15 MB",
            coverColor = Color(0xFF4C3E28),
            isRead = false,
            contentType = ContentType.BOOK,
            collectionIds = listOf("favorites"),
            lastReadTimestamp = 1700000000000L
        ),
        Book(
            id = "complete_works_kafka",
            title = "Complete Works (Kafka, Franz)",
            author = "Kafka, Franz",
            series = null,
            progressPercent = 0,
            isDownloaded = true,
            fileSize = "8 MB",
            coverColor = Color(0xFF2E3A42),
            isRead = false,
            contentType = ContentType.BOOK,
            collectionIds = emptyList(),
            lastReadTimestamp = 1690000000000L
        ),
        Book(
            id = "odyssey_homer",
            title = "The Odyssey",
            author = "Homer",
            series = null,
            progressPercent = 45,
            isDownloaded = true,
            fileSize = "4 MB",
            coverColor = Color(0xFF3C2E3F),
            isRead = false,
            contentType = ContentType.BOOK,
            collectionIds = listOf("classics"),
            lastReadTimestamp = 1695000000000L
        ),
        Book(
            id = "hyperion_simmons",
            title = "Hyperion",
            author = "Simmons, Dan",
            series = "Hyperion Cantos",
            progressPercent = 100,
            isDownloaded = true,
            fileSize = "12 MB",
            coverColor = Color(0xFF4A2B2B),
            isRead = true,
            contentType = ContentType.BOOK,
            collectionIds = listOf("favorites", "scifi"),
            lastReadTimestamp = 1692000000000L
        ),
        Book(
            id = "the_road_mccarthy",
            title = "The Road",
            author = "McCarthy, Cormac",
            series = null,
            progressPercent = 0,
            isDownloaded = false,
            fileSize = "3 MB",
            coverColor = Color(0xFF3E3D2A),
            isRead = false,
            contentType = ContentType.SAMPLE,
            collectionIds = emptyList(),
            lastReadTimestamp = 1680000000000L
        ),
        Book(
            id = "notes_underground_dostoevsky",
            title = "Notes from Underground",
            author = "Dostoevsky, Fyodor",
            series = null,
            progressPercent = 100,
            isDownloaded = true,
            fileSize = "2 MB",
            coverColor = Color(0xFF243A3A),
            isRead = true,
            contentType = ContentType.DOC,
            collectionIds = listOf("classics"),
            lastReadTimestamp = 1691000000000L
        ),
        Book(
            id = "frankenstein_shelley",
            title = "Frankenstein",
            author = "Shelley, Mary",
            series = null,
            progressPercent = 0,
            isDownloaded = true,
            fileSize = "3.5 MB",
            coverColor = Color(0xFF1E3A2F),
            isRead = false,
            contentType = ContentType.BOOK,
            collectionIds = listOf("classics"),
            lastReadTimestamp = 1685000000000L
        ),
        Book(
            id = "pride_and_prejudice_austen",
            title = "Pride and Prejudice",
            author = "Austen, Jane",
            series = null,
            progressPercent = 100,
            isDownloaded = true,
            fileSize = "4.2 MB",
            coverColor = Color(0xFF4A344A),
            isRead = true,
            contentType = ContentType.BOOK,
            collectionIds = listOf("classics"),
            lastReadTimestamp = 1689000000000L
        ),
        Book(
            id = "moby_dick_melville",
            title = "Moby-Dick",
            author = "Melville, Herman",
            series = null,
            progressPercent = 15,
            isDownloaded = false,
            fileSize = "6 MB",
            coverColor = Color(0xFF22364A),
            isRead = false,
            contentType = ContentType.SAMPLE,
            collectionIds = emptyList(),
            lastReadTimestamp = 1675000000000L
        ),
        Book(
            id = "the_metamorphosis_kafka",
            title = "The Metamorphosis",
            author = "Kafka, Franz",
            series = null,
            progressPercent = 0,
            isDownloaded = true,
            fileSize = "1.8 MB",
            coverColor = Color(0xFF3A3026),
            isRead = false,
            contentType = ContentType.DOC,
            collectionIds = listOf("favorites"),
            lastReadTimestamp = 1682000000000L
        )
    )

    val sampleCollections = listOf(
        CollectionItem(
            id = "favorites",
            name = "All-Time Favorites",
            bookCount = 3,
            bookIds = listOf("gardens_of_the_moon", "hyperion_simmons", "the_metamorphosis_kafka")
        ),
        CollectionItem(
            id = "classics",
            name = "Gutenberg Classics",
            bookCount = 4,
            bookIds = listOf("odyssey_homer", "notes_underground_dostoevsky", "frankenstein_shelley", "pride_and_prejudice_austen")
        ),
        CollectionItem(
            id = "scifi",
            name = "Science Fiction",
            bookCount = 1,
            bookIds = listOf("hyperion_simmons")
        )
    )

    val sampleNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Library Synchronized",
            description = "Reading positions and annotations synced across all devices.",
            dateText = "Today, 7:43 PM",
            isRead = true
        ),
        NotificationItem(
            id = "notif_2",
            title = "Download Complete",
            description = "Gardens of the Moon (Book One) is ready to read offline.",
            dateText = "Yesterday, 3:15 PM",
            isRead = true
        ),
        NotificationItem(
            id = "notif_3",
            title = "Welcome to Opus Reader",
            description = "Your personal EPUB reader is configured and ready.",
            dateText = "Nov 14, 2026",
            isRead = false
        )
    )
}
