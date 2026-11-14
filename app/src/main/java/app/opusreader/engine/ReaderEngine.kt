package app.opusreader.engine

import kotlinx.coroutines.flow.StateFlow

data class TocItem(
    val id: String,
    val title: String,
    val level: Int = 0,
    val targetLocation: Int = 0,
    val children: List<TocItem> = emptyList()
)

data class ReaderLocation(
    val current: Int,
    val total: Int,
    val chapterTitle: String,
    val progressPercent: Float = if (total > 0) (current.toFloat() / total * 100f) else 0f
)

data class ReaderSearchResult(
    val location: Int,
    val previewText: String,
    val matchText: String,
    val chapterTitle: String
)

data class AnnotationItem(
    val id: String,
    val location: Int,
    val sectionTitle: String,
    val selectedText: String,
    val note: String? = null,
    val highlightColor: String = "YELLOW", // YELLOW, BLUE, PINK, ORANGE
    val type: AnnotationType = AnnotationType.HIGHLIGHT,
    val isFavorite: Boolean = false,
    val dateAdded: String = "Nov 14, 2026"
)

enum class AnnotationType {
    HIGHLIGHT, NOTE, BOOKMARK, UNDERLINE
}

data class ReaderSettings(
    val fontFamily: String = "Bookerly",
    val fontSize: Int = 18,
    val lineSpacingMultiplier: Float = 1.3f,
    val marginPreset: Int = 1, // 0 = narrow, 1 = normal, 2 = wide
    val isJustified: Boolean = true,
    val pageColor: String = "BLACK", // WHITE, SEPIA, GREEN, BLACK
    val updatePageWithSystemTheme: Boolean = true,
    val continuousScrolling: Boolean = false,
    val orientationLock: Boolean = false,
    val brightness: Float = 0.8f,
    val autoBrightness: Boolean = true,
    val assistiveReaderEnabled: Boolean = false,
    val readingProgressDisplay: String = "LOCATION_PERCENT", // LOCATION_PERCENT, TIME_IN_CHAPTER, TIME_IN_BOOK, OFF
    val visibleClock: Boolean = true,
    val volumeKeysTurnPages: Boolean = false,
    val pageTurnAnimation: Boolean = true
)

interface ReaderEngine {
    val currentLocation: StateFlow<ReaderLocation>
    val currentText: StateFlow<String>
    val isBookmarked: StateFlow<Boolean>
    val settings: StateFlow<ReaderSettings>
    val tableOfContents: StateFlow<List<TocItem>>
    val annotations: StateFlow<List<AnnotationItem>>

    fun nextPage(): Boolean
    fun prevPage(): Boolean
    fun jumpToLocation(location: Int)
    fun toggleBookmark(): Boolean
    fun updateSettings(update: (ReaderSettings) -> ReaderSettings)
    suspend fun searchInBook(query: String): List<ReaderSearchResult>
    fun addAnnotation(annotation: AnnotationItem)
    fun removeAnnotation(id: String)
    fun toggleFavoriteAnnotation(id: String)
}
