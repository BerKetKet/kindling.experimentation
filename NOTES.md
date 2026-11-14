# Development Notes — Opus Reader

## Current State
- **Phase**: Visual Audit & 1-to-1 Screen Pairing (OCR-Verified & Corrected)
- **Git Branch**: `phase-4`
- **Gradle & Toolchain**: Gradle 8.7, AGP 8.5.2, Kotlin 2.0.21, Compose BOM 2024.09.00, compileSdk 35, minSdk 26.
- **Test Device**: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625) connected via adb (`32001776e8e82623`).

## Root Cause Analysis of Previous Pairing Error
1. **Canonical ID Inversion**: Screen 01 is canonically Library Grid (`01_library-grid.png` in `docs/reference/`), while Screen 02 is canonically Home (`02_home.png` in `docs/reference/`). The earlier run labeled Screen 01 "Home" and Screen 02 "Library Grid".
2. **Sub-Activity Capture Contamination**: The previous Kindle capture for Home occurred while the device was nested in `Kindle Store -> Shopping List`, and the Library Grid capture occurred while active in the book reader canvas.
3. **Remediation**: All 29 comparison pairs were re-captured, verified via Tesseract OCR (`--psm 6`), and confirmed against UI Automator XML dumps and pixel color sampling.

## What is Verified
- **29 1-to-1 Verified Pairs** in `docs/comparison_pairs/pairs/`:
  - `pair_01_library_grid`: Verified OCR `Library = N | Q Search Kindle | Franz Kafka` vs `Library = = | Q Search Library | Gardens of the...`.
  - `pair_02_home`: Verified OCR `Home Q | Q Search Kindle | From Your Library` vs `Home A | Q Search Opus & Gutenberg | From Your Library`.
  - `pair_03_more`: Verified OCR `More | Sync | Challenges` vs `More | Q Search Opus | Sync`.
  - `pair_04_settings`: Verified OCR `Settings | Device name | bereket's 11th...` vs `Settings | Device & Account | Device name`.
  - `pair_05_library_filter_sheet`: Verified OCR `Filter DONE | Download state | Read state (16)` vs `Filter DONE | Read state | Read (3)`.
  - `pair_06_library_view_sort_sheet`: Verified OCR `View DONE | Group by Series | Grid | List` vs `View DONE | Group by Series | Grid | List`.
  - `pair_07_library_list`: Verified OCR `Library = N | Complete Works (Kafka, Franz)` in list row layout vs `Library = = | Gardens of the Moon: Book One`.
  - `pair_08_collections`: Verified OCR `Collections + | There are no collections in your library` vs `Collections + | All-Time Favorites`.
  - `pair_09_notifications`: Verified OCR `Notifications` vs `Notifications | Library Synchronized`.
  - `pair_10_reader_chrome_visible`: Verified reader chrome top bar, title, scrubber, and bottom bar.
  - `pair_11_reader_reading_black`: Verified clean text canvas with chrome hidden.
  - `pair_12_contents_toc`: Verified table of contents chapter list and close button.
  - `pair_13_reader_search`: Verified in-book search field.
  - `pair_14_annotations_main`: Verified annotations notebook grouped by location.
  - `pair_15_annotations_filter_sheet`: Verified annotations category filter sheet.
  - `pair_16_annotations_sort_sheet`: Verified annotations sort sheet.
  - `pair_17_annotations_export_sheet`: Verified export notes dialog/sheet.
  - `pair_18_display_settings_font_tab`: Verified font family, font size, and boldness controls.
  - `pair_19_display_settings_layout_tab`: Verified page color swatches, alignment, margins, line spacing.
  - `pair_20_display_settings_themes_tab`: Verified reader theme presets.
  - `pair_21_display_settings_more_tab`: Verified assistive reading and progress indicators.
  - `pair_22_theme_white_reading` & `pair_26_theme_white_chrome`: White theme canvas & chrome.
  - `pair_23_theme_sepia_reading` & `pair_27_theme_sepia_chrome`: Sepia theme canvas & chrome.
  - `pair_24_theme_green_reading` & `pair_28_theme_green_chrome`: Green theme canvas & chrome.
  - `pair_25_theme_black_reading` & `pair_29_theme_black_chrome`: Black theme canvas & chrome.
- **Unpaired Screens Cataloged** in `docs/comparison_pairs/unpaired_kindle/` and `docs/comparison_pairs/unpaired_opus/`.
- **Standalone Deliverable Package**: `/home/bgg/Downloads/kindling/opus_vs_kindle_audit_package.zip` (40 MB).

## What is Broken / Discrepancies Noted
- **OLED True Black**: Kindle uses `#000000` for reading canvas; Opus uses `#141414` dark charcoal.
- **Sepia Theme Inks**: Kindle adjusts sepia dynamically; Opus uses fixed warm parchment `#F7F1E3`.
- **Green Theme Inks**: Kindle uses `#C5E7CE`; Opus uses `#CEE5D8`.
- **Display Settings (Aa) Sheet Style**: Kindle uses a half-height sheet leaving the top page visible; Opus uses a full modal sheet with "DONE" button.
- **Text Selection Context Menu**: Kindle displays a multi-tab floating dictionary/Wikipedia popup; Opus does not yet implement interactive text selection.
- **Scrubber Thumbnail Preview**: Kindle renders thumbnail page cards above the slider track; Opus displays location/percentage text without cards.

## Next Steps
- Await user review and instructions before making any code modifications in `app/`.
