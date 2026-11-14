# Architectural and Design Decisions

## Phase 0: Project Setup, Theme, and Shared Components

- **Build System**: Android Gradle Plugin 8.5.2 with Gradle 8.7, Kotlin 2.0.21 and Jetpack Compose compiler plugin `org.jetbrains.kotlin.plugin.compose`.
- **Target & SDK**: minSdk 26 (Android 8.0 Oreo), targetSdk 35, compileSdk 35. Fits Samsung Galaxy M32 test device and reference specs.
- **Theme Sampling**: Direct pixel sampling from 20 reference screenshots (1080x2340 px at 2.625 density):
  - Screen background: `#101112`
  - Bottom bar & navigation background: `#141414`
  - Surface & bottom sheet background: `#232323`
  - Borders & dividers: `#404040`
  - Text primary: `#EDEDED`, secondary: `#A8ABAD`, tertiary: `#8E9194`
  - Accent blue: `#2491FF`, Accent gold: `#D29648`, Bookmark red: `#D8232A`
- **Component Geometry**:
  - Top Bar: 56dp height, 16dp horizontal padding, 20sp semi-bold title.
  - Search Bar: 48dp height (within 56dp container), 1dp outline border `#404040`, 8dp corner radius.
  - Bottom Sheet: 16dp top corner radius, `#232323` surface, sticky header with Title on left and "DONE" button on right, 1dp divider.
  - List Row: 124dp row height, 76x108dp book cover thumbnail with progress bar, title, author, file size/percentage, download checkmark, overflow menu.
  - Cover Tile: 3-column grid, 106x152dp cover thumbnail (~1:1.43 aspect ratio), title, author, progress bar, download checkmark badge.
  - Toggle Row: minimum 48dp touch target, title, optional subtitle, supporting Switch, Checkbox, Radio, and Checkmark.
- **Engine Interface**: `ReaderEngine` defined in `app/opusreader/engine/ReaderEngine.kt` and backed by `FakeReaderEngine` with bundled Malazan Gardens of the Moon sample text, matching reference screens 10, 11, 13, 17.
- **Privacy & Branding**: No Amazon or Kindle trademarks or assets in code or strings. App name constant: "Opus Reader".

---

## Phase 1: Navigation Shell and Screen Architecture

- **Navigation Framework**: Jetpack Navigation Compose (`androidx.navigation:navigation-compose:2.8.0`) with string routes encapsulated in `NavRoutes.kt`.
- **Scaffold & Bottom Bar Visibility**: `Scaffold` manages the persistent bottom bar (`OpusBottomNavBar`) and floating resume cover (`FloatingResumeCover`) conditionally for primary top-level tabs (`NavRoutes.HOME`, `NavRoutes.LIBRARY_GRID`, `NavRoutes.LIBRARY_LIST`, `NavRoutes.MORE`). Secondary screens (Reader, Settings, TOC, Sheets, Annotations) are edge-to-edge full-bleed without the bottom navigation bar.
- **Bottom Bar Layout**: Bottom bar allocates an 80dp start spacer so that the 52dp floating resume cover can overlap the bottom-left area without obscuring any tab buttons (Home, Library, More).
- **Sheet Architecture**: Sheets (`LibraryFilterSheet`, `LibraryViewSortSheet`, `AnnotationsFilterSheet`, `AnnotationsSortSheet`, `AnnotationsShareSheet`, `DisplaySettingsSheet`) are composables styled as modal bottom sheets with 16dp top corners, `#232323` surface, title headers, and "DONE" / "Close" action buttons. They are registered in the `NavHost` to enable direct navigation and backstack restoration.
- **Reader Display Settings Architecture**: `DisplaySettingsSheet` hosts the four Kindle display tabs (Font `17`, Layout `18`, Themes `19`, More `20`) under a unified tab header and bottom brightness slider with auto-toggle, allowing smooth switching between typography, layout, theme presets, and reading progress controls.

---

## Phase 2: In-Memory Data Repository and Filtering/Sorting Engine

- **Repository Abstraction**: `BookRepository` interface cleanly decouples the UI from data persistence. `FakeBookRepository` uses Kotlin `MutableStateFlow` to notify subscribers of filter, sort, collection, and book updates reactively.
- **Filtering Logic**: Implemented in `FakeBookRepository.computeFilteredBooks`:
  - Read condition evaluates against `isRead` boolean (`progressPercent >= 100`).
  - Content type evaluates against `ContentType` enum (`BOOK`, `SAMPLE`, `DOC`).
  - Active counts in `LibraryFilterSheet` are computed dynamically from `allBooks`.
- **Sorting Logic**:
  - `MOST_RECENT`: sorts descending by `lastReadTimestamp`.
  - `TITLE`: sorts ascending case-insensitively by `title`.
  - `AUTHOR_AZ`: sorts ascending case-insensitively by `author`.
  - `AUTHOR_ZA`: sorts descending case-insensitively by `author`.
- **Collections Lifecycle**: Collections maintain an internal list of member book IDs and a dynamically computed `bookCount`. Adding or deleting a collection persists in the in-memory repository for the session.
- **Dialog Architecture**: In-place `AlertDialog`s for collection creation and device renaming, styled with `#232323` container, `#2491FF` confirm actions, and outlined input fields.
