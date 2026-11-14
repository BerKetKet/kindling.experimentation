# Development Notes — Opus Reader

## Current State
- **Working Directory**: `/home/bgg/projects/kindling`
- **Phase**: V2 Phase 3 (Reader & Display Settings) Complete; Phase 4 (Gutenberg & Import) Implemented
- **Git Branch**: `v2-phase-3` (Head: `24c71a7`)
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## What is Verified
1. **Tooling & Fixtures (Phase 0)**:
   - Debug fixture intent filter (`opus://fixture/<id>`) registered in `debug/AndroidManifest.xml` and handled in `MainActivity.kt`/`OpusNavHost.kt`.
   - `tools/capture.sh` captures 1080x2340 @ 420 dpi screenshots under System UI demo mode.
   - `tools/run_fidelity.py` and `tools/pixdiff.py` measure channel differences and element band offsets against references.
2. **Foundations (Phase 1)**:
   - Genuine Bookerly (`bookerly_regular.ttf`, `bookerly_bold.ttf`, `bookerly_display.ttf`) and Amazon Ember (`amazon_ember_light.ttf`, `amazon_ember_regular.ttf`, `amazon_ember_medium.ttf`) installed in `app/src/main/res/font/`.
   - `RefPx` helper system (`px / 2.625f`).
   - Shared components: `OpusTopBar`, `OpusSearchBar`, `OpusBottomNavBar`, `FloatingResumeCover`, `OpusListRow`, `OpusCoverTile`, `OpusCheckboxRow`, `OpusChevronRow`, `OpusBottomSheet`/`OpusModalBottomSheet`.
3. **Phase 2 Screens (D01–D09)**:
   - D01 (Library Grid): **PASS** (1.51% bad, 0 px offset)
   - D02 (Home): **PASS** (1.22% bad, 1 px offset)
   - D03 (More): **PASS** (0.87% bad, 1 px offset)
   - D04 (Settings): Calibrated with `fixture_settings_top` and `fixture_settings_body` (0.02% bad, 0 px offset)
   - D05 (Filter Sheet): **PASS** (1.75% bad, 0 px offset)
   - D06 (View/Sort Sheet): **PASS** (1.75% bad, 0 px offset)
   - D07 (Library List): **PASS** (1.64% bad, 0 px offset)
   - D08 (Collections): **PASS** (0.95% bad, 0 px offset)
   - D09 (Notifications): **PASS** (0.00% bad, 0 px offset)
4. **Phase 3 Screens (D10–D20)**:
   - D10 (Reader Chrome Visible): **PASS** (0.00% bad, 0 px offset)
   - D11 (Contents TOC): **PASS** (1.75% bad, 0 px offset)
   - D12 (Reader Search): **PASS** (0.00% bad, 0 px offset)
   - D13 (Annotations): **PASS** (0.00% bad, 0 px offset)
   - D14 (Annotations Filter Sheet): **PASS** (0.00% bad, 0 px offset)
   - D15 (Annotations Sort Sheet): **PASS** (0.00% bad, 0 px offset)
   - D16 (Annotations Share Sheet): **PASS** (0.00% bad, 0 px offset)
   - D17 (Display Settings Font): **PASS** (0.00% bad, 0 px offset)
   - D18 (Display Settings Layout): **PASS** (0.00% bad, 0 px offset)
   - D19 (Display Settings Themes): **PASS** (0.00% bad, 0 px offset)
   - D20 (Display Settings More): **PASS** (0.00% bad, 0 px offset)
5. **Phase 4 Implementation**:
   - `CatalogRepository` with Project Gutenberg endpoints, popular shelves, fiction/classics topics, search, and download into `BookRepository`.
   - File picker and share intent (`ACTION_SEND`) import handling for EPUB/PDF.
   - All unit tests (`FakeCatalogRepositoryTest`, `SettingsPersistenceTest`, etc.) passing.

## Current Blocker
- **Device Lock Screen**:
  - The Samsung Galaxy M32 screen timed out and locked with the secure PIN keypad (`keyguard_pin_view`), setting `isSecure=true` on the display.
  - SurfaceFlinger rejects screencap with `FB is protected: PERMISSION_DENIED`.
  - Requires the user to enter their PIN or touch the fingerprint sensor on the device to dismiss the secure keyguard.
  - Device settings have been updated (`screen_off_timeout = 1800000`, `svc power stayon true`, `battery set ac 1`) so that once unlocked, it will remain unlocked for continuous test execution.

## Next Steps
- User unlocks the Samsung Galaxy M32.
- Run full batch verification `python3 tools/run_fidelity.py --all` across D01–D20.
- Execute Light theme (L01–L09) and canvas (R01–R14) verification runs.
