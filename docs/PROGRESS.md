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
