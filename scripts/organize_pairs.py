import os
import shutil

PAIRS_DIR = "docs/comparison_pairs"
os.makedirs(f"{PAIRS_DIR}/pairs", exist_ok=True)
os.makedirs(f"{PAIRS_DIR}/unpaired_kindle", exist_ok=True)
os.makedirs(f"{PAIRS_DIR}/unpaired_opus", exist_ok=True)

pairs = [
    ("pair_01_home", "docs/kindle_audit/dark_02_home.png", "docs/opus_audit/02_home.png"),
    ("pair_02_library_grid", "docs/kindle_audit/dark_01_library_grid.png", "docs/opus_audit/01_library_grid.png"),
    ("pair_03_library_filter_sheet", "docs/kindle_audit/dark_05_library_filter_sheet.png", "docs/opus_audit/05_library_filter_sheet.png"),
    ("pair_04_library_view_sort_sheet", "docs/kindle_audit/dark_06_library_view_sort_sheet.png", "docs/opus_audit/06_library_view_sort_sheet.png"),
    ("pair_05_library_list", "docs/kindle_audit/dark_07_library_list.png", "docs/opus_audit/07_library_list.png"),
    ("pair_06_collections", "docs/kindle_audit/dark_08_collections.png", "docs/opus_audit/08_collections.png"),
    ("pair_07_more", "docs/kindle_audit/dark_03_more.png", "docs/opus_audit/03_more.png"),
    ("pair_08_settings", "docs/kindle_audit/dark_04_settings.png", "docs/opus_audit/04_settings.png"),
    ("pair_09_notifications", "docs/kindle_audit/dark_09_notifications.png", "docs/opus_audit/09_notifications.png"),
    ("pair_10_reader_chrome_black", "docs/kindle_audit/theme_black_chrome_visible.png", "docs/opus_audit/10_reader_chrome_visible.png"),
    ("pair_11_reader_reading_black", "docs/kindle_audit/theme_black_reading.png", "docs/opus_audit/11_reader_reading.png"),
    ("pair_12_reader_reading_white", "docs/kindle_audit/theme_white_reading.png", "docs/opus_audit/theme_white_reading.png"),
    ("pair_13_reader_reading_sepia", "docs/kindle_audit/theme_sepia_reading.png", "docs/opus_audit/theme_sepia_reading.png"),
    ("pair_14_reader_reading_green", "docs/kindle_audit/theme_green_reading.png", "docs/opus_audit/theme_green_reading.png"),
    ("pair_15_reader_chrome_white", "docs/kindle_audit/theme_white_chrome_visible.png", "docs/opus_audit/theme_white_chrome_visible.png"),
    ("pair_16_reader_chrome_sepia", "docs/kindle_audit/theme_sepia_chrome_visible.png", "docs/opus_audit/theme_sepia_chrome_visible.png"),
    ("pair_17_reader_chrome_green", "docs/kindle_audit/theme_green_chrome_visible.png", "docs/opus_audit/theme_green_chrome_visible.png"),
    ("pair_18_display_settings_font_tab", "docs/reference/17_display-settings-font-tab.png", "docs/opus_audit/16_settings_font_tab.png"),
    ("pair_19_display_settings_layout_tab", "docs/reference/18_display-settings-layout-tab.png", "docs/opus_audit/17_settings_layout_tab.png"),
    ("pair_20_display_settings_themes_tab", "docs/reference/19_display-settings-themes-tab.png", "docs/opus_audit/18_settings_themes_tab.png"),
    ("pair_21_display_settings_more_tab", "docs/reference/20_display-settings-more-tab.png", "docs/opus_audit/19_settings_more_tab.png"),
    ("pair_22_contents_toc", "docs/kindle_audit/reader_toc.png", "docs/opus_audit/20_contents_toc.png"),
    ("pair_23_reader_search", "docs/kindle_audit/reader_search.png", "docs/opus_audit/21_reader_search.png"),
    ("pair_24_annotations_main", "docs/kindle_audit/reader_annotations.png", "docs/opus_audit/22_annotations.png"),
    ("pair_25_annotations_filter_sheet", "docs/reference/14_annotations-filter-sheet.png", "docs/opus_audit/23_annotations_filter_sheet.png"),
    ("pair_26_annotations_sort_sheet", "docs/reference/15_annotations-sort-sheet.png", "docs/opus_audit/24_annotations_sort_sheet.png"),
    ("pair_27_annotations_export_sheet", "docs/reference/16_annotations-share-via-email.png", "docs/opus_audit/25_annotations_share_sheet.png"),
]

for name, k_src, o_src in pairs:
    k_dst = f"{PAIRS_DIR}/pairs/{name}_kindle.png"
    o_dst = f"{PAIRS_DIR}/pairs/{name}_opus.png"
    if os.path.exists(k_src):
        shutil.copy2(k_src, k_dst)
    if os.path.exists(o_src):
        shutil.copy2(o_src, o_dst)

# Unpaired Kindle
unpaired_k = [
    ("docs/kindle_audit/reader_text_selection_menu.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_text_selection_dictionary.png"),
    ("docs/kindle_audit/light_01_library_grid.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_01_library_grid.png"),
    ("docs/kindle_audit/light_02_home.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_02_home.png"),
    ("docs/kindle_audit/light_03_more.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_03_more.png"),
    ("docs/kindle_audit/light_04_settings.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_04_settings.png"),
    ("docs/kindle_audit/light_05_library_filter_sheet.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_05_library_filter_sheet.png"),
    ("docs/kindle_audit/light_06_library_view_sort_sheet.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_06_library_view_sort_sheet.png"),
    ("docs/kindle_audit/light_07_library_list.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_07_library_list.png"),
    ("docs/kindle_audit/light_08_collections.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_08_collections.png"),
    ("docs/kindle_audit/light_09_notifications.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_light_09_notifications.png"),
    ("docs/kindle_audit/reader_light_chrome_visible.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_reader_light_chrome_visible.png"),
    ("docs/kindle_audit/reader_light_chrome_hidden.png", f"{PAIRS_DIR}/unpaired_kindle/kindle_reader_light_chrome_hidden.png"),
]

for src, dst in unpaired_k:
    if os.path.exists(src):
        shutil.copy2(src, dst)

# Unpaired Opus
unpaired_o = [
    ("docs/opus_audit/opus_gutenberg_dialog.png", f"{PAIRS_DIR}/unpaired_opus/opus_gutenberg_book_details_dialog.png"),
    ("docs/opus_audit/opus_device_name_dialog.png", f"{PAIRS_DIR}/unpaired_opus/opus_settings_device_name_dialog.png"),
    ("docs/opus_audit/opus_new_collection_dialog.png", f"{PAIRS_DIR}/unpaired_opus/opus_new_collection_dialog.png"),
]

for src, dst in unpaired_o:
    if os.path.exists(src):
        shutil.copy2(src, dst)

print("Comparison pairs organized successfully!")
