# Development Notes — Opus Reader

## Current State
- **Phase**: All Phases (Phase 0, Phase 1, Phase 2, Phase 3, Phase 4) Complete!
- **Git Branch**: `phase-4`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## What is Verified
- `./gradlew assembleDebug testDebugUnitTest lintDebug` passes cleanly (52/52 tasks).
- All 13 unit tests pass (`FakeReaderEngineTest`, `FakeBookRepositoryTest`, and `FakeCatalogRepositoryTest`).
- Debug APK (`app-debug.apk`) installed and running on Galaxy M32.
- Phase 0: Project setup, dark theme sampled from Kindle references, shared components (`OpusTopBar`, `OpusSearchBar`, `OpusBottomSheetContainer`, `OpusListRow`, `OpusCoverTile`, `OpusToggleRow`).
- Phase 1: Navigation shell, bottom tabs (Home | Library | More), floating resume cover, all 20 screen routes per `docs/UI_SPEC.md`.
- Phase 2: In-memory repository with real dynamic filtering, count badges, sorting, view switching (Grid, List, Collections), collection management, notifications, settings.
- Phase 3: Reader chrome, scrubber, bookmark ribbon, tap zones (25/50/25%), TOC chapter jumps, search with preview snippets, annotations management, 4 display settings tabs, and DataStore settings persistence across process restart.
- Phase 4:
  - Project Gutenberg replacement for Kindle store via `CatalogRepository` / `FakeCatalogRepository`.
  - Popular and topic shelves (Fiction, Fantasy, Classics) on Home screen.
  - Interactive topic chips ("Explore", "All", "Fiction", "Fantasy", "Classics") dynamically filtering shelves.
  - Book Details dialog with cover preview, full metadata, download statistics, and "DOWNLOAD EPUB" / "READ NOW" action.
  - Book download adds title to `BookRepository` and generates activity notification. Verified on device.
  - Local import via `SettingsScreen` file picker (`ActivityResultContracts.OpenDocument`).
  - Sideloading via Android intent filters (`ACTION_VIEW` and `ACTION_SEND` for `application/epub+zip` and `application/pdf`). Verified on device with live adb intents.

## What is Broken / Blockers
- None. All features implemented and verified against the physical Samsung Galaxy M32.

## Next Steps
- Deliver final summary and commands.
