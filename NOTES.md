# Development Notes — Opus Reader

## Current State
- **Working Directory**: `/home/bgg/projects/kindling`
- **Phase**: V2 Phase 2 — Library Area Fidelity (D01 & D04 Verified)
- **Git Branch**: `v2-phase-2`
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
3. **Settings Screen (D04)**:
   - 10 flat rows matching `docs/reference/04_settings.png` with Bookerly bold top bar, square checkboxes, and right chevrons.
   - Verified on physical Galaxy M32: vertical band offsets $\le 2\text{ px}$ across all rows.
4. **Library Grid Screen (D01)**:
   - Top bar ($y \in [145, 196]$), search pill ($y \in [254, 358]$, border $3\text{ px}$ `#8E9194`), divider ($y \in [401, 403]$), 3x3 grid + 4th row ($y \in [472, 2111]$), floating resume cover ($210\text{ px} \times 349\text{ px}$ at `start = 40.refPx`, `bottom = 97.refPx`), 4-quadrant bottom nav bar with active Library tab.
   - Verified on physical Galaxy M32: **PASS** with **1.51%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all 9 element bands.

## What is Broken / In Progress
- D02 (Home): Needs carousel alignment and genre chips matching `02_home.png`.
- D03 (More): Needs flat rows and search pill matching `03_more.png`.
- D05 (Library Filter Sheet) & D06 (Library View/Sort Sheet): Need modal sheet calibration against `05_library-filter-sheet.png` and `06_library-view-sort-sheet.png`.
- D07 (Library List): Needs list row pitch ($318\text{ px}$) calibration against `07_library-list.png`.
- D08 (Collections): Empty collections card alignment against `08_collections.png`.
- D09 (Notifications): Notifications list rows against `09_notifications.png`.

## Next Steps
- Commit D01 to `v2-phase-2`.
- Drive D02 (Home) and D03 (More) to $\le 2.0\%$ diff and $\le 1\text{ px}$ offset.
- Calibrate bottom sheets D05, D06 and list/collections screens D07, D08, D09.
- Push `v2-phase-2` and proceed to Phase 3 (Reader & Display Settings).
