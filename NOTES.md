# Development Notes — Opus Reader

## Current State
- **Phase**: Phase 0 Complete. Starting Phase 1.
- **Git Branch**: `phase-0`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## What is Verified
- Gradle build, unit tests, and lint pass: `./gradlew assembleDebug testDebugUnitTest lintDebug`.
- Debug APK (`app-debug.apk`) installed and running on Galaxy M32.
- Screen capture verified at 1080x2340: TopBar, SearchBar, CoverTile, ListRow, ToggleRow all render with sampled dark theme colors (`#101112`, `#141414`, `#404040`, `#EDEDED`).
- `FakeReaderEngine` unit tests passing (`FakeReaderEngineTest`).

## What is Broken / Blockers
- None.

## Next Steps
- **Phase 1**: Implement navigation shell:
  - Bottom navigation bar: Home | Library | More
  - Floating resume cover in bottom-left opening last read book
  - Define routes and composables for all 20 screens per docs/UI_SPEC.md tap map
  - Verify navigation transitions on device
