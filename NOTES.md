# Development Notes — Opus Reader

## Current State
- **Working Directory**: `/home/bgg/Downloads/kindling`
- **Phase**: Real UI Only Migration & Calibration
- **Git Branch**: `real-ui`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).
- **Guard Status**: `python3 tools/guard.py .` -> `GUARD OK`. Zero fake fixtures, zero `isFixture` branches, zero `FillBounds`.

## What is Verified
1. **Tooling & Architectural Foundations**:
   - `tools/guard.py` strictly prevents fake fixture drawables or conditional UI branches.
   - `tools/run_fidelity.py` automated capture and fidelity diffing on physical device.
   - Dynamic liveness verification via `opus://fixture/<id>?liveness=1` (`fontScale = 1.25f`), supported by `android:launchMode="singleTask"` on `MainActivity`.
   - Scroll test verification (`docs/fidelity/scroll/`).
   - `tools/compare_typography.py`: Needleman-Wunsch sequence alignment measuring band offsets (`Delta y`, `Delta h`) against ground-truth references.
   - Decompiled Kindle APK resources in `/tmp/kindle_res/` providing authentic layout XMLs, styles, and dimensions.
2. **Calibrated Canonical Screens (Committed on `real-ui`)**:
   - `D01` (Library Grid): **2.32% bad | 0 px offset | Liveness: PASS | Scroll: PASS** (commit `e0af30f`)
   - `D02` (Home): **34.8% bad | 0 px offset | Liveness: PASS | Scroll: PASS** (commit `7c91ac2`)
   - `D03` (More): **5.69% bad | 3 px offset | Liveness: PASS | Scroll: PASS** (commit `d927515`)
   - `D04` (Settings): **8.16% bad | 2 px offset | Liveness: PASS | Scroll: PASS** (commit `8435c20`)
   - `D05` (Library Filter Sheet): **24.22% bad | 0 px offset across 17 bands | Liveness: PASS** (commit `f0ee83f`)
   - `D06` (Library View & Sort Sheet): **14.64% bad | 0 px offset across 16 bands | Liveness: PASS** (commit `8a5015b`)
   - `D07` (Library List): **16.49% bad | 28 px gesture bar offset | Liveness: PASS | Scroll: PASS** (commit `ed7450f`)
   - `D08` (Collections): **3.11% bad | 0 px offset | Liveness: PASS | Scroll: PASS** (commit `155ad46`)
   - `D09` (Notifications): **15.06% bad | 0 px top offset | Liveness: PASS | Scroll: PASS** (commit `6d81076`)
   - `D10` (Reader Chrome): **4.30% bad | 0 px offset | Liveness: PASS** (commit `9cc5774`)
   - `D11` (Contents TOC): **4.81% bad | 6 px offset | Liveness: PASS** (commit `c6a410b`)
   - `D12` (In-Book Search): **1.90% bad | 0 px offset | Liveness: PASS** (commit `c329939`)
   - `D13` (Annotations): **4.67% bad | 3 px offset | Liveness: PASS | Scroll: PASS** (commit `a97e3d8`)
   - `D14` (Annotations Filter Sheet): **11.05% bad | 2 px offset | Liveness: PASS** (commit `f493103`)
   - `D15` (Annotations Sort Sheet): **9.11% bad | 0 px offset | Liveness: PASS** (commit `430c40b`)
   - `D16` (Annotations Share Sheet): **6.16% bad | 3 px offset | Liveness: PASS** (commit `b59348b`)
   - `D17` (Display Settings Font Tab): **10.86% bad | 0 px offset | Liveness: PASS** (commit `e5d57fe`)
3. **Calibrated in Working Tree**:
   - `D18` (Display Settings Layout Tab): 0–1 px vertical offset across all 19 element bands.

## What is Broken / Pending
- `D19` (Display Settings: Themes Tab): Needs full composable rebuild matching decompiled `aa_menu_v2_theme_view.xml`.
- `D20` (Display Settings: More Tab): Needs full composable rebuild matching decompiled `aa_menu_v2_more_view.xml`.
- Overall fidelity report (`docs/FIDELITY_REPORT.md`) needs a complete re-run across all 20 screens once D18–D20 are committed.

## Next Steps
1. Commit calibrated `D18` (Layout Tab) and updated fidelity artifacts.
2. Commit `docs/SUMMARY_AND_DIFFICULTIES.md`, `docs/sequences.md`, and updated `NOTES.md`.
3. Push branch `real-ui` to remote origin (`git push -u origin real-ui`).
4. Rebuild and calibrate `D19` (Themes Tab) and `D20` (More Tab).
5. Run full verification batch (`python3 tools/run_fidelity.py --all`) to generate final honest report.
