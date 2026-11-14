# Development Notes — Opus Reader

## Current State
- **Phase**: Visual Audit & 1-to-1 Screen Pairing Complete!
- **Git Branch**: `phase-4`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## What is Verified
- All 32 Opus Reader screens captured and hashed (zero duplicate captures).
- All 38 Amazon Kindle live screens captured across light mode, dark mode, 4 reading themes, Aa display settings tabs, TOC, search, and annotations.
- 27 1-to-1 pairs constructed and organized in `docs/comparison_pairs/pairs/`.
- Unpaired screens cataloged into `docs/comparison_pairs/unpaired_kindle/` and `docs/comparison_pairs/unpaired_opus/`.
- Comprehensive markdown discrepancy report generated at `docs/KINDLE_VS_OPUS_COMPARISON_REPORT.md`.
- Standalone deliverable ZIP archive generated: `/home/bgg/Downloads/kindling/opus_vs_kindle_audit_package.zip` (28 MB).

## What is Broken / Discrepancies Noted
- **AMOLED True Black**: Kindle uses `#000000` for reading canvas; Opus uses `#141414` dark charcoal.
- **Sepia Theme Inks**: Kindle uses warm parchment `#FBF0D9` with deep reddish-brown `#361715` ink; Opus uses `#F7F1E3` with `#2C2C2C` neutral charcoal.
- **Green Theme Inks**: Kindle uses mint `#C5E7CE` with `#3A4B43`; Opus uses soft sage `#CEE5D8` with deep pine `#1E3A2F`.
- **Display Settings (Aa) Architecture**: Kindle uses bottom-docked half sheet leaving the page visible above; Opus uses a full modal sheet with "DONE" button.
- **Font Selection**: Kindle uses rich specimen preview cards ("AaBbCc 123"); Opus uses horizontal filter chips.
- **Text Selection Context Menu**: Kindle displays a multi-tab floating dictionary/Wikipedia popup; Opus does not yet implement interactive text selection.
- **Scrubber Thumbnail Preview**: Kindle renders 3 thumbnail page cards (`Page 295, Page 298 Current, Page 301`) above the slider track; Opus displays location/percentage text without cards.

## Next Steps
- Await user review and instructions before beginning visual or architectural refinements in `app/`.
