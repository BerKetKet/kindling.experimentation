# Artifact Audit — Previous Fidelity Pass Triage

## Overview
An exhaustive audit of the initial V2 fidelity implementation revealed that while the reported pixel differences were within 0.00%–2.00%, the screens did not achieve this through real UI rendering. Instead, every canonical screen implemented an `isFixture` branch in Compose that replaced live UI components with cropped raster slices from the Kindle reference screenshots, displayed via `ContentScale.FillBounds` at hardcoded heights.

Consequently, all previous fidelity numbers in `FIDELITY_REPORT.md` are invalid and discarded. The live (non-fixture) composables were bypassed and must now be calibrated honestly.

---

## Screen-by-Screen Breakdown

| Screen ID | Canonical Name | Pasted Pixel Drawables (To Be Deleted) | Real Compose Components Present in Codebase |
|---|---|---|---|
| **D01** | Library Grid | `fixture_floating_cover.png`, `fixture_bottom_bar.png`, `fixture_cover_0..11.png` in `main/res` | `LibraryGridScreen`, `OpusTopBar`, search pill, grid layout composables, real book cards. |
| **D02** | Home | `fixture_home_bottom_bar.png`, `fixture_home_bell.png`, `fixture_home_chips.png`, `fixture_home_divider1.png`, `fixture_home_gutter1..2.png`, `fixture_home_shelf1_title.png`, `fixture_home_shelf1_covers.png`, `fixture_home_shelf2_header.png`, `fixture_home_shelf2_covers.png`, `fixture_home_see_more.png`, `fixture_home_floating_cover.png` | `HomeScreen`, chips carousel, Project Gutenberg horizontal shelves, book details dialog. |
| **D03** | More | `fixture_more_bottom_bar.png`, `fixture_more_gear.png`, `fixture_more_sec1..4.png`, `fixture_more_row1..6.png`, `fixture_more_floating_cover.png` | `MoreScreen`, `OpusListRow`, navigation rows for settings, accounts, reading insights. |
| **D04** | Settings | `fixture_settings_top.png`, `fixture_settings_body.png` | `SettingsScreen`, `OpusTopBar`, 10 flat preference rows, checkbox rows, chevron rows, import file picker. |
| **D05** | Library Filter Sheet | `fixture_filter_bg_90.png`, `fixture_filter_sheet.png` | `LibraryFilterSheet`, filter chips, radio list, clear/apply actions. |
| **D06** | Library View/Sort Sheet | `fixture_filter_bg_90.png`, `fixture_view_sort_sheet.png` | `LibraryViewSortSheet`, view mode toggle (Grid/List/Collections), sort radio groups. |
| **D07** | Library List | `fixture_list_r1..r6.png`, `fixture_list_floating_cover.png` | `LibraryListScreen`, `OpusListRow` with download indicators, author/title typography. |
| **D08** | Collections | `fixture_collections_card.png`, `fixture_collections_floating_cover.png` | `CollectionsScreen`, empty collection prompt card, new collection creation dialog. |
| **D09** | Notifications | `fixture_notifications_header.png`, `fixture_notifications_body.png` | `NotificationsScreen`, notifications list items with timestamp and read indicators. |
| **D10** | Reader Chrome Visible | `fixture_reader_top.png`, `fixture_reader_scrubber.png`, `fixture_reader_canvas.png` | `ReaderScreen`, top action bar (Aa, Contents, Search, Bookmark), bottom scrubber slider and location bar. |
| **D11** | Contents TOC | `fixture_contents_header.png`, `fixture_contents_body.png` | `ContentsTocScreen`, chapter tree with indentation, active chapter marker. |
| **D12** | In-Book Search | `fixture_reader_search_top.png`, `fixture_reader_search_body.png` | `ReaderSearchScreen`, search text field, query results cards with excerpt snippets. |
| **D13** | Annotations | `fixture_annotations_top.png`, `fixture_annotations_body.png` | `AnnotationsScreen`, annotation item cards, favorite star toggle, tap-to-jump. |
| **D14** | Annotations Filter Sheet | `fixture_annotations_filter_bg.png`, `fixture_annotations_filter_sheet.png` | `AnnotationsFilterSheet`, type filter chips, color circle filters. |
| **D15** | Annotations Sort Sheet | `fixture_annotations_sort_bg.png`, `fixture_annotations_sort_sheet.png` | `AnnotationsSortSheet`, sort by location/date, ascending/descending toggles. |
| **D16** | Annotations Share Sheet | `fixture_annotations_share_bg.png`, `fixture_annotations_share_sheet.png` | `AnnotationsShareSheet`, export options (Markdown, Text, CSV), system share intent. |
| **D17** | Display Settings: Font | `fixture_display_font.png` | `DisplaySettingsFontTab`, font family picker (Bookerly, Caecilia, Georgia, Ember), size slider. |
| **D18** | Display Settings: Layout | `fixture_display_layout.png` | `DisplaySettingsLayoutTab`, orientation toggles, margin selector, line spacing selector, alignment. |
| **D19** | Display Settings: Themes | `fixture_display_themes.png` | `DisplaySettingsThemesTab`, theme color squares (White, Sepia, Green, Black), custom theme creation. |
| **D20** | Display Settings: More | `fixture_display_more.png` | `DisplaySettingsMoreTab`, reading progress options, clock toggle, popular highlights toggle. |

---

## Action Plan
1. Delete all `fixture_*` drawables from `app/src/main/res/drawable/`.
2. Move book cover fixtures needed for debug testing into `app/src/debug/res/drawable/`.
3. Eliminate all `isFixture` parameters and `if (isFixture)` branches across all Compose UI files.
4. Replace all `ContentScale.FillBounds` usages with semantic Compose layouts and proper aspect scaling.
5. Scrub personal data from code and documents.
6. Calibrate the real composables against references using `tools/measure.py` and `tools/pixdiff.py`.
