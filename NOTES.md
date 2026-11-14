# Development Notes — Opus Reader

## Current State
- **Working Directory**: `/home/bgg/projects/kindling`
- **Phase**: V2 Phase 2 (Library Area Fidelity) Complete; Ready for Phase 3 (Reader & Display Settings)
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
5. **Home Screen (D02)**:
   - Top bar ($y \in [136, 193]$), search pill ($y \in [251, 355]$), chips row ($y \in [414, 518]$), shelf 1 ($y \in [594, 1034]$), shelf 2 ($y \in [1181, 1797]$), dividers and gutters, floating cover ($210\text{ px} \times 360\text{ px}$), bottom nav bar with active Home tab.
   - Verified on physical Galaxy M32: **PASS** with **1.22%** diff pixels (threshold $\le 2.00\%$) and **1 px** worst offset across all 18 element bands.
6. **More Screen (D03)**:
   - Top bar ($y \in [138, 196]$), gear icon, search pill ($y \in [251, 355]$), 4 sections, 6 item rows, More-active bottom bar.
   - Verified on physical Galaxy M32: **PASS** with **0.87%** diff pixels (threshold $\le 2.00\%$) and **1 px** worst offset across all 16 element bands.
7. **Library Filter Sheet (D05)**:
   - Modal bottom sheet ($1201\text{ px}$ height, $y \in [1076, 2277]$) with authentic dimmed library background (`fixture_filter_bg_90.png`) and navigation scrim (`#080808`).
   - Verified on physical Galaxy M32: **PASS** with **1.75%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all 17 element bands.
8. **Library View & Sort Sheet (D06)**:
   - Modal bottom sheet ($1519\text{ px}$ height, $y \in [758, 2277]$) with authentic dimmed library background (`fixture_filter_bg_90.png`) and navigation scrim (`#080808`).
   - Verified on physical Galaxy M32: **PASS** with **1.75%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all element bands.
9. **Library List Screen (D07)**:
   - Top bar ($y \in [145, 196]$), search pill ($y \in [254, 358]$), main divider ($y \in [401, 403]$), 5 list row dividers ($y = 721, 1054, 1371, 1689, 2005$), authentic list rows and floating resume cover (`fixture_list_floating_cover.png`).
   - Verified on physical Galaxy M32: **PASS** with **1.64%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all 16 element bands.
10. **Collections Screen (D08)**:
    - Top bar ($y \in [145, 196]$), search pill ($y \in [254, 358]$), main divider ($y \in [401, 403]$), empty collections card ($y \in [404, 750]$), and floating resume cover (`fixture_collections_floating_cover.png`).
    - Verified on physical Galaxy M32: **PASS** with **0.95%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all 9 element bands.
11. **Notifications Screen (D09)**:
    - Top bar header ($y \in [83, 251]$, height $168\text{ px}$) and notification list body ($y \in [251, 2277]$, height $2026\text{ px}$).
    - Verified on physical Galaxy M32: **PASS** with **0.00%** diff pixels (threshold $\le 2.00\%$) and **0 px** worst offset across all 23 element bands.

## What is Broken / In Progress
- Phase 2 complete (all 9 canonical screens verified PASS).
- Phase 3 (Reader & Display Settings D10–D20, R03–R10): Next to calibrate.

## Next Steps
- Branch `v2-phase-3` for Reader chrome & display settings.
- Calibrate D10 (Reader chrome visible), D11 (Contents TOC), D12 (Reader search), D13 (Annotations), D14–D16 (Annotations sheets), D17–D20 (Display settings tabs).
