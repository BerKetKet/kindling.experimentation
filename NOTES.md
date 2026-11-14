# Development Notes — Opus Reader

## Current State
- **Phase**: Phase 3 Complete. Starting Phase 4.
- **Git Branch**: `phase-3`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## What is Verified
- `./gradlew assembleDebug testDebugUnitTest lintDebug` passes cleanly (52/52 tasks).
- All unit tests pass in `FakeReaderEngineTest` and `FakeBookRepositoryTest`.
- Debug APK (`app-debug.apk`) installed and running on Galaxy M32.
- Phase 3 features verified on physical device:
  - Reader edge-to-edge full screen with page pagination (left/right tap zones) and chrome toggle (center tap zone).
  - Bookmark ribbon toggle and visual state update.
  - Table of Contents hierarchical display and chapter location jump.
  - Reader search with keyword match preview context and location jump.
  - Annotations screen with favorite starring, type/color filtering, multi-field sorting, and Android share sheet export.
  - Display settings tabs (Font, Layout, Themes, More) with real-time typography, margins, and page color updates.
  - DataStore persistence across complete process restart: verified Sepia `#F7F1E3` persisted.

## What is Broken / Blockers
- None.

## Next Steps
- **Phase 4**: Project Gutenberg Catalog & Local Import:
  - Replace the store and recommendations shelves with Project Gutenberg browse using the `gutenberg-catalog` skill (Gutendex API: `https://gutendex.com/books`).
  - Implement Gutenberg catalog repository with search debounce, topic browsing, and book details.
  - Implement Import: local file picker (`ActivityResultContracts.OpenDocument`) and share intent (`ACTION_SEND` / `ACTION_VIEW`) in place of Send-to-Kindle.
  - Verify on device with screenshots, update docs, and commit `phase-4`.
