# Development Notes — Opus Reader

## Current State
- **Phase**: Phase 2 Complete. Starting Phase 3.
- **Git Branch**: `phase-2`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## What is Verified
- `./gradlew assembleDebug testDebugUnitTest lintDebug` passes cleanly (52/52 tasks).
- Unit tests (`FakeReaderEngineTest` and `FakeBookRepositoryTest`) passing.
- Debug APK (`app-debug.apk`) installed and running on Galaxy M32.
- Phase 2 features verified on physical device:
  - Dynamic filtering in Library (Read/Unread, Content type) with live count badges.
  - Dynamic sorting in Library (Most Recent, Title, Author A-Z, Author Z-A).
  - View switching (Grid <-> List <-> Collections).
  - Collections creation dialog and reactive collection list updates.
  - Notifications list, clear all, and individual item deletion.
  - Settings device name dialog, push notification, SD card, and sync toggles.
  - More screen dynamic "Sync now" action with formatted timestamp.

## What is Broken / Blockers
- None.

## Next Steps
- **Phase 3**: Reader Chrome & Settings Persistence:
  - Reader chrome: top bar, bottom scrubber slider, bookmark ribbon, tap zones (25% prev, 50% toggle chrome, 25% next).
  - Contents (TOC) jump to chapter in `ReaderEngine`.
  - Search in reader with highlighted occurrences and jump to match.
  - Annotations screen with real filter, sort, and export sheets (Markdown/text export).
  - Four display settings tabs (Font, Layout, Themes, More) wired to `ReaderEngine` and persisted via `DataStore`.
  - Verify on device with screenshots, update docs, and commit `phase-3`.
