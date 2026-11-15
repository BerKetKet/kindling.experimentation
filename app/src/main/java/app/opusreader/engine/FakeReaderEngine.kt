package app.opusreader.engine

import app.opusreader.data.ReaderSettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FakeReaderEngine(
    sampleTitle: String = "Gardens of the Moon",
    private val settingsDataStore: ReaderSettingsDataStore? = null,
    private val scope: CoroutineScope? = null
) : ReaderEngine {

    private val samplePages = listOf(
        """Preface to Gardens of the Moon — redux

There is no point in beginning something without ambition. In so many aspects of my life I have held to that notion, and it has led to more than one fiery crash through the years. I still recall, with some bitterness, the response Cam (Ian C. Esslemont) and I received when flogging our co-written feature film and television scripts: ‘Wonderful! Unique! Very cinematic!’ followed immediately by: ‘We can’t do this.’

And why couldn’t they? Because it was too big. Too ambitious. Too expensive.""",

        """The Malazan world had its genesis in a roleplaying game, a variant of Advanced Dungeons & Dragons, that Cam and I cooked up back in 1982. We were both archaeologists and anthropologists by training, and that background informed every aspect of the world-building.

We wanted civilizations with depth, ruins atop ruins, languages layered like geological strata, and gods who were as flawed and desperate as the mortals who worshipped them.""",

        """The city of Pale had held out for three years against the Malazan 2nd Army, under the command of High Fist Dujek Onearm. Now, the final assault was poised. Above the city hovered Moon's Spawn, the obsidian fortress of Anomander Rake, Lord of Moon's Spawn and Knight of High House Dark.

Sorcery crackled in the afternoon sky, smelling of ozone and crushed stone. On the plain below, the Bridgeburners prepared their sapping tunnels.""",

        """A soft knock diverted his attention. “Yes?”

The runner stood in the doorway, breathing hard, dusting ash from his sleeves. “Captain Paran? The Adjunct requires your presence at the command tent immediately.”

Paran stood, buckling his sword-belt. Outside, the sky burned with the unnatural violet of sorcery unleashed."""
    )

    private val _settings = MutableStateFlow(ReaderSettings())
    override val settings: StateFlow<ReaderSettings> = _settings.asStateFlow()

    init {
        if (settingsDataStore != null && scope != null) {
            scope.launch {
                settingsDataStore.settingsFlow.collect { persisted ->
                    _settings.value = persisted
                }
            }
        }
    }

    private val _currentLocation = MutableStateFlow(
        ReaderLocation(
            current = 91,
            total = 879,
            chapterTitle = "Preface to Gardens of the Moon — redux"
        )
    )
    override val currentLocation: StateFlow<ReaderLocation> = _currentLocation.asStateFlow()

    private val pageIndex = MutableStateFlow(0)

    private val _currentText = MutableStateFlow(samplePages[0])
    override val currentText: StateFlow<String> = _currentText.asStateFlow()

    private val _isBookmarked = MutableStateFlow(false)
    override val isBookmarked: StateFlow<Boolean> = _isBookmarked.asStateFlow()

    private val _tableOfContents = MutableStateFlow(
        listOf(
            TocItem(
                id = "toc_1",
                title = "Preface to Gardens of the Moon — redux",
                level = 0,
                targetLocation = 1
            ),
            TocItem(
                id = "toc_2",
                title = "Foreword to Gardens of the Moon — 10th Anniversary",
                level = 0,
                targetLocation = 91
            ),
            TocItem(
                id = "toc_3",
                title = "Maps",
                level = 0,
                targetLocation = 140
            ),
            TocItem(
                id = "toc_4",
                title = "Dramatis Personae",
                level = 0,
                targetLocation = 180
            ),
            TocItem(
                id = "toc_5",
                title = "Prologue",
                level = 0,
                targetLocation = 220
            ),
            TocItem(
                id = "toc_6",
                title = "Book One: Pale",
                level = 0,
                targetLocation = 250,
                children = listOf(
                    TocItem("toc_6_1", "Chapter One", 1, 330),
                    TocItem("toc_6_2", "Chapter Two", 1, 410),
                    TocItem("toc_6_3", "Chapter Three", 1, 520),
                    TocItem("toc_6_4", "Chapter Four", 1, 600)
                )
            ),
            TocItem(
                id = "toc_7",
                title = "Book Two: Darujhistan",
                level = 0,
                targetLocation = 640,
                children = listOf(
                    TocItem("toc_7_1", "Chapter Five", 1, 650),
                    TocItem("toc_7_2", "Chapter Six", 1, 740),
                    TocItem("toc_7_3", "Chapter Seven", 1, 800)
                )
            ),
            TocItem(
                id = "toc_8",
                title = "Epilogue",
                level = 0,
                targetLocation = 850
            )
        )
    )
    override val tableOfContents: StateFlow<List<TocItem>> = _tableOfContents.asStateFlow()

    private val _annotations = MutableStateFlow(
        listOf(
            AnnotationItem(
                id = "ann_1",
                location = 3971,
                sectionTitle = "Book Two: Darujhistan",
                selectedText = "soft knock diverted his attention. “Yes?”",
                note = null,
                highlightColor = "YELLOW",
                type = AnnotationType.HIGHLIGHT,
                isFavorite = false,
                dateAdded = "November 12, 2026"
            ),
            AnnotationItem(
                id = "ann_2",
                location = 11240,
                sectionTitle = "Book One: Raraku",
                selectedText = "“Question,” Mallick Rel said. “Has the council begun?” “Not yet,” Coltaine said slowly. “We await my warlock.” The priest of",
                note = null,
                highlightColor = "BLUE",
                type = AnnotationType.BOOKMARK,
                isFavorite = false,
                dateAdded = "November 10, 2026"
            )
        )
    )
    override val annotations: StateFlow<List<AnnotationItem>> = _annotations.asStateFlow()

    override fun nextPage(): Boolean {
        if (pageIndex.value < samplePages.lastIndex) {
            pageIndex.update { it + 1 }
            _currentText.value = samplePages[pageIndex.value]
            val newLoc = (91 + (pageIndex.value * 120)).coerceAtMost(879)
            _currentLocation.update { it.copy(current = newLoc) }
            return true
        }
        return false
    }

    override fun prevPage(): Boolean {
        if (pageIndex.value > 0) {
            pageIndex.update { it - 1 }
            _currentText.value = samplePages[pageIndex.value]
            val newLoc = (91 + (pageIndex.value * 120)).coerceAtLeast(1)
            _currentLocation.update { it.copy(current = newLoc) }
            return true
        }
        return false
    }

    override fun jumpToLocation(location: Int) {
        val clamped = location.coerceIn(1, 879)
        _currentLocation.update { it.copy(current = clamped) }
        val idx = ((clamped - 1) * samplePages.size / 879).coerceIn(0, samplePages.lastIndex)
        pageIndex.value = idx
        _currentText.value = samplePages[idx]
    }

    override fun toggleBookmark(): Boolean {
        _isBookmarked.update { !it }
        return _isBookmarked.value
    }

    override fun updateSettings(update: (ReaderSettings) -> ReaderSettings) {
        _settings.update { current ->
            val updated = update(current)
            scope?.launch {
                settingsDataStore?.saveSettings(updated)
            }
            updated
        }
    }

    override suspend fun searchInBook(query: String): List<ReaderSearchResult> {
        if (query.isBlank()) return emptyList()
        val results = mutableListOf<ReaderSearchResult>()
        samplePages.forEachIndexed { idx, text ->
            if (text.contains(query, ignoreCase = true)) {
                results.add(
                    ReaderSearchResult(
                        location = 91 + (idx * 120),
                        previewText = text.lines().firstOrNull() ?: text,
                        matchText = query,
                        chapterTitle = "Chapter ${idx + 1}"
                    )
                )
            }
        }
        return results
    }

    override fun addAnnotation(annotation: AnnotationItem) {
        _annotations.update { listOf(annotation) + it }
    }

    override fun removeAnnotation(id: String) {
        _annotations.update { it.filterNot { a -> a.id == id } }
    }

    override fun toggleFavoriteAnnotation(id: String) {
        _annotations.update { list ->
            list.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }
}
