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
    val isRead: Boolean = false,
    val contentType: ContentType = ContentType.BOOK,
    val collectionIds: List<String> = emptyList()
)

enum class ContentType {
    BOOK, SAMPLE, DOC
}

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
            contentType = ContentType.BOOK
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
            contentType = ContentType.BOOK
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
            contentType = ContentType.BOOK
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
            contentType = ContentType.BOOK
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
            contentType = ContentType.SAMPLE
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
            contentType = ContentType.DOC
        )
    )
}
