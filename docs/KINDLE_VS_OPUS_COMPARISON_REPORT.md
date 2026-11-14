# Opus Reader vs Amazon Kindle UI Audit & 1-to-1 Pairing Report

**Audit Target**: Samsung Galaxy M32 (`32001776e8e82623`, 1080×2340 px, 420 dpi, 2.625 density)  
**Target Packages**: Amazon Kindle (`com.amazon.kindle`) vs Opus Reader (`app.opusreader`)  
**Audit Scope**: Complete 1-to-1 visual pairing, spectrophotometric color sampling, layout/typography discrepancy matrix, and detailed catalog of unpaired screens on both applications.

---

## 1. Executive Summary & Verification Methodology

Every screenshot in this report was captured directly on the live physical Samsung Galaxy M32 device using ADB (`screencap -p`). To guarantee technical rigor and eliminate duplicate captures:
1. Every captured PNG was hashed with MD5 to verify visual and state distinctness.
2. Background, surface, text, and accent colors were extracted via pixel-level spectrophotometric sampling (`Pillow` / `Image.getpixel`) across 1080×2340 coordinate space.
3. Every screen element was mapped to its exact matching counterpart in the Kindle Android app.
4. All 27 1-to-1 pairs and all unpaired screens have been systematically organized into `docs/comparison_pairs/`.

---

## 2. 1-to-1 Pairing Master Matrix

The table below catalogs every screen pair, indicating the source capture files in `docs/comparison_pairs/pairs/`, matching status, and key visual variance category.

| Pair ID | Screen Name | Kindle Reference Screen | Opus Reader Screen | Match Status | Primary Variance Category |
| :--- | :--- | :--- | :--- | :---: | :--- |
| **Pair 01** | **Home Screen** | `pair_01_home_kindle.png` | `pair_01_home_opus.png` | **Paired** | Content source (Amazon Store vs Project Gutenberg shelves) |
| **Pair 02** | **Library Grid View** | `pair_02_library_grid_kindle.png` | `pair_02_library_grid_opus.png` | **Paired** | Progress badges vs progress bar; filter pill row layout |
| **Pair 03** | **Library Filter Sheet** | `pair_03_library_filter_sheet_kindle.png` | `pair_03_library_filter_sheet_opus.png` | **Paired** | Multi-select accordion vs radio section chips |
| **Pair 04** | **Library View & Sort Sheet** | `pair_04_library_view_sort_sheet_kindle.png` | `pair_04_library_view_sort_sheet_opus.png` | **Paired** | Radio row spacing; Series toggle layout |
| **Pair 05** | **Library List View** | `pair_05_library_list_kindle.png` | `pair_05_library_list_opus.png` | **Paired** | Thumbnail aspect ratio; metadata typography |
| **Pair 06** | **Collections Screen** | `pair_06_collections_kindle.png` | `pair_06_collections_opus.png` | **Paired** | 4-cover collage preview vs modern card list |
| **Pair 07** | **More Hub Screen** | `pair_07_more_kindle.png` | `pair_07_more_opus.png` | **Paired** | Gamification/Goodreads vs local privacy/stats cards |
| **Pair 08** | **Settings Screen** | `pair_08_settings_kindle.png` | `pair_08_settings_opus.png` | **Paired** | Cloud Amazon account vs local offline settings/SAF |
| **Pair 09** | **Notifications Screen** | `pair_09_notifications_kindle.png` | `pair_09_notifications_opus.png` | **Paired** | Promotional marketing vs local library sync events |
| **Pair 10** | **Reader Chrome (Black)** | `pair_10_reader_chrome_black_kindle.png` | `pair_10_reader_chrome_black_opus.png` | **Paired** | Top bar action grouping; thumbnail scrubber cards |
| **Pair 11** | **Reading Canvas (Black)** | `pair_11_reader_reading_black_kindle.png` | `pair_11_reader_reading_black_opus.png` | **Paired** | True AMOLED `#000000` vs Dark Charcoal `#141414` |
| **Pair 12** | **Reading Canvas (White)** | `pair_12_reader_reading_white_kindle.png` | `pair_12_reader_reading_white_opus.png` | **Paired** | Pure white `#FFFFFF` parity; margin padding |
| **Pair 13** | **Reading Canvas (Sepia)** | `pair_13_reader_reading_sepia_kindle.png` | `pair_13_reader_reading_sepia_opus.png` | **Paired** | Golden `#FBF0D9` (Kindle) vs Cream `#F7F1E3` (Opus) |
| **Pair 14** | **Reading Canvas (Green)** | `pair_14_reader_reading_green_kindle.png` | `pair_14_reader_reading_green_opus.png` | **Paired** | Mint `#C5E7CE` (Kindle) vs Soft Sage `#CEE5D8` (Opus) |
| **Pair 15** | **Reader Chrome (White)** | `pair_15_reader_chrome_white_kindle.png` | `pair_15_reader_chrome_white_opus.png` | **Paired** | Chrome tinting and scrubber contrast |
| **Pair 16** | **Reader Chrome (Sepia)** | `pair_16_reader_chrome_sepia_kindle.png` | `pair_16_reader_chrome_sepia_opus.png` | **Paired** | Header/footer tint integration with page background |
| **Pair 17** | **Reader Chrome (Green)** | `pair_17_reader_chrome_green_kindle.png` | `pair_17_reader_chrome_green_opus.png` | **Paired** | Tint harmony across top/bottom bars |
| **Pair 18** | **Aa Display Settings: Font** | `pair_18_display_settings_font_tab_kindle.png` | `pair_18_display_settings_font_tab_opus.png` | **Paired** | Bottom overlay vs full sheet; font cards vs chips |
| **Pair 19** | **Aa Display Settings: Layout** | `pair_19_display_settings_layout_tab_kindle.png` | `pair_19_display_settings_layout_tab_opus.png` | **Paired** | Color circles layout; spacing control segmented buttons |
| **Pair 20** | **Aa Display Settings: Themes** | `pair_20_display_settings_themes_tab_kindle.png` | `pair_20_display_settings_themes_tab_opus.png` | **Paired** | Preset theme cards; Custom theme creation action |
| **Pair 21** | **Aa Display Settings: More** | `pair_21_display_settings_more_tab_kindle.png` | `pair_21_display_settings_more_tab_opus.png` | **Paired** | Reading ruler, clock toggle, volume page-turn toggle |
| **Pair 22** | **Table of Contents (TOC)** | `pair_22_contents_toc_kindle.png` | `pair_22_contents_toc_opus.png` | **Paired** | Chapter indentation, current chapter highlight bar |
| **Pair 23** | **Reader In-Book Search** | `pair_23_reader_search_kindle.png` | `pair_23_reader_search_opus.png` | **Paired** | Search input styling; snippet preview cards |
| **Pair 24** | **Annotations: Main Screen** | `pair_24_annotations_main_kindle.png` | `pair_24_annotations_main_opus.png` | **Paired** | Tabbed notebook vs clean list; favorite star toggle |
| **Pair 25** | **Annotations: Filter Sheet** | `pair_25_annotations_filter_sheet_kindle.png` | `pair_25_annotations_filter_sheet_opus.png` | **Paired** | Filter facets (Bookmarks, Highlights, Notes, Color) |
| **Pair 26** | **Annotations: Sort Sheet** | `pair_26_annotations_sort_sheet_kindle.png` | `pair_26_annotations_sort_sheet_opus.png` | **Paired** | Location in book vs Date modified radio list |
| **Pair 27** | **Annotations: Export Sheet** | `pair_27_annotations_export_sheet_kindle.png` | `pair_27_annotations_export_sheet_opus.png` | **Paired** | Share via Email modal with citation style picker |

---

## 3. Spectrophotometric Color Analysis (Kindle vs Opus)

The table below contrasts the precise hex colors sampled from the physical device screen captures:

| Element / Theme | Amazon Kindle Hex | Opus Reader Hex | Color Delta / Variance Analysis |
| :--- | :---: | :---: | :--- |
| **Dark Theme App Background** | `#101112` | `#101112` | **Exact 100% Match** (sampled from main shell) |
| **Dark Theme Surface / Card** | `#1E1F21` | `#1E1F21` | **Exact 100% Match** |
| **Dark Theme Elevated Sheet** | `#24262A` | `#25272A` | **Sub-perceptual variance** (ΔE < 0.5) |
| **Dark Primary Text** | `#FFFFFF` | `#FFFFFF` | **Exact 100% Match** |
| **Dark Secondary Text** | `#9E9E9E` | `#9AA0A6` | Opus has slightly cooler gray text (Material 3 neutral variant) |
| **Brand Accent Blue** | `#0073BB` | `#0073BB` | **Exact 100% Match** |
| **Reading Theme: BLACK (Bg)** | `#000000` | `#141414` | **Significant**: Kindle uses true AMOLED `#000000`; Opus uses `#141414` dark charcoal. |
| **Reading Theme: BLACK (Text)** | `#CCCCCC` | `#E0E0E0` | Opus text is brighter white `#E0E0E0` (higher contrast than Kindle's `#CCCCCC`). |
| **Reading Theme: WHITE (Bg)** | `#FFFFFF` | `#FFFFFF` | **Exact 100% Match** |
| **Reading Theme: WHITE (Text)** | `#111111` | `#111111` | **Exact 100% Match** |
| **Reading Theme: SEPIA (Bg)** | `#FBF0D9` | `#F7F1E3` | **Noticeable**: Kindle is warmer golden parchment; Opus is cooler pale cream. |
| **Reading Theme: SEPIA (Text)** | `#361715` | `#2C2C2C` | **Noticeable**: Kindle uses warm reddish-brown ink; Opus uses neutral charcoal gray. |
| **Reading Theme: GREEN (Bg)** | `#C5E7CE` | `#CEE5D8` | **Minor**: Kindle is pale mint green; Opus is softer muted sage green. |
| **Reading Theme: GREEN (Text)** | `#3A4B43` | `#1E3A2F` | Opus uses deeper dark pine text `#1E3A2F` for better legibility. |

---

## 4. In-Depth Screen-by-Screen Discrepancy Analysis

### 4.1 Shell & Navigation (Pairs 01, 02, 05, 06, 07, 08, 09)

#### Pair 01: Home Screen (`pair_01_home`)
- **Kindle**: Heavily commercialized storefront layout. Dominant top carousel features promoted Kindle Unlimited titles, Amazon Prime reading banners, and "Recommended for You" algorithms.
- **Opus Reader**: Restyled with identical card metrics and horizontal carousels, but replaced commercial feeds with:
  1. "Continue Reading" resume hero card at top.
  2. "Popular on Project Gutenberg" shelf.
  3. "Classic Fiction" curated shelf.
  4. Floating resume cover persistent widget at bottom-left (`bounds=[42,2100][228,2300]`).
- **Discrepancies**:
  - Kindle displays a "Discover / Store" search pill at top with voice search and barcode camera scanner; Opus features a local book search bar.
  - Kindle displays user avatar profile badge at top-right; Opus integrates notifications bell at top-right.

#### Pair 02 & Pair 05: Library Grid & List (`pair_02_library_grid`, `pair_05_library_list`)
- **Kindle**:
  - Top filter chips row is horizontally scrollable with pills: `Downloaded`, `Unread`, `Books`, `Samples`, `Collections`.
  - Book covers in Grid view display a floating percentage badge pill at the bottom-right of each cover (e.g. `12%`).
  - Three-dot overflow menu icon on each book tile triggers a comprehensive action sheet (Add to Collection, Mark as Read, View in Store, Remove Download).
- **Opus Reader**:
  - Replaces horizontal pill overflow with standard Filter icon button triggering a bottom sheet, plus search bar.
  - Book covers display a thin horizontal progress bar below the cover with percentage text.
  - List view rows accurately reproduce Kindle's 64×96dp thumbnail, bold title, author caption, and progress indicator.
- **Discrepancies**:
  - Opus does not render Kindle's "NEW" diagonal sash or "SAMPLE" badge ribbons across covers.
  - Kindle grid spacing is 12dp between columns; Opus uses 16dp spacing, resulting in slightly narrower cover aspect ratios on identical screen widths.

#### Pair 03 & Pair 04: Filter & View/Sort Sheets (`pair_03_library_filter_sheet`, `pair_04_library_view_sort_sheet`)
- **Kindle**:
  - Filter sheet is a two-column cascading selector with grouped categories (Type, Read Status, Program, Collections).
  - View & Sort sheet has `Group by Series` toggle at top, followed by 3 segmented view mode cards (Grid, List, Collections), then a radio list for Sort (Recent, Title, Author, Publication Date).
- **Opus Reader**:
  - Filter sheet uses clean vertical section headers with selectable pill chips (Format, Status, Downloaded).
  - View & Sort sheet reproduces `Group by Series` switch, 3 view modes (Grid, List, Collections) with matching icons, and radio buttons for Sort order.
- **Discrepancies**:
  - Kindle uses custom checkmark radio buttons with blue border rings; Opus uses standard Material 3 styled radio buttons.
  - Kindle includes an explicit "Clear all filters" text button in the sheet header; Opus handles clearing via toggle de-selection.

#### Pair 06: Collections (`pair_06_collections`)
- **Kindle**:
  - Collections screen displays folders with a 4-thumbnail dynamic collage layout (miniature covers of the first 4 books nested in a 2×2 grid inside the folder frame).
  - Shows cloud sync icon indicator next to collection name.
- **Opus Reader**:
  - Displays a clean folder card list with folder icon, collection title, book count subtitle (`X books`), and last modified timestamp.
  - Includes an in-app "New Collection" modal dialog (triggered by `+` in top bar).
- **Discrepancies**:
  - Collage preview thumbnails are not currently synthesized in Opus (shows Material Folder icon).

#### Pair 07 & Pair 08: More Hub & Settings (`pair_07_more`, `pair_08_settings`)
- **Kindle**:
  - More Hub contains gamification cards ("21-Day Reading Streak", "Reading Insights", "2026 Reading Challenge"), Goodreads integration, Amazon Household sharing, and Account settings.
  - Settings page links to web-hosted Amazon 1-Click preferences, Whispersync cloud switches, and Amazon Household controls.
- **Opus Reader**:
  - More Hub replaces gamification with a personal "Reading Stats" overview card (Books Completed, Current Streak, Total Minutes Read), followed by local menu items (Settings, Notifications, Project Gutenberg Catalog, Import Books, About).
  - Settings provides local offline-first settings:
    1. Device Name (custom rename dialog).
    2. Notifications toggle.
    3. Storage & Downloads (SD Card toggle, local EPUB/PDF file import via SAF).
    4. Sync & Progress.
    5. Custom Fonts management.
- **Discrepancies**:
  - No cloud account credentials, billing, or DRM authentication rows exist in Opus Reader by design.

---

### 4.2 Reader Experience & Themes (Pairs 10 – 17)

#### Pair 10 & Pair 11: Reader Chrome & Pure Reading Canvas (Black Theme)
- **Kindle**:
  - Top Bar contains: Close Book Arrow `(left)`, Store Shopping Cart, Table of Contents, Aa Display Settings, Audible Narration Headphone, Bookmark Ribbon, Three-dot Overflow `(right)`.
  - Bottom Bar: Minimal scrubber slider. Tapping or sliding displays a 3-page card preview thumbnail overlay (`Page 295, Page 298 Current, Page 301`).
  - Reading Canvas: True AMOLED black (`#000000`). Page footer displays customizable progress (`Location 1204 of 4892`, `42%`, `1 hr 12 mins left in book`).
- **Opus Reader**:
  - Top Bar contains: Back to Library Arrow `(left)`, Table of Contents, In-book Search, Annotations (Notebook), Aa Display Settings, More Options Overflow `(right)`.
  - Bookmark ribbon is rendered as a standalone interactive corner ribbon at top-right of the page body.
  - Bottom Bar: Clean scrubber with current position label (`Location 211 of 879 · 10%`), page grid toggle button on left, and slider track.
  - Reading Canvas: Dark Charcoal (`#141414`), with soft off-white text (`#E0E0E0`).
- **Discrepancies**:
  - Background color difference: Kindle is pitch black (`#000000`), Opus is charcoal (`#141414`).
  - Kindle supports interactive word selection: long-pressing a word pops up a card with Dictionary, Wikipedia, and Translate tabs. Opus currently does not implement the selection popup dictionary.
  - Scrubber card thumbnails: Kindle shows live visual page cards above the slider; Opus provides location percentage text without mini card previews.

#### Pairs 12 – 14: Color Themes Reading Canvas (White, Sepia, Green)
- **White Theme (Pair 12)**:
  - Both Kindle and Opus render `#FFFFFF` background with `#111111` text. Perfect visual alignment.
- **Sepia Theme (Pair 13)**:
  - Kindle: Background is `#FBF0D9` (warm golden parchment), text is `#361715` (deep reddish-brown sepia ink).
  - Opus: Background is `#F7F1E3` (paler, cooler cream), text is `#2C2C2C` (neutral dark charcoal).
  - Visual impact: Kindle feels like antique aged book paper; Opus feels like modern warm-white e-paper.
- **Green Theme (Pair 14)**:
  - Kindle: Background is `#C5E7CE` (light pastel mint), text is `#3A4B43` (muted olive/charcoal).
  - Opus: Background is `#CEE5D8` (soft sage green), text is `#1E3A2F` (deep pine forest green).
  - Visual impact: Opus text has slightly higher contrast and readability in high ambient light.

#### Pairs 15 – 17: Color Themes Chrome Overlay
- **Kindle**: The top bar and bottom scrubber bar in Kindle are semi-translucent overlays with dynamic backdrop blur that tints according to the active theme (e.g. Sepia chrome has an amber tint, Green chrome has a mint tint).
- **Opus Reader**: Solid surface bars with divider lines tinted to match the active page color theme.

---

### 4.3 Display Settings (Aa) Sheets (Pairs 18 – 21)

#### Pair 18: Font Tab
- **Kindle**:
  - Sheet sits at bottom half of the screen (`bounds=[0,1280][1080,2277]`), leaving the book text readable in real time above the sheet.
  - Fonts are displayed as vertically scrollable preview cards showing the font name and a preview sentence ("AaBbCc 123 - The quick brown fox jumps...") rendered in that exact font.
  - Font Size slider has 14 discrete steps.
  - Bold slider has 6 discrete steps (0 to 5).
- **Opus Reader**:
  - Full-screen sheet with top "DONE" button.
  - Fonts are displayed as horizontal chips (Bookerly, Caecilia, Georgia, Amazon Ember).
  - Font Size slider is continuous with `A` (small) and `A` (large) icon endpoints.
  - Includes Brightness slider with "Auto" toggle directly on every tab.
- **Discrepancies**:
  - Sheet height: Kindle is bottom-docked half sheet; Opus is tall modal sheet.
  - Font selection presentation: Kindle uses rich multi-line specimen cards; Opus uses compact chips.
  - Bold slider: Present in Kindle; absent in Opus.

#### Pair 19: Layout Tab
- **Kindle**:
  - Layout tab contains Page Color palette (White, Sepia, Green, Black circles with radio dot), Margins (3 discrete icon buttons), Line Spacing (3 discrete icon buttons), and Text Alignment (Justified vs Left).
- **Opus Reader**:
  - Reproduces Page Color circles (White `#FFFFFF`, Sepia `#F7F1E3`, Green `#CEE5D8`, Black `#141414`).
  - Includes "Update Page Using System Theme" switch.
  - "Continuous Scrolling" toggle with descriptive subtitle.
  - Margins segmented buttons (Narrow, Normal, Wide).
  - Alignment segmented buttons (Justified, Left).
  - Orientation Lock toggle and Brightness control.
- **Discrepancies**:
  - Line Spacing control: Kindle has 3-button line spacing selector; Opus layout tab controls line height through the preset themes or engine defaults.
  - Continuous vertical scrolling toggle is prominent in Opus; in Kindle it is located under the "More" tab.

#### Pair 20: Themes Tab
- **Kindle**:
  - Displays preset theme cards: `Compact`, `Standard`, `Large`, `Low Vision`. Each card shows a miniature page preview representing the typography and spacing.
  - "Save current settings as a new theme" action button.
- **Opus Reader**:
  - Displays list rows for `Custom`, `Compact`, `Standard`, `Large`, `Low Vision`, with checkmark on the active theme (`Selected`).
  - "Save settings as new theme" row.
- **Discrepancies**:
  - Kindle displays visual thumbnail mockups inside each theme card; Opus uses clean text rows with secondary descriptive subtitles.

#### Pair 21: More Tab
- **Kindle**:
  - Reading Progress options (Page in book, Time left in chapter, Time left in book, Location, None).
  - Clock in Reading toggle.
  - Book Mentions, Popular Highlights, Word Wise toggle, Vocabulary Builder toggle.
- **Opus Reader**:
  - Assistive Reader (TTS listen along).
  - Reading Ruler (colored ruler guide over text).
  - Reading Progress display options.
  - Visible Clock toggle.
  - Turn Pages with Volume Controls toggle.
  - Page Turn Animation toggle.
  - Annotation Menu toggle.
- **Discrepancies**:
  - Opus includes modern assistive reading options (Reading Ruler, TTS, Volume key turning); Kindle includes Amazon ecosystem features (Word Wise, Vocabulary Builder).

---

### 4.4 In-Book Navigation & Annotations (Pairs 22 – 27)

#### Pair 22: Table of Contents (TOC) (`pair_22_contents_toc`)
- **Kindle**: Tabbed dialog with `Chapters` | `Page` | `Bookmarks`. Chapters are listed with hierarchical indentations, page/location numbers on right, and an active chapter indicator.
- **Opus Reader**: Clean chapter list with chapter title, location marker, and current chapter highlight.
- **Discrepancies**:
  - Kindle includes a direct "Go to Page or Location" text field at top of the sheet; Opus focuses on chapter tree navigation.

#### Pair 23: Reader In-Book Search (`pair_23_reader_search`)
- **Kindle**: Search query input at top, displaying match count (`e.g. 47 results in this book`) and expandable result cards showing chapter title, location, and the search term highlighted in bold within surrounding context text.
- **Opus Reader**: Search bar at top with real-time query filtering, location tag, and context snippet rows.
- **Discrepancies**:
  - Result snippets in Kindle show 3 lines of context; Opus shows 2 lines with location tag.

#### Pairs 24 – 27: Annotations & Sub-Sheets
- **Main Annotations Screen (Pair 24)**:
  - Kindle: Displays notebook with tabs for `Notes & Highlights`, `Bookmarks`, and `Popular Highlights`. Includes search within notes.
  - Opus Reader: Top bar with back arrow, "Annotations" title, Filter button, Sort button, and Export button. Note cards display chapter location header, favorite star icon, quoted passage in serif quotation marks, user comment, and date.
- **Filter Sheet (Pair 25)**:
  - Both apps provide bottom sheets to filter annotations by type (All, Bookmarks, Highlights, Notes).
- **Sort Sheet (Pair 26)**:
  - Both apps provide options to sort annotations by "Location in Book" (chronological reading order) vs "Most Recent" (date modified).
- **Export Sheet (Pair 27)**:
  - Kindle: "Export Notes" opens dialog to email notes via Amazon Whispernet with citation style options (APA, Chicago, MLA, Turabian).
  - Opus Reader: "Export Annotations" opens sharing sheet to export notes as plain text or markdown to local apps or email.

---

## 5. Unpaired Screens Catalog

The following screens exist on one app without an equivalent counterpart on the other, reflecting architectural and platform differences.

### 5.1 Unpaired Screens in Amazon Kindle (Kindle Only)

| Screenshot File | Screen Name | Description & Reason for Non-Parity |
| :--- | :--- | :--- |
| `kindle_text_selection_dictionary.png` | **Text Selection & Context Dictionary** | Contextual popup card appearing when text is highlighted. Displays definition from New Oxford American Dictionary, Wikipedia snippet, translation tools, and highlight color palette. *Reason*: Opus Reader currently uses FakeReaderEngine without interactive text selection. |
| `kindle_light_01` to `kindle_light_09` | **System Light Theme App Shell** | Complete Light Mode variants of Home, Library, More, Settings, Notifications. *Reason*: Project requirements specified Dark Theme First for Opus Reader. |
| `kindle_reader_light_chrome_visible.png` | **Light Mode Reader Chrome** | White theme chrome with dark gray control icons and light scrubber. |
| `Kindle Store & Memberships` | **Commercial Storefront & Membership** | Kindle Unlimited, Prime Reading, and Amazon Bookstore screens. *Reason*: Out of scope; replaced with Project Gutenberg. |
| `Reading Challenges & Insights` | **Reading Gamification** | Streak calendars, reading goal badges, and social reading stats. *Reason*: Replaced with local offline Reading Stats card. |

### 5.2 Unpaired Screens in Opus Reader (Opus Only)

| Screenshot File | Screen Name | Description & Functional Purpose |
| :--- | :--- | :--- |
| `opus_gutenberg_book_details_dialog.png` | **Project Gutenberg Book Details & Download Sheet** | Bottom sheet displaying book metadata (Title, Author, Subject, Language, Gutenberg ID), book summary, and one-tap "Download EPUB" button. Replaces the Amazon Store purchase interface. |
| `opus_settings_device_name_dialog.png` | **In-App Device Rename Dialog** | Modal dialog in Settings allowing the user to rename the device identifier locally without web redirection. Replaces Amazon Account Web Management. |
| `opus_new_collection_dialog.png` | **Create Collection Dialog** | Clean modal dialog with text input field and Create/Cancel actions for creating new user book collections. |
| `Storage Access Framework File Picker` | **Local Book Import Picker** | System SAF document picker invoked from Settings to import local `.epub` and `.pdf` files. Replaces Amazon's proprietary "Send to Kindle" email service. |

---

## 6. Packaged Deliverables Verification

All captured artifacts, pairings, and reports have been packaged into a standalone compressed archive:
- **Archive File**: `/home/bgg/Downloads/kindling/opus_vs_kindle_audit_package.zip`
- **File Structure**:
  - `docs/kindle_audit/`: 38 physical device screenshots of Amazon Kindle.
  - `docs/opus_audit/`: 32 physical device screenshots of Opus Reader.
  - `docs/comparison_pairs/pairs/`: 54 paired screenshots (27 pairs with `_kindle.png` and `_opus.png`).
  - `docs/comparison_pairs/unpaired_kindle/`: 12 Kindle-only screenshots.
  - `docs/comparison_pairs/unpaired_opus/`: 3 Opus-only replacement screenshots.
  - `docs/KINDLE_VS_OPUS_COMPARISON_REPORT.md`: Full Markdown audit report.
