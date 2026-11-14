# Development Notes — Opus Reader

## Current State
- **Working Directory**: `/home/bgg/projects/kindling`
- **Phase**: V2 Phase 0 — Tooling, Fixture Deep Linking & Pixel Fidelity Harness Complete
- **Git Branch**: `v2-phase-0`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## Phase 0 Tooling & Fixture Setup
1. **Repository Relocated**: App source tree and commit history synced from `~/Downloads/kindling` to `~/projects/kindling`.
2. **Legacy Audit Reset**: Removed `docs/comparison_pairs/pairs/` and legacy comparison report as instructed in `KICKOFF_PROMPT_V2.md`.
3. **Fixture Deep Linking**:
   - `app/src/debug/AndroidManifest.xml` added to intercept `opus://fixture/<id>`.
   - `MainActivity.kt` and `OpusNavHost.kt` wired to `handleFixtureDeepLink` to launch matching screen and state dynamically.
   - Verified on physical device: `adb shell am start -W -a android.intent.action.VIEW -d opus://fixture/D04` navigates directly to Settings screen.
4. **Fidelity Harness**:
   - `tools/capture.sh` tested on Galaxy M32 (produces 1080x2340 @ 420 dpi capture in system UI demo mode).
   - `tools/run_fidelity.py` implemented to run single or all canonical screens and generate `docs/FIDELITY_REPORT.md`.
   - `docs/fidelity/MASKS.md` registered with initial masks for virtual keyboard (`D12`) and canvas text area (`R03`-`R06`).
   - Verified end-to-end with `tools/run_fidelity.py --id D04`.

## Next Steps (Phase 1: Foundations)
- Font candidates verification via `ui-fonts` skill (serif titles in Bookerly, UI sans in Amazon Ember).
- Measure color and typography tokens from references.
- Implement `RefPx` helper (`px / 2.625f`).
- Build shared components in debug gallery and diff against reference crops (top bar, search pill, labelled bottom nav, list row, cover tile with badges, checkbox row, chevron row, modal bottom sheet with scrim, drag handle, and rounded corners).
