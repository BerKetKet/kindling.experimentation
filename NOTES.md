# Development Notes — Opus Reader

## Current State
- **Phase**: Phase 1 Complete. Moving to Phase 2.
- **Git Branch**: `phase-1`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## What is Verified
- `./gradlew assembleDebug testDebugUnitTest lintDebug` passes cleanly (52/52 tasks).
- Debug APK (`app-debug.apk`) installed and running on Galaxy M32.
- All 20 screens and navigation routes verified on physical device via adb input and screencap OCR:
  - 01 Library Grid: rendered with cover tiles and search bar.
  - 02 Home: bottom tab navigation, shelf sections, explore chips.
  - 03 More: sync, features (Notebooks, Challenges, Lists), settings navigation.
  - 04 Settings: device preferences, storage, local import, font management.
  - 05 Library Filter Sheet: read/unread and content type toggles.
  - 06 Library View & Sort Sheet: Grid / List / Collections switching and sort orders.
  - 07 Library List: rendered with detailed rows, metadata, and progress.
  - 08 Collections: collections list with empty state and create action.
  - 09 Notifications: activity and sync history feed.
  - 10 Reader: full screen reader with text flow, page scrubber, and bookmark ribbon.
  - 11 Contents TOC: chapter hierarchy with indentations and jump targets.
  - 12 Reader Search: query input and search preview.
  - 13 Annotations: list of notes, highlights, bookmarks with filter, sort, share actions.
  - 14 Annotations Filter Sheet: filter by type (favorite, note, highlight, bookmark) and color.
  - 15 Annotations Sort Sheet: sort by location, date created, date modified.
  - 16 Annotations Share Sheet: export as Markdown, Plain Text, CSV.
  - 17 Display Settings Font Tab: font family carousel, size slider.
  - 18 Display Settings Layout Tab: page color palette, margins, alignment, continuous scroll.
  - 19 Display Settings Themes Tab: custom theme and presets (Compact, Standard, Large, Low Vision).
  - 20 Display Settings More Tab: assistive reader, ruler, progress display, visible clock.
- Floating resume cover sits at bottom-left overlapping the bottom bar and opens Reader when tapped.

## What is Broken / Blockers
- None.

## Next Steps
- **Phase 2**: Library, Home, More, Settings, Notifications Interactivity:
  - Create `BookRepository` interface and in-memory fake repository with reactive `StateFlow`.
  - Wire real filtering (Read/Unread, Content type: Books/Samples/Docs) in Library.
  - Wire real view switching (Grid <-> List <-> Collections) and sorting (Most Recent, Title, Author A-Z, Author Z-A).
  - Implement New Collection creation dialog and collections management.
  - Verify on device with screenshots, update docs, and commit `phase-2`.
