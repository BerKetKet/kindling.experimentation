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

print("=== Capturing Remaining Opus Screens ===")

# Currently in Reader chrome
# 1. Annotations (center: 754, 157)
print("\n--- Annotations & Sub-sheets ---")
tap(754, 157, 1.5)
capture("22_annotations.png")

# Filter sheet
filt_pt = dump_and_find(desc_match="Filter Annotations") or (780, 260)
tap(filt_pt[0], filt_pt[1], 1.5)
capture("23_annotations_filter_sheet.png")
close_pt = dump_and_find(desc_match="Close Filter") or dump_and_find(desc_match="Close") or (987, 850)
tap(close_pt[0], close_pt[1], 1.0)

# Sort sheet
sort_pt = dump_and_find(desc_match="Sort Annotations") or (997, 158)
tap(sort_pt[0], sort_pt[1], 1.5)
capture("24_annotations_sort_sheet.png")
tap(540, 500, 1.0) # Dismiss sheet

# Export sheet
share_pt = dump_and_find(desc_match="Export Annotations") or (880, 158)
tap(share_pt[0], share_pt[1], 1.5)
capture("25_annotations_share_sheet.png")
tap(540, 500, 1.0) # Dismiss sheet

# Back to Reader
back_pt = dump_and_find(desc_match="Back to Reader") or (74, 157)
tap(back_pt[0], back_pt[1], 1.5)

# Ensure chrome is visible
adb("shell uiautomator dump /sdcard/dump.xml")
adb("pull /sdcard/dump.xml /tmp/dump_chk.xml")
tree = ET.parse("/tmp/dump_chk.xml")
texts = [n.attrib.get("text") or n.attrib.get("content-desc") or "" for n in tree.iter()]
if not any("Table of Contents" in t for t in texts):
    tap(540, 1170, 1.0)

# 2. Display Settings (Aa at 880, 158)
print("\n--- Display Settings Tabs ---")
tap(880, 158, 1.5)
capture("16_settings_font_tab.png")

# Layout tab (tap 370, 1340)
tap(370, 1340, 1.5)
capture("17_settings_layout_tab.png")

# Themes tab (tap 640, 1340)
tap(640, 1340, 1.5)
capture("18_settings_themes_tab.png")

# More tab (tap 910, 1340)
tap(910, 1340, 1.5)
capture("19_settings_more_tab.png")

# 3. Reading Themes
print("\n--- Reading Themes in Opus Reader ---")
# Back to Layout tab
tap(370, 1340, 1.5)

themes = {
    "white": 105,
    "sepia": 269,
    "green": 433,
    "black": 597
}

for cname, cx in themes.items():
    print(f"Switching to theme: {cname.upper()}")
    tap(cx, 1575, 1.0)
    # Dismiss sheet
    tap(540, 500, 1.0)
    capture(f"theme_{cname}_chrome_visible.png")
    # Hide chrome
    tap(540, 1170, 1.0)
    capture(f"theme_{cname}_reading.png")
    # Show chrome
    tap(540, 1170, 1.0)
    # Reopen Aa
    tap(880, 158, 1.0)
    # Reopen Layout tab
    tap(370, 1340, 1.0)

# Dismiss sheet and return to default black theme
tap(597, 1575, 1.0)
tap(540, 500, 1.0)

print("\n=== Remaining Opus Screens Captured Successfully! ===")
