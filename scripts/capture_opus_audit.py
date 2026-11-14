import subprocess
import time
import os
import xml.etree.ElementTree as ET

SERIAL = "32001776e8e82623"
OUT_DIR = "docs/opus_audit"
os.makedirs(OUT_DIR, exist_ok=True)

def adb(cmd):
    return subprocess.run(f"adb -s {SERIAL} {cmd}", shell=True, capture_output=True, text=True)

def tap(x, y, delay=1.0):
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
            # Parse bounds [x1,y1][x2,y2]
            parts = b.replace("][", ",").replace("[", "").replace("]", "").split(",")
            if len(parts) == 4:
                x1, y1, x2, y2 = map(int, parts)
                return ((x1 + x2) // 2, (y1 + y2) // 2)
    return None

print("=== Starting Opus Reader Audit Capture ===")

# Ensure Opus Reader is running
adb("shell am start -n app.opusreader/.MainActivity")
time.sleep(2.0)

# 1. Home Screen
print("\n--- 02 Home Screen ---")
tap(355, 2256, 1.5) # Home tab
capture("02_home.png")

# Gutenberg details dialog
# Find "Frankenstein" or first Gutenberg item on Home
pt = dump_and_find(text_match="Frankenstein")
if pt:
    tap(pt[0], pt[1], 1.5)
    capture("opus_gutenberg_dialog.png")
    # Dismiss dialog
    close_pt = dump_and_find(text_match="CLOSE")
    if close_pt:
        tap(close_pt[0], close_pt[1], 1.0)
    else:
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)

# Notifications
print("\n--- 09 Notifications ---")
notif_pt = dump_and_find(desc_match="Notifications")
if notif_pt:
    tap(notif_pt[0], notif_pt[1], 1.5)
    capture("09_notifications.png")
    adb("shell input keyevent KEYCODE_BACK")
    time.sleep(1.0)

# 2. Library
print("\n--- 01 Library Grid ---")
tap(645, 2256, 1.5) # Library tab
capture("01_library_grid.png")

# Filter Sheet
filt_pt = dump_and_find(desc_match="Filter Library")
if filt_pt:
    tap(filt_pt[0], filt_pt[1], 1.5)
    capture("05_library_filter_sheet.png")
    done_pt = dump_and_find(text_match="DONE")
    if done_pt:
        tap(done_pt[0], done_pt[1], 1.0)
    else:
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)

# View & Sort Sheet
vs_pt = dump_and_find(desc_match="View and Sort")
if vs_pt:
    tap(vs_pt[0], vs_pt[1], 1.5)
    capture("06_library_view_sort_sheet.png")
    
    # Switch to List
    list_pt = dump_and_find(text_match="List")
    done_pt = dump_and_find(text_match="DONE")
    if list_pt and done_pt:
        tap(list_pt[0], list_pt[1], 0.5)
        tap(done_pt[0], done_pt[1], 1.5)
        capture("07_library_list.png")
    
    # Switch to Collections
    vs_pt = dump_and_find(desc_match="View and Sort")
    if vs_pt:
        tap(vs_pt[0], vs_pt[1], 1.5)
        col_pt = dump_and_find(text_match="Collections")
        done_pt = dump_and_find(text_match="DONE")
        if col_pt and done_pt:
            tap(col_pt[0], col_pt[1], 0.5)
            tap(done_pt[0], done_pt[1], 1.5)
            capture("08_collections.png")
            
            # New Collection Dialog
            new_col_pt = dump_and_find(text_match="CREATE A NEW COLLECTION") or dump_and_find(desc_match="New Collection")
            if new_col_pt:
                tap(new_col_pt[0], new_col_pt[1], 1.5)
                capture("opus_new_collection_dialog.png")
                cancel_pt = dump_and_find(text_match="CANCEL")
                if cancel_pt:
                    tap(cancel_pt[0], cancel_pt[1], 1.0)
                else:
                    adb("shell input keyevent KEYCODE_BACK")
                    time.sleep(1.0)
    
    # Switch back to Grid
    # In Collections screen, tap back button to return to Library Grid
    adb("shell input keyevent KEYCODE_BACK")
    time.sleep(1.0)

# 3. More & Settings
print("\n--- 03 More & 04 Settings ---")
tap(935, 2256, 1.5) # More tab
capture("03_more.png")

# Settings
gear_pt = dump_and_find(desc_match="Settings")
if gear_pt:
    tap(gear_pt[0], gear_pt[1], 1.5)
    capture("04_settings.png")
    
    # Device Name Dialog
    dev_pt = dump_and_find(text_match="Device name")
    if dev_pt:
        tap(dev_pt[0], dev_pt[1], 1.5)
        capture("opus_device_name_dialog.png")
        cancel_pt = dump_and_find(text_match="CANCEL")
        if cancel_pt:
            tap(cancel_pt[0], cancel_pt[1], 1.0)
        else:
            adb("shell input keyevent KEYCODE_BACK")
            time.sleep(1.0)
    
    adb("shell input keyevent KEYCODE_BACK")
    time.sleep(1.0)

# 4. Reader
print("\n--- 10 & 11 Reader ---")
# Resume cover tap at bottom left
tap(118, 2177, 2.0)
capture("10_reader_chrome_visible.png")

# Hide chrome
tap(540, 1170, 1.5)
capture("11_reader_reading.png")

# Show chrome again
tap(540, 1170, 1.5)

# 5. Reader Sub-screens
print("\n--- Reader Sub-screens (TOC, Search, Annotations) ---")
# TOC
toc_pt = dump_and_find(desc_match="Table of Contents")
if toc_pt:
    tap(toc_pt[0], toc_pt[1], 1.5)
    capture("20_contents_toc.png")
    close_pt = dump_and_find(desc_match="Close Contents") or dump_and_find(desc_match="Close")
    if close_pt:
        tap(close_pt[0], close_pt[1], 1.0)
    else:
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)

# Search
search_pt = dump_and_find(desc_match="Search Book")
if search_pt:
    tap(search_pt[0], search_pt[1], 1.5)
    capture("21_reader_search.png")
    back_pt = dump_and_find(desc_match="Back to Reader")
    if back_pt:
        tap(back_pt[0], back_pt[1], 1.0)
    else:
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)

# Annotations
annot_pt = dump_and_find(desc_match="Annotations and Highlights")
if annot_pt:
    tap(annot_pt[0], annot_pt[1], 1.5)
    capture("22_annotations.png")
    
    # Filter
    f_pt = dump_and_find(desc_match="Filter Annotations")
    if f_pt:
        tap(f_pt[0], f_pt[1], 1.5)
        capture("23_annotations_filter_sheet.png")
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)
    
    # Sort
    s_pt = dump_and_find(desc_match="Sort Annotations")
    if s_pt:
        tap(s_pt[0], s_pt[1], 1.5)
        capture("24_annotations_sort_sheet.png")
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)
    
    # Export / Share
    e_pt = dump_and_find(desc_match="Export Annotations")
    if e_pt:
        tap(e_pt[0], e_pt[1], 1.5)
        capture("25_annotations_share_sheet.png")
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)
    
    # Back to reader
    back_pt = dump_and_find(desc_match="Back to Reader")
    if back_pt:
        tap(back_pt[0], back_pt[1], 1.0)
    else:
        adb("shell input keyevent KEYCODE_BACK")
        time.sleep(1.0)

# 6. Display Settings Tabs & Color Themes
print("\n--- Display Settings Tabs & Color Themes ---")
aa_pt = dump_and_find(desc_match="Display Settings")
if aa_pt:
    tap(aa_pt[0], aa_pt[1], 1.5)
    # Font tab
    capture("16_settings_font_tab.png")
    
    # Layout tab
    l_pt = dump_and_find(text_match="Layout")
    if l_pt:
        tap(l_pt[0], l_pt[1], 1.5)
        capture("17_settings_layout_tab.png")
    
    # Themes tab
    th_pt = dump_and_find(text_match="Themes")
    if th_pt:
        tap(th_pt[0], th_pt[1], 1.5)
        capture("18_settings_themes_tab.png")
    
    # More tab
    m_pt = dump_and_find(text_match="More")
    if m_pt:
        tap(m_pt[0], m_pt[1], 1.5)
        capture("19_settings_more_tab.png")
    
    # Back to Layout tab for Color Theme switching
    if l_pt:
        tap(l_pt[0], l_pt[1], 1.5)
        
        # Color positions in Layout tab
        # White (140, 1575), Sepia (270, 1575), Green (400, 1575), Black (530, 1575)
        color_coords = {
            "white": (140, 1575),
            "sepia": (270, 1575),
            "green": (400, 1575),
            "black": (530, 1575)
        }
        
        for cname, coord in color_coords.items():
            print(f"Switching to theme: {cname.upper()}")
            tap(coord[0], coord[1], 1.0)
            # Dismiss sheet by tapping above it (540, 500)
            tap(540, 500, 1.0)
            capture(f"theme_{cname}_chrome_visible.png")
            # Tap center to hide chrome
            tap(540, 1170, 1.0)
            capture(f"theme_{cname}_reading.png")
            # Tap center to show chrome
            tap(540, 1170, 1.0)
            # Reopen Aa
            aa_pt = dump_and_find(desc_match="Display Settings")
            if aa_pt:
                tap(aa_pt[0], aa_pt[1], 1.0)
                l_pt = dump_and_find(text_match="Layout")
                if l_pt:
                    tap(l_pt[0], l_pt[1], 1.0)

print("\n=== Opus Reader Audit Capture Complete! ===")
