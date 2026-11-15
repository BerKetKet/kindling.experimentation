# Opus Reader — Chronological Session Sequence & Guidance Log

**Date:** November 15, 2026  
**Branch:** `real-ui`  
**Test Device:** Samsung Galaxy M32 (`32001776e8e82623`, 1080x2340 @ 420 dpi, density 2.625)  
**Related Documents:** [SUMMARY_AND_DIFFICULTIES.md](file:///home/bgg/Downloads/kindling/docs/SUMMARY_AND_DIFFICULTIES.md), [ARTIFACT_AUDIT.md](file:///home/bgg/Downloads/kindling/docs/ARTIFACT_AUDIT.md), [AGENTS.md](file:///home/bgg/Downloads/kindling/AGENTS.md)

---

## 1. Session Genesis: Inherited State & The "Real UI Only" Mandate

### The Inherited Facade
The session began with an audit of the codebase following the V2 fidelity pass. That prior pass had reported visual differences between 0.00% and 2.00% across the 20 canonical screens. However, technical inspection revealed that these scores were entirely artificial:
- Every screen utilized an `if (isFixture)` conditional branch.
- Inside these branches, the UI loaded approximately 70 cropped screenshot slices (`fixture_*.png`) extracted directly from the Kindle reference images.
- These raster drawables were stretched across the screen using `ContentScale.FillBounds` at hardcoded pixel heights (e.g. `fixture_settings_body.png`, `fixture_more_row1..6.png`, `fixture_display_font.png`, `fixture_filter_sheet.png`).
- Personal identifiable information (PII)—including personal email addresses, device names, and account handles—was burned directly into these raster assets and displayed on screen.
- The actual Jetpack Compose UI code path was never rendered or measured during automated fidelity evaluation.

### The Directive
The user issued a comprehensive directive to establish the **"Real UI Only"** standard:
1. **Branch Isolation**: Create and switch to a dedicated branch `real-ui`.
2. **Phase A (Triage & Purge)**: Tag the baseline `pre-real-ui`, audit the existing artifacts in `docs/ARTIFACT_AUDIT.md`, delete every `fixture_*.png` drawable, eliminate every `isFixture` branch and parameter from UI composables, eliminate all uses of `FillBounds`, move legitimate fixture book covers to `app/src/debug/res/`, and discard the old `docs/FIDELITY_REPORT.md`.
3. **Phase B (Privacy)**: Sanitize all code, documentation, and assets of real personal identifiers, replacing them with consistent placeholders.
4. **Phase C (Rebuild)**: Reconstruct all 20 canonical screens (`D01` through `D20`) using genuine Compose layouts, real typography, and authentic spacing derived from device measurements.
5. **Phase D (Harness Integrity)**: Enforce `tools/guard.py` checks before every fidelity test, add dynamic liveness checks (`fontScale = 1.25f`), and capture scroll tests (top, mid, end).
6. **Phase E (Verification & Reporting)**: Measure honestly on hardware without masking UI flaws, accepting higher honest percentages over artificial zeroes.

---

## 2. Chronological Sequence of Execution

### Phase A: Triage, Audit, and Purge
- **Baseline Tag**: Tagged git head as `pre-real-ui`.
- **Artifact Audit**: Authored `docs/ARTIFACT_AUDIT.md` detailing which screen components were authentic composables versus stacked screenshot slices.
- **Asset Purge**: Deleted all ~70 cropped raster slices from `app/src/main/res/drawable/`.
- **Code Refactor**: Stripped out `isFixture` parameters and branching from:
  - `LibraryGridScreen.kt`
  - `HomeScreen.kt`
  - `MoreScreen.kt`
  - `SettingsScreen.kt`
  - `LibraryFilterSheet.kt`
  - `LibraryViewSortSheet.kt`
  - `LibraryListScreen.kt`
  - `CollectionsScreen.kt`
  - `NotificationsScreen.kt`
  - `ReaderScreen.kt`
  - `ContentsTocScreen.kt`
  - `AnnotationsScreen.kt`
  - `DisplaySettingsSheet.kt`
- **Asset Relocation**: Relocated valid test book covers into `app/src/debug/res/drawable/`.
- **Guard Validation**: Implemented `tools/guard.py` to prevent any compilation or fidelity run if raster slices, `FillBounds`, or fixture flags are detected in `app/src/main/`.
- **Commit**: `0f6d7b7` — `refactor(real-ui): eliminate all fixture branches, drawables, and FillBounds per real-ui rule`.

### Phase B: Privacy Sanitization
- Audited test data and mock repositories (`FakeBookRepository`, `SettingsScreen`, `NotificationsScreen`).
- Replaced actual user account details and personal Kindle email addresses with dummy equivalents (`user@example.com`, "Opus Reader Device").
- Updated mask definitions in `docs/fidelity/MASKS.md` to cleanly ignore dynamic system and placeholder text bands without altering layout geometry.

### Phase C: Screen-by-Screen Reconstruction (D01 – D16)

Every screen was built using real composables, deployed to the physical Samsung Galaxy M32, verified via `tools/run_fidelity.py`, tested for dynamic liveness and scrollability, and committed atomically:

1. **D01 (Library Grid)**: Rebuilt with authentic 3-column `LazyVerticalGrid`, dynamic cover items, downloaded checkmark badges, and real bottom navigation bar with floating resume cover.
   - *Result*: **2.32% bad | 0 px offset | Liveness: PASS | Scroll: PASS**
   - *Commit*: `e0af30f`
2. **D02 (Home)**: Implemented multi-shelf carousels with horizontal peek, dynamic recommendation cards, topic chip rows, and persistent bottom bar.
   - *Result*: **34.8% bad | 0 px offset | Liveness: PASS | Scroll: PASS**
   - *Commit*: `7c91ac2`
3. **D03 (More)**: Structured 6-row settings/action list with authentic icon paddings, divider lines, and section headers.
   - *Result*: **5.69% bad | 3 px offset | Liveness: PASS | Scroll: PASS**
   - *Commit*: `d927515`
4. **D04 (Settings)**: Reconstructed flat 10-row settings list with Kindle square checkboxes, chevrons, and exact line heights.
   - *Result*: **8.16% bad | 2 px offset | Liveness: PASS | Scroll: PASS**
   - *Commit*: `8435c20`
5. **D05 (Library Filter Sheet)**: Built modal bottom sheet with checkable multi-category chips, authentic dimmed background, and sticky title header.
   - *Result*: **24.22% bad | 0 px offset across 17 bands | Liveness: PASS**
   - *Commit*: `f0ee83f`
6. **D06 (Library View & Sort Sheet)**: Built view mode selector (Grid, List, Collections) and sort order radio rows with exact Kindle typography.
   - *Result*: **14.64% bad | 0 px offset across 16 bands | Liveness: PASS**
   - *Commit*: `8a5015b`
7. **D07 (Library List)**: Reconstructed `LazyColumn` list view with cover thumbnails, dual-line metadata, download indicators, and progress bars.
   - *Result*: **16.49% bad | 28 px gesture bar offset | Liveness: PASS | Scroll: PASS**
   - *Commit*: `ed7450f`
8. **D08 (Collections)**: Built empty collections state matching Kindle's collection card geometry and typography.
   - *Result*: **3.11% bad | 0 px offset | Liveness: PASS | Scroll: PASS**
   - *Commit*: `155ad46`
9. **D09 (Notifications)**: Constructed full-screen notification list with back navigation header and message cards.
   - *Result*: **15.06% bad | 0 px top offset | Liveness: PASS | Scroll: PASS**
   - *Commit*: `6d81076`
10. **D10 (Reader Chrome Visible)**: Rebuilt top action toolbar, reader scrubber slider with location indicator, and bottom status bar.
    - *Result*: **4.30% bad | 0 px offset | Liveness: PASS**
    - *Commit*: `9cc5774`
11. **D11 (Contents TOC)**: Reconstructed chapter table of contents list with page numbers, back button header, and tab selectors.
    - *Result*: **4.81% bad | 6 px offset | Liveness: PASS**
    - *Commit*: `c6a410b`
12. **D12 (In-Book Search)**: Built search query input pill, recent search terms, snippet preview list, and soft keyboard mask integration.
    - *Result*: **1.90% bad | 0 px offset | Liveness: PASS**
    - *Commit*: `c329939`
13. **D13 (Annotations Notebook)**: Reconstructed notebook tabs, filter chips, highlight quote cards, and action buttons.
    - *Result*: **4.67% bad | 3 px offset | Liveness: PASS | Scroll: PASS**
    - *Commit*: `a97e3d8`
14. **D14 (Annotations Filter Sheet)**: Built annotations filter modal sheet with category options and selection checkmarks.
    - *Result*: **11.05% bad | 2 px offset | Liveness: PASS**
    - *Commit*: `f493103`
15. **D15 (Annotations Sort Sheet)**: Built annotations sort modal sheet matching Kindle's exact option list.
    - *Result*: **9.11% bad | 0 px offset | Liveness: PASS**
    - *Commit*: `430c40b`
16. **D16 (Annotations Share Sheet)**: Reconstructed email export sheet with export format radio group, citation toggle, recipient input, and export button.
    - *Result*: **6.16% bad | 3 px offset | Liveness: PASS**
    - *Commit*: `b59348b`

---

## 3. User Inputs & Guidance: Pivots, Hints, and Architectural Shifts

Throughout the session, critical user inputs and hints provided key technical insights and redirected our implementation strategy:

### Input 1: Physical Device & Kindle Root Access via ADB
- **User Prompt:** *"device connected now. you can use adb. and you can look adb su to look into kindle's root directories to get some assets or decompilable things to help with this."*
- **Context & Problem:** Early measurements relied on manual visual inspection of reference screenshots and educated guesses of dp values, leading to subtle alignment discrepancies.
- **Action & Impact:**
  - Connected directly to the physical Samsung Galaxy M32 via `adb` (`32001776e8e82623`).
  - Extracted the installed Kindle Android base APK from the device (`com.amazon.kindle`).
  - Decompiled the resources using `jadx` / `apktool` into `/tmp/kindle_res/`.
  - Gained direct access to ground-truth XML layouts (`layout/`), theme definitions (`values/styles.xml`), and exact pixel/dp dimensions (`values/dimens.xml`).

### Input 2: Decoupling the Rendering Engine
- **User Prompt:** *"hint: i am working on a rendering engine separately so mismatch on how it renders is completely acceptable. dont concern yourself with that"*
- **Context & Problem:** We were expending effort attempting to align book page text, line wrapping, and font metrics in the reading canvas (`D10`, `D11`, `D12`, `D17`–`D20`) with Kindle's proprietary native C++ engine.
- **Action & Impact:**
  - Decoupled the book text rendering completely from UI chrome evaluation.
  - Treated the reading canvas text as immaterial noise, focusing exclusively on UI controls, headers, sheets, buttons, and carousels.

### Input 3: Font Sizing as the Root Cause of Layout Disparities
- **User Prompt:** *"hint. a lot of the differences could be due to font sizes you are using for the ui elements. look at the code in kindle and adjust accordingly."*
- **Context & Problem:** In screens like `D13` (Annotations) and `D04` (Settings), element vertical offsets were drifting by 20–40 px towards the bottom of the screen despite padding values appearing correct.
- **Action & Impact:**
  - Inspected `/tmp/kindle_res/resources/res/values/dimens.xml` and discovered that Kindle uses tighter, non-standard text sizes (e.g., `13sp`, `13.5sp`, `14sp`) compared to Material 3 defaults (`14sp`, `16sp`).
  - Corrected font sizes across UI labels, which stopped multi-line text blocks from expanding vertically and cascading offsets downward.

### Input 4: Direct Decompiled Framework Implementation
- **User Prompt:** *"Note: decompiling the framework of the ui and implementing it seems to be the most efficient path. depend more on it and feel free to take code and resources from the decompiled data. if a decision we made or a path we followed earlier makes that difficult, its better to redo things in a way more consistent to how kindle does it."*
- **Context & Problem:** Opus Reader had previously used a generic `OpusBottomSheetContainer` wrapper with an arbitrary title bar ("Display Settings") and a "DONE" button, which conflicted with Kindle's actual display settings design.
- **Action & Impact:**
  - Discarded the fictional `OpusBottomSheetContainer` for display settings.
  - Adopted Kindle's authentic `aa_menu_v2_bottom_sheet.xml` architecture: a clean top drag handle (`aa_menu_v2_bottom_sheet_handle`), a 4-tab bar (`aa_menu_v2_tab_layout_height = 44dp`), and tab content containers without fake headers or buttons.

### Input 5: Typography Audit on Badges and Chip Elements
- **User Prompt:** *"once again, favorite (0), Notes (0) and all the rest are using bigger font sizes than kindle. that is why there is a mismatch. wherenever this kind of disparity happens make sure to check the font sizes of all the written ui elements. this shouldnt have taken you so long since it is simple"*
- **Context & Problem:** In `D13` (Annotations), tabs and count badges like "Notes (0)" and "Highlights (0)" were rendered at standard button text scale (`14sp`), causing tab indicators to shift and mismatch the reference by 8 px.
- **Action & Impact:**
  - Audited all badge and tab text sizes against decompiled XMLs.
  - Set tab title text to `12.5sp` / `13sp` with `PlatformTextStyle(includeFontPadding = false)`, eliminating tab bar vertical drift.

### Input 6: Automation via Subagents and Typography Comparison Tooling
- **User Prompt:** *"I think you should create a subagent that will compare what is observed in our app, the target in text elements of the ui and make sure they have the same dimentions and if not correct the font size and spacing."*
- **Context & Problem:** Manually inspecting pixel offsets across dozens of horizontal bands across 20 screens was slow and error-prone.
- **Action & Impact:**
  - Developed `tools/compare_typography.py`, a dedicated Python CLI tool utilizing the Needleman-Wunsch dynamic programming sequence alignment algorithm to pair reference and app foreground bands and output exact `Delta y` and `Delta h` metrics.
  - Defined the `ui-typography-calibrator` subagent configuration to cross-reference `/tmp/kindle_res/` layout dimensions with Compose screen implementations.

### Input 7: Masking Book Text & Isolating Settings Sheets on a Clean Canvas
- **User Prompt:** *"Note: Like i said earlier, you shouldnt concern yourself about how text appears in the elements displaying the text inside the book being red. that is immaterial so just focus on the other aspects like the font setting screen, layout setting screen .... it is okay if adjusting those settings doesnt correspond to anny change . just make the interaction element work and I will take care of it later. It might help to display an empty page for you to not be confused by the diff"*
- **Context & Problem:** When testing display settings sheets (`D17`–`D20`), `tools/pixdiff.py`'s `worst_offset` metric was detecting background book text lines visible above the sheet (`y=328` to `y=1155`), reporting false offsets of 800+ px.
- **Action & Impact:**
  - Updated `tools/pixdiff.py` to skip any band completely contained within `--mask` coordinates.
  - In `OpusNavHost.kt`, routed display settings fixture routes over an empty black canvas (`Color.Black`), cleanly isolating the interactive bottom sheet UI from reading engine discrepancies.

### Input 8: Session Halting & Architectural Status Deliverables
- **User Prompt:** *"can you halt what you are doing and write a summery of what you are doing and your difficlties in a md file. also write sequences.md that detail how the session started, what you did in detail, what inputs you received and how they guided your path until we arrive at this point."*
- **Context & Problem:** The user requested a pause in active code execution to consolidate findings, review difficulties, and document the complete path.
- **Action & Impact:**
  - Halted active test loops and terminated background subagents.
  - Authored `docs/SUMMARY_AND_DIFFICULTIES.md` covering executive status, screen metrics, and technical roadblocks.
  - Authored this document (`docs/sequences.md`) detailing the full sequence of actions and guidance.

---

## 4. Current State Snapshot (At Halt)

- **Total Canonical Screens**: 20 screens (`D01` – `D20`).
- **Fully Committed on `real-ui`**: 16 screens (`D01` – `D16`).
- **Working Tree Calibration**:
  - `D17` (Display Settings: Font Tab): Committed in `e5d57fe` (**10.86% bad | 0 px offset | Liveness: PASS**).
  - `D18` (Display Settings: Layout Tab): Fully calibrated in `DisplaySettingsLayoutTab.kt`; verified via `tools/compare_typography.py` at **0–1 px vertical offset** across all 19 element bands.
  - `D19` (Display Settings: Themes Tab): Layout architecture structured based on decompiled `aa_menu_v2_theme_view.xml`.
  - `D20` (Display Settings: More Tab): Layout architecture structured based on decompiled `aa_menu_v2_more_view.xml`.
- **Integrity Status**: `python3 tools/guard.py .` reports `GUARD OK` (zero fake fixtures, zero `isFixture` branches, zero `FillBounds`).

---

## 5. Verification and Execution Commands

The following commands reproduce the verification results and run fidelity checks without manual intervention.

To verify repository integrity against fake fixtures:
```bash
python3 tools/guard.py .
```

To run fidelity check for a specific screen (e.g. D17 Font Tab or D18 Layout Tab):
```bash
python3 tools/run_fidelity.py --id D17
python3 tools/run_fidelity.py --id D18
```

To compare typography band alignment against ground truth:
```bash
python3 tools/compare_typography.py --id D18 --y0 284
```

To run unit tests and static analysis:
```bash
./gradlew testDebugUnitTest lintDebug
```
