# Implementation Progress

## Phase 0: Project Setup, Theme, and Shared Components

### Done
- Gradle project initialized with single `app` module, package `app.opusreader`.
- Toolchain: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21 with Jetpack Compose 2024.09.00 BOM, Room 2.6.1, DataStore 1.1.1, Coil 2.7.0, Java 21, minSdk 26, targetSdk 35.
- Dark theme sampled directly from Kindle Android reference screenshots (1080x2340 @ 2.625 density):
  - Screen background: `#101112`
  - Bottom bar & navigation background: `#141414`
  - Bottom sheet & dialog surface: `#232323`
  - Border and outline: `#404040`
  - Primary text: `#EDEDED`
  - Secondary text: `#A8ABAD`
  - Accent colors: Blue `#2491FF`, Gold `#D29648`, Bookmark red `#D8232A`
  - Reader background palette: White `#FFFFFF`, Sepia `#F7F1E3`, Green `#CEE5D8`, Black `#141414`.
- Shared components built with Compose previews and min 48dp touch targets:
  - `OpusTopBar`: Kindle-styled top bar with navigation back/close button, title, and action buttons.
  - `OpusSearchBar`: 48dp outlined search container with 1dp `#404040` border, search icon, clear button, and placeholder.
  - `OpusBottomSheet`: Container with 16dp rounded top corners, `#232323` background, header with title and DONE/Close action.
  - `OpusListRow`: Row layout with 76x108dp cover thumbnail, progress indicator, title, uppercase author, metadata, download checkmark, and overflow menu.
  - `OpusCoverTile`: 3-column grid tile with 106x152dp cover thumbnail, progress indicator bar, download badge, and title/author.
  - `OpusToggleRow`: Setting/option row supporting Switch, Checkbox, Radio, and Checkmark selection modes with title and subtitle.
- Engine boundary: `ReaderEngine` interface and `FakeReaderEngine` implementation with bundled Gardens of the Moon sample text and table of contents.
- Verification: `./gradlew assembleDebug testDebugUnitTest lintDebug` passed. Installed and verified on Samsung Galaxy M32 (1080x2340 @ 420 dpi).

### Differences from Reference
- Kindle/Amazon branding replaced with "Opus Reader".

### Inferred
- Placeholder typography in the book cover thumbnail uses clean sans-serif when no cover image is supplied.

---

## Phase 1: Navigation Shell, Bottom Tabs, Floating Cover, and 20 Screen Routes

### Done
- Navigation shell built using Jetpack Navigation Compose (`OpusNavHost`) wiring all 20 screens defined in `docs/UI_SPEC.md`:
  - 01 `LibraryGridScreen` (ref: `01_library-grid.png`)
  - 02 `HomeScreen` (ref: `02_home.png`)
  - 03 `MoreScreen` (ref: `03_more.png`)
  - 04 `SettingsScreen` (ref: `04_settings.png`)
  - 05 `LibraryFilterSheet` (ref: `05_library-filter-sheet.png`)
  - 06 `LibraryViewSortSheet` (ref: `06_library-view-sort-sheet.png`)
  - 07 `LibraryListScreen` (ref: `07_library-list.png`)
  - 08 `CollectionsScreen` (ref: `08_collections.png`)
  - 09 `NotificationsScreen` (ref: `09_notifications.png`)
  - 10 `ReaderScreen` (ref: `10_reader-chrome-visible.png`)
  - 11 `ContentsTocScreen` (ref: `11_contents-toc.png`)
  - 12 `ReaderSearchScreen` (ref: `12_reader-search.png`)
  - 13 `AnnotationsScreen` (ref: `13_annotations.png`)
  - 14 `AnnotationsFilterSheet` (ref: `14_annotations-filter-sheet.png`)
  - 15 `AnnotationsSortSheet` (ref: `15_annotations-sort-sheet.png`)
  - 16 `AnnotationsShareSheet` (ref: `16_annotations-share-via-email.png`)
  - 17 `DisplaySettingsFontTab` (ref: `17_display-settings-font-tab.png`)
  - 18 `DisplaySettingsLayoutTab` (ref: `18_display-settings-layout-tab.png`)
  - 19 `DisplaySettingsThemesTab` (ref: `19_display-settings-themes-tab.png`)
  - 20 `DisplaySettingsMoreTab` (ref: `20_display-settings-more-tab.png`)
  - Shared `DisplaySettingsSheet` hosting tabs 17-20 with common top tab row and bottom brightness slider.
- Bottom Navigation Bar (`OpusBottomNavBar`):
  - Fixed 56dp height bar in `#141414` with persistent tabs: Home | Library | More.
  - Reserved 80dp start spacer for the floating resume cover.
  - Active tab highlighting in `#EDEDED` and inactive in `#A8ABAD`.
- Floating Resume Cover (`FloatingResumeCover`):
  - Positioned at bottom-left (`offset(x = 16.dp, y = (-12).dp)`), overlapping the bottom bar exactly as in the Kindle references.
  - Displays currently reading book cover thumbnail (52x72dp) with title, author, and reading progress percentage bar.
  - Tapping opens the Reader screen (`10`) directly.
- Tap map connectivity:
  - Library Grid <-> Library List <-> Collections view switching via View & Sort sheet (`06`).
  - Filter sheet (`05`) accessible from Library top bar.
  - More screen (`03`) top bar settings action opens Settings screen (`04`).
  - Reader screen (`10`) chrome buttons wire directly to Contents TOC (`11`), Search (`12`), Annotations (`13`), and Display Settings (`17-20`).
  - Annotations screen (`13`) top bar actions wire directly to Filter sheet (`14`), Sort sheet (`15`), and Export/Share sheet (`16`).
- Verification:
  - `./gradlew assembleDebug testDebugUnitTest lintDebug` passed (52/52 tasks).
  - Installed and verified on Samsung Galaxy M32 (1080x2340 @ 420 dpi). Tapped and verified live navigation through all primary and sub-screens via screenshots and OCR.

### Differences from Reference
- Kindle Store and Amazon Prime Reading shelves replaced with Project Gutenberg browse sections in Home and Library.
- Kindle email send-to-device replaced with local storage import and system share sheet export.
- Amazon account management in Settings replaced with local device reader preferences and font manager.

### Inferred
- Empty Collections state UI with folder icon and "+ CREATE A NEW COLLECTION" button (unillustrated in reference screenshots, inferred from Kindle Android convention).
- Annotations export options: Markdown (.md), Plain Text (.txt), CSV spreadsheet.

---

## Phase 2: Library, Home, More, Settings, Notifications Interactivity

### Done
- In-memory data architecture implemented with `BookRepository` interface and `FakeBookRepository`:
  - Reactive `StateFlow` streams for `allBooks`, `filteredBooks`, `filterState`, `sortOrder`, `viewMode`, `collections`, and `notifications`.
  - Realistic initial library of 10 diverse titles spanning Books, Samples, and Docs, with read (100%), in-progress, and unread states.
- Real dynamic filtering (`LibraryFilterSheet`):
  - Dynamic item counts computed directly from library data: Read (3), Unread (7), Books (6), Samples (2), Docs (2).
  - Toggling read state or content type filters re-filters library books in real-time. Verified on device (filtering to Read only displays 3 items).
- Real dynamic sorting (`LibraryViewSortSheet`):
  - Supports Most Recent, Title (A-Z), Author (A-Z), and Author (Z-A).
  - Selecting a sort option immediately reorders the library in both Grid and List views. Verified on device with Author A-Z sorting (Austen -> Dostoevsky -> Erikson ...).
- Real view switching (`LibraryViewSortSheet`):
  - Seamlessly toggles between Grid (`01`), List (`07`), and Collections (`08`) with backstack consistency.
- Collections management (`CollectionsScreen`):
  - Pre-populated collections: "All-Time Favorites" (3 items), "Gutenberg Classics" (4 items), "Science Fiction" (1 item).
  - Interactive "New Collection" creation dialog with text input. Verified on device by creating new "Cyberpunk" collection live.
- Home screen interactivity (`HomeScreen`):
  - Displays "From Your Library" shelf populated with user's reading list.
  - "Project Gutenberg Classics" discovery shelf.
  - Category chips ("Explore", "All", "Fiction", "Nonfiction", "Fantasy", "Classics").
  - Notification badge linking to activity feed.
- Notifications (`NotificationsScreen`):
  - Activity feed with real sync, download, and milestone alerts.
  - Per-item dismissal and "Clear All" action.
- Settings & More screens (`SettingsScreen`, `MoreScreen`):
  - Interactive dialog for renaming device.
  - Toggles for push notifications, SD card downloads, and library sync.
  - Dynamic "Sync now" action with real-time timestamp formatting.
- Verification:
  - Unit test suite (`FakeBookRepositoryTest`) passing with 8 tests covering filtering, sorting, collection CRUD, and progress updates.
  - `./gradlew assembleDebug testDebugUnitTest lintDebug` passed cleanly (52/52 tasks).
  - Verified on physical Samsung Galaxy M32 (1080x2340 @ 420 dpi) across all interactions.

### Differences from Reference
- None in layout or visuals. Kindle account settings replaced with local reader preferences as specified in `docs/UI_SPEC.md`.

### Inferred
- Collection creation dialog with text entry and validation.
- Per-item delete action on notification rows.
