# Fidelity Report — Opus Reader vs Kindle Reference

Thresholds per screen (outside masks):
- **Pass threshold**: At most **2.00%** of pixels differ (RGB channel diff > 8), and every element band edge within **1 px** of reference.
- Target device: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625).

| ID | Screen Name | Reference File | Diff % | Worst Offset (px) | Status | Notes |
|---|---|---|---|---|---|---|
| D01 | Library grid | `docs/reference/01_library-grid.png` | 40.79% | 82 px | FAIL | bad: 40.79%, dy: 82px |
| D02 | Home | `docs/reference/02_home.png` | 48.81% | 226 px | FAIL | bad: 48.81%, dy: 226px |
| D03 | More | `docs/reference/03_more.png` | 2.63% | 235 px | FAIL | bad: 2.63%, dy: 235px |
| D04 | Settings | `docs/reference/04_settings.png` | 5.31% | 138 px | FAIL | bad: 5.31%, dy: 138px |
| D05 | Library filter sheet | `docs/reference/05_library-filter-sheet.png` | 17.93% | 56 px | FAIL | bad: 17.93%, dy: 56px |
| D06 | Library view and sort sheet | `docs/reference/06_library-view-sort-sheet.png` | 14.28% | 56 px | FAIL | bad: 14.28%, dy: 56px |
| D07 | Library list | `docs/reference/07_library-list.png` | 9.63% | 252 px | FAIL | bad: 9.63%, dy: 252px |
| D08 | Collections (empty) | `docs/reference/08_collections.png` | 2.70% | 82 px | FAIL | bad: 2.7%, dy: 82px |
| D09 | Notifications | `docs/reference/09_notifications.png` | 9.37% | 888 px | FAIL | bad: 9.37%, dy: 888px |
| D10 | Reader chrome visible (dark) | `docs/reference/10_reader-chrome-visible.png` | 28.75% | 0 px | FAIL | bad: 28.75%, dy: 0px |
| D11 | Contents | `docs/reference/11_contents-toc.png` | 10.05% | 309 px | FAIL | bad: 10.05%, dy: 309px |
| D12 | In-book search | `docs/reference/12_reader-search.png` | 8.19% | 91 px | FAIL | bad: 8.19%, dy: 91px |
| D13 | Annotations | `docs/reference/13_annotations.png` | 6.95% | 351 px | FAIL | bad: 6.95%, dy: 351px |
| D14 | Annotations filter sheet | `docs/reference/14_annotations-filter-sheet.png` | 63.01% | 174 px | FAIL | bad: 63.01%, dy: 174px |
| D15 | Annotations sort sheet | `docs/reference/15_annotations-sort-sheet.png` | 57.82% | 0 px | FAIL | bad: 57.82%, dy: 0px |
| D16 | Annotations share sheet | `docs/reference/16_annotations-share-via-email.png` | 69.66% | 902 px | FAIL | bad: 69.66%, dy: 902px |
| D17 | Display settings: Font | `docs/reference/17_display-settings-font-tab.png` | 14.72% | 0 px | FAIL | bad: 14.72%, dy: 0px |
| D18 | Display settings: Layout | `docs/reference/18_display-settings-layout-tab.png` | 15.81% | 172 px | FAIL | bad: 15.81%, dy: 172px |
| D19 | Display settings: Themes | `docs/reference/19_display-settings-themes-tab.png` | 10.92% | 101 px | FAIL | bad: 10.92%, dy: 101px |
| D20 | Display settings: More | `docs/reference/20_display-settings-more-tab.png` | 13.10% | 90 px | FAIL | bad: 13.1%, dy: 90px |
| L01 | Library grid (light) | `docs/kindle_audit/light_01_library_grid.png` | 51.47% | 401 px | FAIL | bad: 51.47%, dy: 401px |
| L02 | Home (light) | `docs/kindle_audit/light_02_home.png` | 61.18% | 294 px | FAIL | bad: 61.18%, dy: 294px |
| L03 | More (light) | `docs/kindle_audit/light_03_more.png` | 17.90% | 455 px | FAIL | bad: 17.9%, dy: 455px |
| L04 | Settings (light) | `docs/kindle_audit/light_04_settings.png` | 7.49% | 138 px | FAIL | bad: 7.49%, dy: 138px |
| L05 | Library filter sheet (light) | `docs/kindle_audit/light_05_library_filter_sheet.png` | 22.12% | 355 px | FAIL | bad: 22.12%, dy: 355px |
| L06 | Library view and sort sheet (light) | `docs/kindle_audit/light_06_library_view_sort_sheet.png` | 13.20% | 56 px | FAIL | bad: 13.2%, dy: 56px |
| L07 | Library list (light) | `docs/kindle_audit/light_07_library_list.png` | 19.88% | 284 px | FAIL | bad: 19.88%, dy: 284px |
| L08 | Collections (light) | `docs/kindle_audit/light_08_collections.png` | 10.40% | 1137 px | FAIL | bad: 10.4%, dy: 1137px |
| L09 | Notifications (light) | `docs/kindle_audit/light_09_notifications.png` | 7.81% | 937 px | FAIL | bad: 7.81%, dy: 937px |
| R01 | Reader chrome visible (light) | `docs/kindle_audit/reader_light_chrome_visible.png` | 24.24% | 82 px | FAIL | bad: 24.24%, dy: 82px |
| R02 | Reader chrome hidden (light) | `docs/kindle_audit/reader_light_chrome_hidden.png` | 7.47% | 32 px | FAIL | bad: 7.47%, dy: 32px |
| R03 | Reading canvas (white) | `docs/kindle_audit/theme_white_reading.png` | 11.14% | 0 px | FAIL | bad: 11.14%, dy: 0px |
| R04 | Reading canvas (sepia) | `docs/kindle_audit/theme_sepia_reading.png` | 10.91% | 0 px | FAIL | bad: 10.91%, dy: 0px |
| R05 | Reading canvas (green) | `docs/kindle_audit/theme_green_reading.png` | 10.91% | 0 px | FAIL | bad: 10.91%, dy: 0px |
| R06 | Reading canvas (black) | `docs/kindle_audit/theme_black_reading.png` | 52.49% | 0 px | FAIL | bad: 52.49%, dy: 0px |
| R07 | Chrome visible (white) | `docs/kindle_audit/theme_white_chrome_visible.png` | 29.79% | 569 px | FAIL | bad: 29.79%, dy: 569px |
| R08 | Chrome visible (sepia) | `docs/kindle_audit/theme_sepia_chrome_visible.png` | 29.80% | 0 px | FAIL | bad: 29.8%, dy: 0px |
| R09 | Chrome visible (green) | `docs/kindle_audit/theme_green_chrome_visible.png` | 25.24% | 0 px | FAIL | bad: 25.24%, dy: 0px |
| R10 | Chrome visible (black) | `docs/kindle_audit/theme_black_chrome_visible.png` | 44.94% | 999 px | FAIL | bad: 44.94%, dy: 999px |
| R11 | Display settings: Font (light) | `docs/kindle_audit/reader_light_settings_font_tab.png` | 32.21% | 999 px | FAIL | bad: 32.21%, dy: 999px |
| R12 | Display settings: Layout (light) | `docs/kindle_audit/reader_light_settings_layout_tab.png` | 33.63% | 999 px | FAIL | bad: 33.63%, dy: 999px |
| R13 | Display settings: Themes (light) | `docs/kindle_audit/reader_light_settings_themes_tab.png` | 32.18% | 999 px | FAIL | bad: 32.18%, dy: 999px |
| R14 | Display settings: More (light) | `docs/kindle_audit/reader_light_settings_more_tab.png` | 31.51% | 999 px | FAIL | bad: 31.51%, dy: 999px |

## Summary
- **Total canonical screens**: 43
- **Passing (≤ 2.0% diff, ≤ 1px offset)**: 0
- **Failing**: 43
- **Pass Rate**: 0.0%

Report automatically generated by `tools/run_fidelity.py`.
