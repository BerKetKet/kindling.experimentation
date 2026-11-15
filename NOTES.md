# Development Notes — Opus Reader

## Current State
- **Working Directory**: `/home/bgg/projects/kindling`
- **Phase**: Pondering Branch Integration & Full Fidelity Audit
- **Git Branch**: `pondering`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).
- **Guard Status**: `python3 tools/guard.py .` -> `GUARD PASSED: 0 screenshot slices, 0 fixture branches, 0 FillBounds hacks.`

## What is Verified
1. **Tooling & Architectural Compliance**:
   - `tools/guard.py` verification passes with zero forbidden raster slices, zero `isFixture` branches, and zero `ContentScale.FillBounds`.
   - Android lint (`./gradlew lintDebug`) and unit tests (`./gradlew testDebugUnitTest`) pass cleanly.
   - Debug build (`./gradlew assembleDebug installDebug`) installs and launches cleanly on hardware.
2. **Hardware Fidelity Suite Evaluation**:
   - `python3 tools/run_fidelity.py --all` executed across all 43 canonical screens on Samsung Galaxy M32.
   - Comprehensive test report generated in `docs/FIDELITY_REPORT.md`.
   - Screens evaluated include:
     - Dark Theme Canonical Screens: D01–D20
     - Light Theme Screens: L01–L09
     - Reader Canvas and Theme Screens: R01–R14

## Next Steps
- Continue iterative layout and typography calibration on failing screens according to reference images.
- Refine background masking where applicable.
