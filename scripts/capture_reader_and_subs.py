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
    print(f"Captured: {filename} ({os.path.getsize(path)} bytes)")

def dump_ui():
    adb("shell uiautomator dump /sdcard/dump.xml")
    adb("pull /sdcard/dump.xml /tmp/dump.xml")
    tree = ET.parse("/tmp/dump.xml")
    elements = []
    for n in tree.iter():
        t = n.attrib.get("text", "").strip()
        d = n.attrib.get("content-desc", "").strip()
        b = n.attrib.get("bounds", "")
        if (t or d) and b:
            parts = b.replace("][", ",").replace("[", "").replace("]", "").split(",")
            if len(parts) == 4:
                x1, y1, x2, y2 = map(int, parts)
                cx, cy = (x1 + x2) // 2, (y1 + y2) // 2
                elements.append({"text": t, "desc": d, "center": (cx, cy), "bounds": (x1, y1, x2, y2)})
    return elements

def find_exact(elements, text=None, desc=None, min_y=0, max_y=2400):
    for e in elements:
        if not (min_y <= e["center"][1] <= max_y):
            continue
        if text and text.lower() == e["text"].lower():
            return e
        if desc and desc.lower() == e["desc"].lower():
            return e
    return None

def find_contains(elements, text=None, desc=None, min_y=0, max_y=2400):
    for e in elements:
        if not (min_y <= e["center"][1] <= max_y):
            continue
        if text and text.lower() in e["text"].lower():
            return e
        if desc and desc.lower() in e["desc"].lower():
            return e
    return None

print("=== Starting Targeted Reader & Collections Capture ===")

# Force stop and start app cleanly to guarantee starting from Home
adb("shell am force-stop app.opusreader")
time.sleep(0.5)
adb("shell am start -n app.opusreader/.MainActivity")
time.sleep(2.0)

# Switch to Library tab (bottom nav: 540, 2250)
print("Navigating to Library tab...")
tap(540, 2250, 1.5)

elems = dump_ui()
# Verify we are on Library
vs_btn = find_contains(elems, desc="View and sort", max_y=400)
print(f"View & Sort button found: {vs_btn}")

# --- Collections Screen & Dialog ---
print("\n--- Capturing Collections ---")
if vs_btn:
    tap(vs_btn["center"][0], vs_btn["center"][1], 1.2)
else:
    tap(1006, 260, 1.2)

elems = dump_ui()
col_row = find_exact(elems, text="Collections")
if col_row:
    tap(col_row["center"][0], col_row["center"][1], 0.8)
done_btn = find_exact(elems, text="DONE")
if done_btn:
    tap(done_btn["center"][0], done_btn["center"][1], 1.5)

# On Collections Screen!
elems = dump_ui()
capture("08_collections.png")

# Open New Collection Dialog
add_col = find_contains(elems, desc="New Collection")
if add_col:
    tap(add_col["center"][0], add_col["center"][1], 1.2)
    capture("opus_new_collection_dialog.png")
    elems = dump_ui()
    cancel_btn = find_exact(elems, text="CANCEL")
    if cancel_btn:
        tap(cancel_btn["center"][0], cancel_btn["center"][1], 1.0)
    else:
        tap(680, 1300, 1.0)

# Go back to Library
elems = dump_ui()
back_btn = find_contains(elems, desc="Navigate back") or find_contains(elems, desc="Back")
if back_btn:
    tap(back_btn["center"][0], back_btn["center"][1], 1.2)

# Reset Library to Grid View
tap(1006, 260, 1.2)
elems = dump_ui()
grid_row = find_exact(elems, text="Grid")
if grid_row:
    tap(grid_row["center"][0], grid_row["center"][1], 0.8)
done_btn = find_exact(elems, text="DONE")
if done_btn:
    tap(done_btn["center"][0], done_btn["center"][1], 1.5)

# --- Enter Reader Screen ---
print("\n--- Entering Reader ---")
# Tap first book cover in Library (270, 600)
tap(270, 600, 2.0)

# Ensure Reader chrome is visible
elems = dump_ui()
if not find_contains(elems, desc="Table of contents"):
    tap(540, 1170, 1.2)
    elems = dump_ui()

capture("10_reader_chrome_visible.png")

# Hide chrome for reading screenshot
tap(540, 1170, 1.0)
capture("11_reader_reading.png")
# Bring chrome back
tap(540, 1170, 1.2)

# --- Annotations Screen & Sub-sheets ---
print("\n--- Capturing Annotations ---")
elems = dump_ui()
ann_btn = find_contains(elems, desc="Annotations")
if ann_btn:
    tap(ann_btn["center"][0], ann_btn["center"][1], 1.5)
else:
    tap(754, 157, 1.5)

elems = dump_ui()
capture("22_annotations.png")

# Filter annotations
filt_btn = find_contains(elems, desc="Filter annotations")
if filt_btn:
    tap(filt_btn["center"][0], filt_btn["center"][1], 1.5)
    capture("23_annotations_filter_sheet.png")
    # Dismiss filter sheet
    adb("shell input keyevent KEYCODE_BACK")
    time.sleep(1.0)

# Sort annotations
elems = dump_ui()
sort_btn = find_contains(elems, desc="Sort annotations")
if sort_btn:
    tap(sort_btn["center"][0], sort_btn["center"][1], 1.5)
    capture("24_annotations_sort_sheet.png")
    # Dismiss sort sheet
    adb("shell input keyevent KEYCODE_BACK")
    time.sleep(1.0)

# Export annotations
elems = dump_ui()
exp_btn = find_contains(elems, desc="Export annotations")
if exp_btn:
    tap(exp_btn["center"][0], exp_btn["center"][1], 1.5)
    capture("25_annotations_share_sheet.png")
    # Dismiss export sheet
    elems = dump_ui()
    cancel_btn = find_exact(elems, text="CANCEL")
    if cancel_btn:
        tap(cancel_btn["center"][0], cancel_btn["center"][1], 1.0)
    else:
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)

# Back to Reader
elems = dump_ui()
back_r = find_contains(elems, desc="Back to Reader")
if back_r:
    tap(back_r["center"][0], back_r["center"][1], 1.5)
else:
    adb("shell input keyevent KEYCODE_BACK")
    time.sleep(1.5)

# Ensure Reader chrome is visible
elems = dump_ui()
if not find_contains(elems, desc="Display settings"):
    tap(540, 1170, 1.2)
    elems = dump_ui()

# --- Display Settings (Aa) Tabs ---
print("\n--- Capturing Display Settings Tabs ---")
aa_btn = find_contains(elems, desc="Display settings")
if aa_btn:
    tap(aa_btn["center"][0], aa_btn["center"][1], 1.5)
else:
    tap(880, 157, 1.5)

# Verify Font tab is active
elems = dump_ui()
capture("16_settings_font_tab.png")

# Layout tab: find text 'Layout' around y=330
layout_tab = find_exact(elems, text="Layout", min_y=250, max_y=400)
if layout_tab:
    tap(layout_tab["center"][0], layout_tab["center"][1], 1.2)
else:
    tap(391, 322, 1.2)
elems = dump_ui()
capture("17_settings_layout_tab.png")

# Themes tab: find text 'Themes' around y=330
themes_tab = find_exact(elems, text="Themes", min_y=250, max_y=400)
if themes_tab:
    tap(themes_tab["center"][0], themes_tab["center"][1], 1.2)
else:
    tap(665, 322, 1.2)
elems = dump_ui()
capture("18_settings_themes_tab.png")

# More tab: find text 'More' around y=330
more_tab = find_exact(elems, text="More", min_y=250, max_y=400)
if more_tab:
    tap(more_tab["center"][0], more_tab["center"][1], 1.2)
else:
    tap(922, 322, 1.2)
elems = dump_ui()
capture("19_settings_more_tab.png")

# --- Reading Themes (White, Sepia, Green, Black) ---
print("\n--- Capturing Reading Themes ---")
# Tap Layout tab
tap(391, 322, 1.2)

theme_coords = {
    "white": (100, 575),
    "sepia": (269, 575),
    "green": (438, 575),
    "black": (607, 575)
}

for name, (cx, cy) in theme_coords.items():
    print(f"Capturing Theme: {name.upper()}")
    tap(cx, cy, 0.8)
    # Tap DONE
    tap(955, 167, 1.2)
    capture(f"theme_{name}_chrome_visible.png")
    # Hide chrome
    tap(540, 1170, 1.0)
    capture(f"theme_{name}_reading.png")
    # Show chrome
    tap(540, 1170, 1.0)
    # Open Aa
    tap(880, 157, 1.0)
    # Open Layout tab
    tap(391, 322, 1.0)

# Reset to black theme
tap(607, 575, 0.8)
tap(955, 167, 1.0)

print("\n=== All Steps Completed Successfully! ===")
