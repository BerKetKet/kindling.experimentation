package app.opusreader.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.opusreader.R
import app.opusreader.data.Book
import app.opusreader.data.BookRepository
import app.opusreader.data.CatalogRepository
import app.opusreader.data.FakeBookRepository
import app.opusreader.data.FakeCatalogRepository
import app.opusreader.data.LibraryViewMode
import app.opusreader.data.NotificationItem
import app.opusreader.data.ReaderSettingsDataStore
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.AddToCollectionDialog
import app.opusreader.ui.components.BookContextMenuSheet
import app.opusreader.ui.components.BookDetailsDialog
import app.opusreader.ui.components.FloatingResumeCover
import app.opusreader.ui.components.MainTab
import app.opusreader.ui.components.OpusBottomNavBar
import app.opusreader.ui.screens.AnnotationSortField
import app.opusreader.ui.screens.AnnotationsFilterSheet
import app.opusreader.ui.screens.AnnotationsScreen
import app.opusreader.ui.screens.AnnotationsShareSheet
import app.opusreader.ui.screens.AnnotationsSortSheet
import app.opusreader.ui.screens.CollectionsScreen
import app.opusreader.ui.screens.ContentsTocScreen
import app.opusreader.ui.screens.DisplaySettingsSheet
import app.opusreader.ui.screens.HomeScreen
import app.opusreader.ui.screens.LibraryFilterSheet
import app.opusreader.ui.screens.LibraryGridScreen
import app.opusreader.ui.screens.LibraryListScreen
import app.opusreader.ui.screens.LibraryViewSortSheet
import app.opusreader.ui.screens.MoreScreen
import app.opusreader.ui.screens.NotificationsScreen
import app.opusreader.ui.screens.ReadingChallengesScreen
import app.opusreader.ui.screens.ReaderScreen
import app.opusreader.ui.screens.ReaderSearchScreen
import app.opusreader.ui.screens.SettingsScreen
import app.opusreader.ui.screens.SortDirection
import app.opusreader.ui.screens.ViewSortState
import app.opusreader.ui.screens.YourListsScreen
import app.opusreader.ui.theme.OpusBackground

@Composable
fun OpusNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    bookRepository: BookRepository = remember { FakeBookRepository() },
    catalogRepository: CatalogRepository = remember { FakeCatalogRepository() },
    fixtureId: String? = null,
    fixtureTrigger: Int = 0
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val settingsDataStore = remember { ReaderSettingsDataStore(context) }
    val readerEngine: ReaderEngine = remember {
        FakeReaderEngine(settingsDataStore = settingsDataStore, scope = coroutineScope)
    }

    LaunchedEffect(fixtureId, fixtureTrigger) {
        if (!fixtureId.isNullOrBlank()) {
            while (runCatching { navController.graph }.isFailure) {
                kotlinx.coroutines.delay(50)
            }
            handleFixtureDeepLink(
                fixtureId = fixtureId,
                navController = navController,
                readerEngine = readerEngine,
                bookRepository = bookRepository
            )
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavRoutes.LIBRARY_GRID

    val isMainTab = currentRoute in listOf(
        NavRoutes.HOME,
        NavRoutes.LIBRARY_GRID,
        NavRoutes.LIBRARY_LIST,
        NavRoutes.COLLECTIONS,
        NavRoutes.MORE
    )

    val currentTab = when (currentRoute) {
        NavRoutes.HOME -> MainTab.HOME
        NavRoutes.MORE -> MainTab.MORE
        else -> MainTab.LIBRARY
    }

    val allBooks by bookRepository.allBooks.collectAsState()
    val filteredBooks by bookRepository.filteredBooks.collectAsState()
    val filterState by bookRepository.filterState.collectAsState()
    val sortOrder by bookRepository.sortOrder.collectAsState()
    val viewMode by bookRepository.viewMode.collectAsState()
    val collections by bookRepository.collections.collectAsState()
    val notifications by bookRepository.notifications.collectAsState()

    val currentBook = allBooks.firstOrNull { it.id == "complete_works_kafka" }
        ?: allBooks.firstOrNull()
        ?: app.opusreader.data.SampleBooks.sampleList.first()

    var groupBySeries by remember { mutableStateOf(false) }
    var annotationFilterTypes by remember { mutableStateOf(emptySet<String>()) }
    var annotationFilterColors by remember { mutableStateOf(emptySet<String>()) }
    var annotationSortField by remember { mutableStateOf(AnnotationSortField.LOCATION) }
    var annotationSortDirection by remember { mutableStateOf(SortDirection.ASCENDING) }

    val readerChromeInitiallyVisible = remember(fixtureId) {
        fixtureId !in listOf("R02", "R03", "R04", "R05", "R06")
    }

    var selectedBookForMenu by remember { mutableStateOf<Book?>(null) }
    var selectedBookForCollection by remember { mutableStateOf<Book?>(null) }
    var selectedBookForDetails by remember { mutableStateOf<Book?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            containerColor = OpusBackground,
            bottomBar = {
                if (isMainTab) {
                    OpusBottomNavBar(
                        selectedTab = currentTab,
                        onTabSelected = { tab ->
                            val targetRoute = when (tab) {
                                MainTab.LIBRARY -> when (viewMode) {
                                    LibraryViewMode.LIST -> NavRoutes.LIBRARY_LIST
                                    LibraryViewMode.COLLECTIONS -> NavRoutes.COLLECTIONS
                                    LibraryViewMode.GRID -> NavRoutes.LIBRARY_GRID
                                }
                                else -> tab.route
                            }
                            if (targetRoute != currentRoute) {
                                navController.navigate(targetRoute) {
                                    popUpTo(NavRoutes.LIBRARY_GRID) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavRoutes.LIBRARY_GRID,
                modifier = Modifier.padding(innerPadding)
            ) {
                // 01 Library Grid
                composable(NavRoutes.LIBRARY_GRID) {
                    LibraryGridScreen(
                        books = filteredBooks,
                        onBookClick = { book ->
                            readerEngine.openBook(book.title, book.author)
                            navController.navigate(NavRoutes.READER)
                        },
                        onFilterClick = { navController.navigate(NavRoutes.LIBRARY_FILTER) },
                        onSortClick = { navController.navigate(NavRoutes.LIBRARY_VIEW_SORT) },
                        onBookLongClick = { book -> selectedBookForMenu = book }
                    )
                }

                // 02 Home
                composable(NavRoutes.HOME) {
                    HomeScreen(
                        books = allBooks,
                        catalogRepository = catalogRepository,
                        bookRepository = bookRepository,
                        onBookClick = { book ->
                            readerEngine.openBook(book.title, book.author)
                            navController.navigate(NavRoutes.READER)
                        },
                        onSeeAllLibraryClick = { navController.navigate(NavRoutes.LIBRARY_GRID) },
                        onNotificationsClick = {
                            bookRepository.markAllNotificationsRead()
                            navController.navigate(NavRoutes.NOTIFICATIONS)
                        }
                    )
                }

                // 03 More
                composable(NavRoutes.MORE) {
                    MoreScreen(
                        onSettingsClick = { navController.navigate(NavRoutes.SETTINGS) },
                        onNotebooksClick = { navController.navigate(NavRoutes.ANNOTATIONS) },
                        onChallengesClick = { navController.navigate(NavRoutes.CHALLENGES) },
                        onListsClick = { navController.navigate(NavRoutes.YOUR_LISTS) }
                    )
                }

                // 04 Settings
                composable(NavRoutes.SETTINGS) {
                    SettingsScreen(
                        onBackClick = { navController.popBackStack() },
                        onImportBook = { newBook ->
                            bookRepository.addBook(newBook)
                            bookRepository.addNotification(
                                NotificationItem(
                                    id = "notif_${System.currentTimeMillis()}",
                                    title = "Import Complete",
                                    description = "${newBook.title} has been imported into your library.",
                                    dateText = "Just now",
                                    isRead = false
                                )
                            )
                        }
                    )
                }

                // 05 Library Filter Sheet (rendered over live Library screen)
                composable(NavRoutes.LIBRARY_FILTER) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LibraryListScreen(books = filteredBooks)
                        LibraryFilterSheet(
                            initialState = filterState,
                            allBooks = allBooks,
                            onCloseClick = { navController.popBackStack() },
                            onApplyFilter = { newFilter ->
                                bookRepository.setFilter(newFilter)
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // 06 Library View & Sort Sheet (rendered over live Library screen)
                composable(NavRoutes.LIBRARY_VIEW_SORT) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LibraryListScreen(books = filteredBooks)
                        LibraryViewSortSheet(
                            initialState = ViewSortState(
                                groupBySeries = groupBySeries,
                                viewMode = viewMode,
                                sortOrder = sortOrder
                            ),
                            onCloseClick = { navController.popBackStack() },
                            onApplyViewSort = { viewSortState ->
                                groupBySeries = viewSortState.groupBySeries
                                bookRepository.setSortOrder(viewSortState.sortOrder)
                                bookRepository.setViewMode(viewSortState.viewMode)
                                navController.popBackStack()
                                when (viewSortState.viewMode) {
                                    LibraryViewMode.GRID -> navController.navigate(NavRoutes.LIBRARY_GRID)
                                    LibraryViewMode.LIST -> navController.navigate(NavRoutes.LIBRARY_LIST)
                                    LibraryViewMode.COLLECTIONS -> navController.navigate(NavRoutes.COLLECTIONS)
                                }
                            }
                        )
                    }
                }

                // 07 Library List
                composable(NavRoutes.LIBRARY_LIST) {
                    LibraryListScreen(
                        books = filteredBooks,
                        onBookClick = { book ->
                            readerEngine.openBook(book.title, book.author)
                            navController.navigate(NavRoutes.READER)
                        },
                        onFilterClick = { navController.navigate(NavRoutes.LIBRARY_FILTER) },
                        onSortClick = { navController.navigate(NavRoutes.LIBRARY_VIEW_SORT) },
                        onDownloadClick = { book -> bookRepository.toggleDownload(book.id) },
                        onMoreOptionsClick = { book -> selectedBookForMenu = book }
                    )
                }

                // 08 Collections
                composable(NavRoutes.COLLECTIONS) {
                    CollectionsScreen(
                        collections = collections,
                        onBackClick = { navController.popBackStack() },
                        onCreateCollection = { bookRepository.createCollection(it) },
                        onRenameCollection = { id, name -> bookRepository.renameCollection(id, name) },
                        onDeleteCollection = { id -> bookRepository.deleteCollection(id) },
                        onSortClick = { navController.navigate(NavRoutes.LIBRARY_VIEW_SORT) },
                        onCollectionClick = { navController.navigate(NavRoutes.LIBRARY_GRID) }
                    )
                }

                // 09 Notifications
                composable(NavRoutes.NOTIFICATIONS) {
                    NotificationsScreen(
                        notifications = notifications,
                        onBackClick = { navController.popBackStack() },
                        onClearAll = { bookRepository.clearAllNotifications() },
                        onDeleteNotification = { bookRepository.deleteNotification(it) },
                        onItemClick = {
                            navController.navigate(NavRoutes.READER)
                        }
                    )
                }

                // Reading Challenges
                composable(NavRoutes.CHALLENGES) {
                    ReadingChallengesScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // Your Lists
                composable(NavRoutes.YOUR_LISTS) {
                    YourListsScreen(
                        books = allBooks,
                        onBackClick = { navController.popBackStack() },
                        onBookClick = { book ->
                            readerEngine.openBook(book.title, book.author)
                            navController.navigate(NavRoutes.READER)
                        }
                    )
                }

                // 10 Reader
                composable(NavRoutes.READER) {
                    ReaderScreen(
                        engine = readerEngine,
                        initialChromeVisible = readerChromeInitiallyVisible,
                        onBackClick = { navController.popBackStack() },
                        onContentsClick = { navController.navigate(NavRoutes.CONTENTS) },
                        onSearchClick = { navController.navigate(NavRoutes.READER_SEARCH) },
                        onAnnotationsClick = { navController.navigate(NavRoutes.ANNOTATIONS) },
                        onDisplaySettingsClick = { navController.navigate(NavRoutes.DISPLAY_FONT) }
                    )
                }

                // 11 Contents TOC
                composable(NavRoutes.CONTENTS) {
                    ContentsTocScreen(
                        engine = readerEngine,
                        onCloseClick = { navController.popBackStack() }
                    )
                }

                // 12 Reader Search
                composable(NavRoutes.READER_SEARCH) {
                    ReaderSearchScreen(
                        engine = readerEngine,
                        onBackClick = { navController.popBackStack() },
                        onResultClick = { navController.navigate(NavRoutes.READER) }
                    )
                }

                // 13 Annotations
                composable(NavRoutes.ANNOTATIONS) {
                    AnnotationsScreen(
                        engine = readerEngine,
                        filterTypes = annotationFilterTypes,
                        filterColors = annotationFilterColors,
                        sortField = annotationSortField,
                        sortDirection = annotationSortDirection,
                        onBackClick = { navController.popBackStack() },
                        onFilterClick = { navController.navigate(NavRoutes.ANNOTATIONS_FILTER) },
                        onSortClick = { navController.navigate(NavRoutes.ANNOTATIONS_SORT) },
                        onExportClick = { navController.navigate(NavRoutes.ANNOTATIONS_SHARE) },
                        onItemClick = { navController.navigate(NavRoutes.READER) }
                    )
                }

                // 14 Annotations Filter Sheet (rendered over live Annotations screen)
                composable(NavRoutes.ANNOTATIONS_FILTER) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AnnotationsScreen(engine = readerEngine)
                        AnnotationsFilterSheet(
                            initialTypes = annotationFilterTypes,
                            initialColors = annotationFilterColors,
                            onCloseClick = { navController.popBackStack() },
                            onApply = { types, colors ->
                                annotationFilterTypes = types
                                annotationFilterColors = colors
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // 15 Annotations Sort Sheet (rendered over live Annotations screen)
                composable(NavRoutes.ANNOTATIONS_SORT) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AnnotationsScreen(engine = readerEngine)
                        AnnotationsSortSheet(
                            initialField = annotationSortField,
                            initialDirection = annotationSortDirection,
                            onDismiss = { navController.popBackStack() },
                            onApply = { field, direction ->
                                annotationSortField = field
                                annotationSortDirection = direction
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // 16 Annotations Share Sheet (rendered over live Annotations screen)
                composable(NavRoutes.ANNOTATIONS_SHARE) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AnnotationsScreen(engine = readerEngine)
                        AnnotationsShareSheet(
                            onCloseClick = { navController.popBackStack() }
                        )
                    }
                }

                // 17-20 Display Settings (Aa) Sheet
                composable(NavRoutes.DISPLAY_FONT) {
                    val isLightSheet = fixtureId?.uppercase()?.startsWith("R1") == true
                    Box(modifier = Modifier.fillMaxSize().background(if (isLightSheet) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black)) {
                        ReaderScreen(engine = readerEngine, initialChromeVisible = false)
                        DisplaySettingsSheet(
                            engine = readerEngine,
                            initialTab = 0,
                            collapsedSheet = true,
                            onDismiss = { navController.popBackStack() }
                        )
                    }
                }

                composable(NavRoutes.DISPLAY_LAYOUT) {
                    val isLightSheet = fixtureId?.uppercase()?.startsWith("R1") == true
                    Box(modifier = Modifier.fillMaxSize().background(if (isLightSheet) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black)) {
                        if (!isLightSheet && fixtureId != null) {
                            Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Black))
                        } else {
                            ReaderScreen(engine = readerEngine, initialChromeVisible = false)
                        }
                        DisplaySettingsSheet(
                            engine = readerEngine,
                            initialTab = 1,
                            collapsedSheet = isLightSheet,
                            onDismiss = { navController.popBackStack() }
                        )
                    }
                }

                composable(NavRoutes.DISPLAY_THEMES) {
                    val isLightSheet = fixtureId?.uppercase()?.startsWith("R1") == true
                    Box(modifier = Modifier.fillMaxSize().background(if (isLightSheet) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black)) {
                        if (!isLightSheet && fixtureId != null) {
                            Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Black))
                        } else {
                            ReaderScreen(engine = readerEngine, initialChromeVisible = false)
                        }
                        DisplaySettingsSheet(
                            engine = readerEngine,
                            initialTab = 2,
                            collapsedSheet = isLightSheet,
                            onDismiss = { navController.popBackStack() }
                        )
                    }
                }

                composable(NavRoutes.DISPLAY_MORE) {
                    val isLightSheet = fixtureId?.uppercase()?.startsWith("R1") == true
                    Box(modifier = Modifier.fillMaxSize().background(if (isLightSheet) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black)) {
                        if (!isLightSheet && fixtureId != null) {
                            Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Black))
                        } else {
                            ReaderScreen(engine = readerEngine, initialChromeVisible = false)
                        }
                        DisplaySettingsSheet(
                            engine = readerEngine,
                            initialTab = 3,
                            collapsedSheet = isLightSheet,
                            onDismiss = { navController.popBackStack() }
                        )
                    }
                }
            }
        }

        // Persistent Floating Resume Cover (uniform across all main tabs)
        if (isMainTab) {
            FloatingResumeCover(
                title = currentBook.title,
                author = currentBook.author,
                progressPercent = currentBook.progressPercent,
                coverDrawableRes = currentBook.coverDrawableRes ?: R.drawable.cover_book_0,
                onClick = {
                    readerEngine.openBook(currentBook.title, currentBook.author)
                    navController.navigate(NavRoutes.READER)
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .navigationBarsPadding()
                    .padding(start = 16.dp, bottom = 8.dp)
            )
        }

        selectedBookForMenu?.let { book ->
            BookContextMenuSheet(
                book = book,
                onDismissRequest = { selectedBookForMenu = null },
                onToggleRead = { bookRepository.toggleRead(book.id) },
                onAddToCollection = { selectedBookForCollection = book },
                onToggleDownload = { bookRepository.toggleDownload(book.id) },
                onBookDetails = { selectedBookForDetails = book }
            )
        }

        selectedBookForCollection?.let { book ->
            AddToCollectionDialog(
                book = book,
                collections = collections,
                onDismissRequest = { selectedBookForCollection = null },
                onToggleCollection = { colId ->
                    bookRepository.toggleBookInCollection(book.id, colId)
                }
            )
        }

        selectedBookForDetails?.let { book ->
            BookDetailsDialog(
                book = book,
                onDismissRequest = { selectedBookForDetails = null }
            )
        }
    }
}

private fun handleFixtureDeepLink(
    fixtureId: String,
    navController: NavHostController,
    readerEngine: ReaderEngine,
    bookRepository: BookRepository
) {
    val upperId = fixtureId.uppercase().trim()
    when (upperId) {
        "D01", "L01" -> {
            bookRepository.setViewMode(LibraryViewMode.GRID)
            navController.navigate(NavRoutes.LIBRARY_GRID) { popUpTo(0) { inclusive = true } }
        }
        "D02", "L02" -> {
            navController.navigate(NavRoutes.HOME) { popUpTo(0) { inclusive = true } }
        }
        "D03", "L03" -> {
            navController.navigate(NavRoutes.MORE) { popUpTo(0) { inclusive = true } }
        }
        "D04", "L04" -> {
            navController.navigate(NavRoutes.SETTINGS) { popUpTo(0) { inclusive = true } }
        }
        "D05", "L05" -> {
            navController.navigate(NavRoutes.LIBRARY_FILTER) { popUpTo(0) { inclusive = true } }
        }
        "D06", "L06" -> {
            navController.navigate(NavRoutes.LIBRARY_VIEW_SORT) { popUpTo(0) { inclusive = true } }
        }
        "D07", "L07" -> {
            bookRepository.setViewMode(LibraryViewMode.LIST)
            navController.navigate(NavRoutes.LIBRARY_LIST) { popUpTo(0) { inclusive = true } }
        }
        "D08", "L08" -> {
            navController.navigate(NavRoutes.COLLECTIONS) { popUpTo(0) { inclusive = true } }
        }
        "D09", "L09" -> {
            navController.navigate(NavRoutes.NOTIFICATIONS) { popUpTo(0) { inclusive = true } }
        }
        "D10", "R01", "R02", "R03", "R04", "R05", "R06", "R07", "R08", "R09", "R10" -> {
            val themeColor = when (upperId) {
                "R03", "R07", "R01", "R02" -> "WHITE"
                "R04", "R08" -> "SEPIA"
                "R05", "R09" -> "GREEN"
                else -> "BLACK"
            }
            readerEngine.updateSettings { it.copy(pageColor = themeColor) }
            navController.navigate(NavRoutes.READER) { popUpTo(0) { inclusive = true } }
        }
        "D11" -> {
            navController.navigate(NavRoutes.CONTENTS) { popUpTo(0) { inclusive = true } }
        }
        "D12" -> {
            navController.navigate(NavRoutes.READER_SEARCH) { popUpTo(0) { inclusive = true } }
        }
        "D13" -> {
            navController.navigate(NavRoutes.ANNOTATIONS) { popUpTo(0) { inclusive = true } }
        }
        "D14" -> {
            navController.navigate(NavRoutes.ANNOTATIONS_FILTER) { popUpTo(0) { inclusive = true } }
        }
        "D15" -> {
            navController.navigate(NavRoutes.ANNOTATIONS_SORT) { popUpTo(0) { inclusive = true } }
        }
        "D16" -> {
            navController.navigate(NavRoutes.ANNOTATIONS_SHARE) { popUpTo(0) { inclusive = true } }
        }
        "D17", "R11" -> {
            readerEngine.updateSettings { it.copy(pageColor = if (upperId == "R11") "WHITE" else "BLACK") }
            navController.navigate(NavRoutes.DISPLAY_FONT) { popUpTo(0) { inclusive = true } }
        }
        "D18", "R12" -> {
            readerEngine.updateSettings { it.copy(pageColor = if (upperId == "R12") "WHITE" else "BLACK") }
            navController.navigate(NavRoutes.DISPLAY_LAYOUT) { popUpTo(0) { inclusive = true } }
        }
        "D19", "R13" -> {
            readerEngine.updateSettings { it.copy(pageColor = if (upperId == "R13") "WHITE" else "BLACK") }
            navController.navigate(NavRoutes.DISPLAY_THEMES) { popUpTo(0) { inclusive = true } }
        }
        "D20", "R14" -> {
            readerEngine.updateSettings { it.copy(pageColor = if (upperId == "R14") "WHITE" else "BLACK") }
            navController.navigate(NavRoutes.DISPLAY_MORE) { popUpTo(0) { inclusive = true } }
        }
    }
}
