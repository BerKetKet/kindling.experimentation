import subprocess
import time
import os
import xml.etree.ElementTree as ET

SERIAL = "32001776e8e82623"
OUT_DIR = "docs/opus_audit"
os.makedirs(OUT_DIR, exist_ok=True)

def adb(cmd):
    return subprocess.run(f"adb -s {SERIAL} {cmd}", shell=True, capture_output=True, text=True)

def tap(x, y, delay=1.2):
    adb(f"shell input tap {x} {y}")
    time.sleep(delay)

def capture(filename):
    path = os.path.join(OUT_DIR, filename)
    adb(f"shell screencap -p /sdcard/{filename}")
    adb(f"pull /sdcard/{filename} {path}")
    adb(f"shell rm /sdcard/{filename}")
    print(f"Captured: {filename}")

def dump_and_find(text_match=None, desc_match=None):
    adb("shell uiautomator dump /sdcard/dump.xml")
    adb("pull /sdcard/dump.xml /tmp/dump_find.xml")
    if not os.path.exists("/tmp/dump_find.xml"):
        return None
    tree = ET.parse("/tmp/dump_find.xml")
    for node in tree.iter():
        t = node.attrib.get("text", "")
        d = node.attrib.get("content-desc", "")
        b = node.attrib.get("bounds", "")
        if (text_match and text_match.lower() in t.lower()) or (desc_match and desc_match.lower() in d.lower()):
            parts = b.replace("][", ",").replace("[", "").replace("]", "").split(",")
            if len(parts) == 4:
                x1, y1, x2, y2 = map(int, parts)
                return ((x1 + x2) // 2, (y1 + y2) // 2)
    return None

def find_all_text_desc():
    adb("shell uiautomator dump /sdcard/dump.xml")
    adb("pull /sdcard/dump.xml /tmp/dump_find.xml")
    tree = ET.parse("/tmp/dump_find.xml")
    res = []
    for node in tree.iter():
        t = node.attrib.get("text", "")
        d = node.attrib.get("content-desc", "")
        b = node.attrib.get("bounds", "")
        if t or d:
            res.append((t, d, b))
    return res

print("=== Starting Opus Targeted Captures ===")

# First bring app to front or restart cleanly
adb("shell am start -n app.opusreader/.MainActivity")
time.sleep(1.5)

# Dismiss any open dialog / sheet by pressing back or tapping top
for _ in range(3):
    adb("shell input keyevent KEYCODE_BACK")
    time.sleep(0.5)

# If we exited the app, bring it back
adb("shell am start -n app.opusreader/.MainActivity")
time.sleep(1.5)

# Check where we are
print("Navigating to Library...")
# Tap Library bottom tab (approx 540, 2250)
tap(540, 2250, 1.2)

# Verify we are on Library screen
capture("01_library_grid.png")

# --- 1. Collections Screen & New Collection Dialog ---
print("\n--- Capturing Collections ---")
# Open View & Sort Sheet (icon button at 1006, 260)
view_btn = dump_and_find(desc_match="View and sort options") or (1006, 260)
tap(view_btn[0], view_btn[1], 1.2)
capture("06_library_view_sort_sheet.png")

# Tap 'Collections' row in View sheet
col_row = dump_and_find(text_match="Collections") or (540, 800)
tap(col_row[0], col_row[1], 0.8)

# Tap 'DONE' (955, 167)
done_btn = dump_and_find(text_match="DONE") or (955, 167)
tap(done_btn[0], done_btn[1], 1.5)

# We should be on Collections screen!
capture("08_collections.png")

# Tap 'New Collection' (+) button
add_col_btn = dump_and_find(desc_match="New Collection") or (920, 157)
tap(add_col_btn[0], add_col_btn[1], 1.2)
capture("opus_new_collection_dialog.png")

# Tap CANCEL
cancel_btn = dump_and_find(text_match="CANCEL") or (680, 1300)
tap(cancel_btn[0], cancel_btn[1], 1.0)

# Back to Library
back_btn = dump_and_find(desc_match="Back") or (74, 157)
tap(back_btn[0], back_btn[1], 1.2)

# Switch back to Grid view via View & Sort
tap(1006, 260, 1.2)
grid_row = dump_and_find(text_match="Grid") or (540, 600)
tap(grid_row[0], grid_row[1], 0.8)
tap(955, 167, 1.2)

# --- 2. Enter Reader Screen ---
print("\n--- Entering Reader ---")
# Tap first book cover in Library
tap(270, 600, 1.5)

# Ensure Reader chrome is visible
adb("shell uiautomator dump /sdcard/dump.xml")
adb("pull /sdcard/dump.xml /tmp/dump_chk.xml")
tree = ET.parse("/tmp/dump_chk.xml")
texts = [n.attrib.get("text") or n.attrib.get("content-desc") or "" for n in tree.iter()]
if not any("Table of Contents" in t for t in texts):
    print("Showing Reader chrome...")
    tap(540, 1170, 1.2)

capture("10_reader_chrome_visible.png")

# Hide chrome for reading screenshot
tap(540, 1170, 1.0)
capture("11_reader_reading.png")
# Bring chrome back
tap(540, 1170, 1.0)

# --- 3. Annotations & Sub-Sheets ---
print("\n--- Capturing Annotations & Sub-sheets ---")
# Annotations button is at (744, 157) or desc="Annotations"
tap(744, 157, 1.5)
capture("22_annotations.png")

# Filter annotations button: (744, 157)
tap(744, 157, 1.2)
capture("23_annotations_filter_sheet.png")
# Dismiss filter sheet: tap Close Filter (987, 850) or back
close_filter = dump_and_find(desc_match="Close") or (987, 850)
tap(close_filter[0], close_filter[1], 1.0)

# Sort annotations button: (870, 157)
tap(870, 157, 1.2)
capture("24_annotations_sort_sheet.png")
# Dismiss sort sheet: tap outside or DONE
done_sort = dump_and_find(text_match="DONE") or (955, 167)
tap(done_sort[0], done_sort[1], 1.0)

# Export annotations button: (996, 157)
tap(996, 157, 1.2)
capture("25_annotations_share_sheet.png")
# Dismiss share sheet: tap CANCEL or close
cancel_share = dump_and_find(text_match="CANCEL") or (540, 2100)
tap(cancel_share[0], cancel_share[1], 1.0)

# Back to Reader
back_reader = dump_and_find(desc_match="Back to Reader") or (84, 157)
tap(back_reader[0], back_reader[1], 1.2)

# Ensure Reader chrome is visible
tap(540, 1170, 0.8) # in case chrome was hidden

# --- 4. Display Settings Tabs ---
print("\n--- Capturing Display Settings Tabs ---")
# Aa button is at (880, 157)
tap(880, 157, 1.2)

# Tab 1: Font (152, 322)
tap(152, 322, 1.0)
capture("16_settings_font_tab.png")

# Tab 2: Layout (391, 322)
tap(391, 322, 1.0)
capture("17_settings_layout_tab.png")

# Tab 3: Themes (665, 322)
tap(665, 322, 1.0)
capture("18_settings_themes_tab.png")

# Tab 4: More (922, 322)
tap(922, 322, 1.0)
capture("19_settings_more_tab.png")

# --- 5. Reading Themes ---
print("\n--- Capturing Reading Themes ---")
# Go back to Layout tab (391, 322)
tap(391, 322, 1.0)

theme_coords = {
    "white": (100, 575),
    "sepia": (269, 575),
    "green": (438, 575),
    "black": (607, 575)
}

for name, (cx, cy) in theme_coords.items():
    print(f"Switching to theme: {name.upper()}")
    tap(cx, cy, 1.0)
    # Tap DONE (955, 167)
    tap(955, 167, 1.2)
    capture(f"theme_{name}_chrome_visible.png")
    # Tap middle to hide chrome
    tap(540, 1170, 1.0)
    capture(f"theme_{name}_reading.png")
    # Tap middle to show chrome
    tap(540, 1170, 1.0)
    # Re-open Aa (880, 157)
    tap(880, 157, 1.0)
    # Re-open Layout tab (391, 322)
    tap(391, 322, 1.0)

# Restore default black theme
tap(607, 575, 1.0)
tap(955, 167, 1.0)

print("\n=== Opus Captures Finished! ===")
