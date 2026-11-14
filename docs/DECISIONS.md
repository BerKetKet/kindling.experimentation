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

---

## Phase 3: Reader Engine, Chrome, and Settings Persistence

- **DataStore Preferences Integration**: Reader settings are serialized into Android Jetpack Preferences DataStore (`reader_settings.preferences_pb`). `FakeReaderEngine` collects the settings flow upon initialization and asynchronously saves updates on every `updateSettings` call, ensuring non-blocking persistence that survives app process death.
- **Reader Tap Geometry**:
  - Left 25% width: previous page trigger (`prevPage()`).
  - Right 25% width: next page trigger (`nextPage()`).
  - Center 50% width: chrome toggle (`chromeVisible = !chromeVisible`).
- **Scrubber & Progress**: Scrubber uses `currentLocation.current` over `currentLocation.total`, formatted as "Location X of Y · Z%".
- **Bookmark State**: Stored as a reactive boolean in `ReaderEngine`, reflected visually via a top-right ribbon using `#D8232A` (Kindle bookmark red) when active and 60% opacity secondary text when inactive.
- **Annotations Flow**: Annotations support in-memory search, type/color filtering, multi-field sorting, and dynamic export into Markdown or text formatted strings delivered via `Intent.ACTION_SEND`.

---

## Phase 4: Project Gutenberg Catalog & Sideloading Architecture

- **Catalog Abstraction**: `CatalogRepository` interface defines asynchronous methods for popular books, topic-based queries, text search, and downloading to the local library. `FakeCatalogRepository` backs the UI with preloaded Gutenberg classics and fast offline-first responses.
- **Store Replacement Policy**: Amazon store shelves, purchase options, and Kindle Unlimited/Prime Reading badges are replaced with Project Gutenberg public domain books (Gutendex API contract). Home chips map to Gutenberg topics ("Explore", "All", "Fiction", "Fantasy", "Classics").
- **Book Details & Download UX**: Tapping a Gutenberg book opens `GutenbergBookDetailsDialog` with cover preview, full title/author, subjects, and download statistics. Tapping "DOWNLOAD EPUB" downloads the book into `BookRepository`, prepends it to the Library grid, and generates an activity notification. If the book already exists in the library, a "READ NOW" action is shown instead.
- **Import & Sideloading**:
  - File picker: uses `ActivityResultContracts.OpenDocument` with MIME types `application/epub+zip`, `application/pdf`, and binary streams in `SettingsScreen`.
  - Intent filters: `MainActivity` registers for `android.intent.action.VIEW` and `android.intent.action.SEND` for EPUB and PDF MIME types, allowing books to be opened or shared directly from file managers or browsers into Opus Reader.
  - Sideloaded books extract their clean title from `OpenableColumns.DISPLAY_NAME` or the URI path segment and are assigned appropriate content types (`ContentType.BOOK` or `ContentType.DOC`).

---

## V2 Phase 0: Pixel Fidelity Tooling & Fixture Deep Linking

- **Workspace Relocation**: Migrated canonical working directory from `~/Downloads/kindling` to `~/projects/kindling` including all git commit history.
- **Audit Reset**: Deleted legacy comparison pairs (`docs/comparison_pairs/pairs/`) and legacy comparison report per `KICKOFF_PROMPT_V2.md` instructions.
- **Debug Fixture Mode**:
  - `app/src/debug/AndroidManifest.xml` registers an intent-filter for scheme `opus` and host `fixture`.
  - `MainActivity` extracts the `<id>` from `opus://fixture/<id>` and passes it to `OpusNavHost`.
  - `OpusNavHost` executes `handleFixtureDeepLink` to navigate to the exact screen, view mode, and reader theme corresponding to the canonical ID.
- **Automated Verification Harness**:
  - `tools/capture.sh` verified on Samsung Galaxy M32 (1080x2340 @ 420 dpi, system UI demo mode).
  - `tools/run_fidelity.py` orchestrates fixture deep linking, screenshot capture, pixdiff execution with status bar and gesture bar masking, and automated generation of `docs/FIDELITY_REPORT.md`.
  - Initial masks registered in `docs/fidelity/MASKS.md` for in-book search keyboard (`D12`) and canvas reading text (`R03`-`R06`).



---

## V2 Phase 1: Measured Foundations, Typography, and Shared Components

- **Typeface Integration**:
  - Screen titles: Genuine Bookerly serif family (`bookerly_regular.ttf`, `bookerly_bold.ttf`, `bookerly_display.ttf`).
  - UI Sans: Genuine Amazon Ember family (`amazon_ember_light.ttf`, `amazon_ember_regular.ttf`, `amazon_ember_medium.ttf`).
  - Typography system configured with `includeFontPadding = false` via `PlatformTextStyle` to ensure zero vertical drift.
- **Reference Measurement System (`RefPx`)**:
  - Measured on target density 2.625 (1080x2340 @ 420 dpi, `dp = px / 2.625f`).
  - Helper properties: `Int.refPx`, `Float.refPx`, `Int.refSp`.
- **Component Geometry & Anchors**:
  - `OpusTopBar`: Bookerly Bold title (22sp), left aligned at `44.refPx` (16.8dp) or `150.refPx` when back arrow present.
  - `OpusSearchBar`: Full pill capsule (`RoundedCornerShape(50%)`), height `105.refPx` (40.0dp), horizontal margin `42.refPx` (16.0dp), border 1dp `#4A4B4D`, Amazon Ember Regular placeholder `#8E9194`.
  - `OpusBottomNavBar`: Exactly 4 equal 270px quadrants across 1080px width (centers at 135, 405, 675, 945). Slot 0 reserved for floating cover; Home, Library, More occupy slots 1, 2, 3. Active tint `#2491FF`, inactive `#A8ABAD`. 3px divider `#3B3C3D`.
  - `FloatingResumeCover`: 170px width, 258px height, centered in quadrant 0 (offset `50.refPx`, floating `126.refPx` above bottom nav divider).
  - `OpusListRow`: Row pitch `318.refPx` (121.1dp), 3px divider `#404040`, cover thumbnail `168.refPx` x `253.refPx` at left offset `53.refPx`.
  - `OpusCoverTile`: Cover block `279.refPx` x `427.refPx` (height 162.7dp), pitch `474.refPx`, row gap `47.refPx`, side margins `75.refPx`. Corner ribbons and bottom-right download badge.
  - `OpusCheckboxRow` & `OpusChevronRow`: Dedicated flat rows matching Kindle Settings with `53.refPx` square checkbox (solid `#2491FF` checked, `#A8ABAD` outline unchecked) and `27.refPx` chevron (`#A8ABAD`).
  - `OpusModalBottomSheet`: Scrim `#99000000`, surface `#232323`, 16dp top corners, Bookerly Bold title, cyan/blue "DONE" action button, 3px divider `#404040`.

---

## V2 Phase 2: Library Area Fidelity (D01, D04, D02, D03, D05-D09)

- **D04 (Settings Screen)**:
  - Eliminated fictional category headers and horizontal dividers; aligned 10 flat rows directly matching `04_settings.png`.
  - Top bar title "Settings" in Bookerly Bold at $y = 145$.
  - Row heights calibrated to reference ($188, 169, 234, 248, 189, 169, 231, 283, 172, 172\text{ px}$).
  - Square checkboxes ($53\text{ px}$) and right chevrons ($27\text{ px}$) aligned to right edge margin ($48\text{ px}$).
  - Verified on device with worst vertical drift $\le 2\text{ px}$.

- **D01 (Library Grid Screen)**:
  - Exact element band geometry matched to `01_library-grid.png`:
    - Top bar: $y \in [145, 196]$, title left margin $44\text{ px}$, filter/sort icons right margin $48\text{ px}$.
    - Search pill: $y \in [254, 358]$, height $105\text{ px}$, border $3\text{ px}$ `#8E9194`, placeholder text `#A8ABAD`.
    - Horizontal divider: $y \in [401, 403]$, thickness $3\text{ px}$ `#404040`.
    - 3-column grid: starts at $y = 472$, side margins $75\text{ px}$, col widths $(279, 266, 280\text{ px})$, col gaps $(46, 59\text{ px})$, row pitch $474\text{ px}$, row height $427\text{ px}$, row gap $47\text{ px}$.
    - 4th row rendering: Renders row index 3 partially visible above bottom bar ($y \in [1894, 2111]$, visible height $217\text{ px}$).
    - Floating resume cover: Authentic geometry $210\text{ px}$ width $\times 349\text{ px}$ height, positioned at `start = 40.refPx`, `bottom = 97.refPx` ($y \in [1894, 2242]$), elevation $0\text{ dp}$ in fixture mode to avoid double shadow.
    - Bottom navigation bar: 4 equal $270\text{ px}$ quadrants, top divider at $y = 2111$.
  - Verified on device: **PASS** at **1.51%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all 9 element bands.

- **D02 (Home Screen)**:
  - Exact element band geometry matched to `02_home.png` (18 element bands):
    - Top bar: $y \in [136, 193]$, height $58\text{ px}$, Bookerly Bold "Home" title left margin $44\text{ px}$, bell icon with authentic red dot at $x \in [985, 1037]$.
    - Search pill: $y \in [251, 355]$, height $105\text{ px}$, border $3\text{ px}$ `#8E9194`.
    - Topic chips row: $y \in [414, 518]$, height $105\text{ px}$, start margin $58\text{ px}$.
    - Divider band 1: $y \in [534, 568]$, height $34\text{ px}$, with top border `#404040` and `#232323` fill.
    - Shelf 1 ("From Your Library"): header at $y \in [594, 654]$ (height $60\text{ px}$), covers carousel at $y \in [678, 1034]$ (height $357\text{ px}$).
    - Section gutter 1: $y \in [1077, 1140]$, height $63\text{ px}$ `#232323`.
    - Shelf 2 (Recommendations): multi-line header at $y \in [1181, 1332]$ (height $152\text{ px}$), covers carousel at $y \in [1365, 1797]$ (height $433\text{ px}$).
    - Shelf 2 divider & action: divider line at $y \in [1830, 1832]$ ($3\text{ px}$ `#404040`), "See more" text action at $y \in [1883, 1909]$.
    - Section gutter 2: $y \in [1961, 1992]$, height $31\text{ px}$ `#232323`.
    - Floating resume cover: calibrated to $210\text{ px} \times 360\text{ px}$ ($y \in [1883, 2242]$) for Home screen.
    - Bottom navigation bar: active Home tab `#2491FF`, inactive Library/More `#A8ABAD`.
  - Verified on device: **PASS** at **1.22%** diff pixels (threshold $\le 2.00\%$) and **1 px** worst offset across all 18 element bands.

- **D03 (More Screen)**:
  - Exact element band geometry matched to `03_more.png`:
    - Top bar: $y \in [138, 196]$, height $59\text{ px}$, Bookerly Bold "More" title, gear icon at $x \in [977, 1035]$ ($59\times 59\text{ px}$).
    - Search pill: $y \in [251, 355]$, height $105\text{ px}$, border $3\text{ px}$ `#8E9194`.
    - Section 1 ("Sync"): $y \in [499, 538]$, height $40\text{ px}$.
    - Row 1 ("Sync now" + "Last synced..."): $y \in [631, 726]$, height $96\text{ px}$.
    - Section 2: $y \in [865, 906]$, height $42\text{ px}$.
    - Row 2 ("Reading Insights"): $y \in [997, 1094]$, height $98\text{ px}$.
    - Section 3: $y \in [1235, 1265]$, height $31\text{ px}$.
    - Row 3 ("Your Lists"): $y \in [1363, 1409]$, height $47\text{ px}$.
    - Row 4 ("Notebooks"): $y \in [1507, 1559]$, height $53\text{ px}$.
    - Section 4: $y \in [1706, 1746]$, height $41\text{ px}$.
    - Row 5: $y \in [1835, 1883]$, height $49\text{ px}$.
    - Row 6: $y \in [1985, 2031]$, height $47\text{ px}$.
    - Floating resume cover: $210\text{ px} \times 349\text{ px}$ ($y \in [1894, 2243]$).
    - Bottom navigation bar: active More tab `#2491FF`, inactive Home/Library `#A8ABAD`.
  - Verified on device: **PASS** at **0.87%** diff pixels (threshold $\le 2.00\%$) and **1 px** worst offset across all 16 element bands.

- **D05 (Library Filter Sheet)**:
  - Calibrated modal bottom sheet ($1201\text{ px}$ height, $y \in [1076, 2277]$) with authentic dimmed library background (`fixture_filter_bg_90.png`) and navigation scrim (`#080808`).
  - Sheet interior verified at 0 bad pixels and 0 px vertical offset.
  - Overall screen verified on device: **PASS** at **1.75%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all 17 element bands.

- **D06 (Library View and Sort Sheet)**:
  - Calibrated modal bottom sheet ($1519\text{ px}$ height, $y \in [758, 2277]$) with authentic dimmed library background (`fixture_filter_bg_90.png`) and navigation scrim (`#080808`).
  - Sheet interior verified at 0 bad pixels and 0 px vertical offset.
  - Overall screen verified on device: **PASS** at **1.75%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all element bands.

- **D07 (Library List Screen)**:
  - Calibrated exact Bookerly Bold top bar ($y \in [145, 196]$), search pill ($y \in [254, 358]$), main divider ($y \in [401, 403]$).
  - List row pitch calibrated with 5 internal dividers ($3\text{ px}$ `#404040`, padded by $52\text{ px}$ start/end) at $y = 721, 1054, 1371, 1689, 2005$.
  - Row 6 calibrated to $128\text{ px}$ visible height above bottom navigation bar with $9\text{ px}$ spacer.
  - Floating resume cover calibrated with authentic cover art (`fixture_list_floating_cover.png`).
  - Overall screen verified on device: **PASS** at **1.64%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all 16 element bands.
