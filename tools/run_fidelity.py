#!/usr/bin/env python3
import os
import sys
import json
import argparse
import subprocess
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

CANONICAL_SCREENS = [
    # id, name, reference_file, notes
    ("D01", "Library grid", "docs/reference/01_library-grid.png", "Dark theme"),
    ("D02", "Home", "docs/reference/02_home.png", "Dark theme"),
    ("D03", "More", "docs/reference/03_more.png", "Dark theme"),
    ("D04", "Settings", "docs/reference/04_settings.png", "Dark theme"),
    ("D05", "Library filter sheet", "docs/reference/05_library-filter-sheet.png", "Bottom sheet over dimmed Library"),
    ("D06", "Library view and sort sheet", "docs/reference/06_library-view-sort-sheet.png", "Bottom sheet over dimmed Library"),
    ("D07", "Library list", "docs/reference/07_library-list.png", "Dark theme"),
    ("D08", "Collections (empty)", "docs/reference/08_collections.png", "Dark theme"),
    ("D09", "Notifications", "docs/reference/09_notifications.png", "Dark theme"),
    ("D10", "Reader chrome visible (dark)", "docs/reference/10_reader-chrome-visible.png", "Scrubber card mode"),
    ("D11", "Contents", "docs/reference/11_contents-toc.png", "Dark theme"),
    ("D12", "In-book search", "docs/reference/12_reader-search.png", "System keyboard masked"),
    ("D13", "Annotations", "docs/reference/13_annotations.png", "Dark theme"),
    ("D14", "Annotations filter sheet", "docs/reference/14_annotations-filter-sheet.png", "Dark theme"),
    ("D15", "Annotations sort sheet", "docs/reference/15_annotations-sort-sheet.png", "Dark theme"),
    ("D16", "Annotations share sheet", "docs/reference/16_annotations-share-via-email.png", "Export annotations dialog"),
    ("D17", "Display settings: Font", "docs/reference/17_display-settings-font-tab.png", "Display settings sheet"),
    ("D18", "Display settings: Layout", "docs/reference/18_display-settings-layout-tab.png", "Display settings sheet"),
    ("D19", "Display settings: Themes", "docs/reference/19_display-settings-themes-tab.png", "Display settings sheet"),
    ("D20", "Display settings: More", "docs/reference/20_display-settings-more-tab.png", "Display settings sheet"),
    ("L01", "Library grid (light)", "docs/kindle_audit/light_01_library_grid.png", "Light theme"),
    ("L02", "Home (light)", "docs/kindle_audit/light_02_home.png", "Light theme"),
    ("L03", "More (light)", "docs/kindle_audit/light_03_more.png", "Light theme"),
    ("L04", "Settings (light)", "docs/kindle_audit/light_04_settings.png", "Light theme"),
    ("L05", "Library filter sheet (light)", "docs/kindle_audit/light_05_library_filter_sheet.png", "Light theme"),
    ("L06", "Library view and sort sheet (light)", "docs/kindle_audit/light_06_library_view_sort_sheet.png", "Light theme"),
    ("L07", "Library list (light)", "docs/kindle_audit/light_07_library_list.png", "Light theme"),
    ("L08", "Collections (light)", "docs/kindle_audit/light_08_collections.png", "Light theme"),
    ("L09", "Notifications (light)", "docs/kindle_audit/light_09_notifications.png", "Light theme"),
    ("R01", "Reader chrome visible (light)", "docs/kindle_audit/reader_light_chrome_visible.png", "Light theme"),
    ("R02", "Reader chrome hidden (light)", "docs/kindle_audit/reader_light_chrome_hidden.png", "Light theme"),
    ("R03", "Reading canvas (white)", "docs/kindle_audit/theme_white_reading.png", "Reading text masked"),
    ("R04", "Reading canvas (sepia)", "docs/kindle_audit/theme_sepia_reading.png", "Reading text masked"),
    ("R05", "Reading canvas (green)", "docs/kindle_audit/theme_green_reading.png", "Reading text masked"),
    ("R06", "Reading canvas (black)", "docs/kindle_audit/theme_black_reading.png", "Reading text masked"),
    ("R07", "Chrome visible (white)", "docs/kindle_audit/theme_white_chrome_visible.png", "White theme chrome"),
    ("R08", "Chrome visible (sepia)", "docs/kindle_audit/theme_sepia_chrome_visible.png", "Sepia theme chrome"),
    ("R09", "Chrome visible (green)", "docs/kindle_audit/theme_green_chrome_visible.png", "Green theme chrome"),
    ("R10", "Chrome visible (black)", "docs/kindle_audit/theme_black_chrome_visible.png", "Black theme chrome"),
    ("R11", "Display settings: Font (light)", "docs/kindle_audit/reader_light_settings_font_tab.png", "Light theme"),
    ("R12", "Display settings: Layout (light)", "docs/kindle_audit/reader_light_settings_layout_tab.png", "Light theme"),
    ("R13", "Display settings: Themes (light)", "docs/kindle_audit/reader_light_settings_themes_tab.png", "Light theme"),
    ("R14", "Display settings: More (light)", "docs/kindle_audit/reader_light_settings_more_tab.png", "Light theme"),
]

def load_masks():
    masks_file = ROOT / "docs" / "fidelity" / "MASKS.md"
    masks = {}
    if not masks_file.exists():
        return masks
    # Format: id: x0,y0,x1,y1 [x0,y0,x1,y1...]
    for line in masks_file.read_text().splitlines():
        line = line.strip()
        if not line or line.startswith("#"):
            continue
        parts = line.split(":", 1)
        if len(parts) == 2:
            sid = parts[0].strip()
            rects = [r.strip() for r in parts[1].split() if r.strip()]
            masks[sid] = rects
    return masks

def run_screen(sid, ref_rel_path, masks_dict, bottom=24, status=90, skip_capture=False):
    ref_path = ROOT / ref_rel_path
    if not ref_path.exists():
        return dict(id=sid, status="MISSING_REF", pct_bad=100.0, worst_offset=999, notes=f"Ref missing: {ref_rel_path}")
    
    app_img = ROOT / "docs" / "fidelity" / f"{sid}_app.png"
    out_prefix = ROOT / "docs" / "fidelity" / sid
    
    if not skip_capture:
        # Deep link
        deep_link = f"opus://fixture/{sid}"
        subprocess.run(["adb", "shell", "am", "start", "-W", "-a", "android.intent.action.VIEW", "-d", deep_link],
                       stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL, check=False)
        # Settle
        subprocess.run(["sleep", "1"])
        # Capture
        cap_res = subprocess.run([str(ROOT / "tools" / "capture.sh"), str(app_img)],
                                 stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        if cap_res.returncode != 0:
            return dict(id=sid, status="CAPTURE_FAILED", pct_bad=100.0, worst_offset=999, notes=cap_res.stderr.strip())

    if not app_img.exists():
        return dict(id=sid, status="NO_CAPTURE", pct_bad=100.0, worst_offset=999, notes="App image not found")

    cmd = [
        sys.executable,
        str(ROOT / "tools" / "pixdiff.py"),
        str(ref_path),
        str(app_img),
        str(out_prefix),
        f"--status={status}",
        f"--bottom={bottom}"
    ]
    for m in masks_dict.get(sid, []):
        cmd.extend(["--mask", m])

    diff_res = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    if diff_res.returncode != 0:
        return dict(id=sid, status="DIFF_ERROR", pct_bad=100.0, worst_offset=999, notes=diff_res.stderr.strip()[:100])

    try:
        data = json.loads(diff_res.stdout)
        pct_bad = data.get("pct_bad", 100.0)
        worst_offs = data.get("worst_offsets", [])
        worst_dy = 0
        if worst_offs:
            first = worst_offs[0]
            if first.get("app"):
                worst_dy = abs(first.get("dy_top", 0))
            else:
                worst_dy = 999
        
        status_str = "PASS" if (pct_bad <= 2.0 and worst_dy <= 1) else "FAIL"
        return dict(
            id=sid,
            status=status_str,
            pct_bad=pct_bad,
            worst_offset=worst_dy,
            notes=f"bad: {pct_bad}%, dy: {worst_dy}px"
        )
    except Exception as e:
        return dict(id=sid, status="PARSE_ERROR", pct_bad=100.0, worst_offset=999, notes=str(e))

def regenerate_report(results):
    report_file = ROOT / "docs" / "FIDELITY_REPORT.md"
    lines = [
        "# Fidelity Report — Opus Reader vs Kindle Reference",
        "",
        "Thresholds per screen (outside masks):",
        "- **Pass threshold**: At most **2.00%** of pixels differ (RGB channel diff > 8), and every element band edge within **1 px** of reference.",
        "- Target device: Samsung Galaxy M32 (1080x2340 @ 420 dpi, density 2.625).",
        "",
        "| ID | Screen Name | Reference File | Diff % | Worst Offset (px) | Status | Notes |",
        "|---|---|---|---|---|---|---|"
    ]

    pass_count = 0
    fail_count = 0

    for item in results:
        sid = item["id"]
        name = item.get("name", sid)
        ref = item.get("ref", "")
        pct = item.get("pct_bad", 0.0)
        dy = item.get("worst_offset", 0)
        st = item.get("status", "PENDING")
        notes = item.get("notes", "")

        if st == "PASS":
            pass_count += 1
            st_badge = "**PASS**"
        elif st == "FAIL":
            fail_count += 1
            st_badge = "FAIL"
        else:
            st_badge = st

        lines.append(f"| {sid} | {name} | `{ref}` | {pct:.2f}% | {dy} px | {st_badge} | {notes} |")

    lines.extend([
        "",
        "## Summary",
        f"- **Total canonical screens**: {len(results)}",
        f"- **Passing (≤ 2.0% diff, ≤ 1px offset)**: {pass_count}",
        f"- **Failing**: {fail_count}",
        f"- **Pass Rate**: {round(100.0 * pass_count / max(len(results), 1), 1)}%",
        "",
        "Report automatically generated by `tools/run_fidelity.py`."
    ])

    report_file.write_text("\n".join(lines) + "\n")
    print(f"Report written to {report_file}")

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--id", help="Single screen id to test (e.g. D01)")
    ap.add_argument("--all", action="store_true", help="Run all canonical screens")
    ap.add_argument("--skip-capture", action="store_true", help="Diff existing captures without invoking adb")
    ap.add_argument("--status", type=int, default=90)
    ap.add_argument("--bottom", type=int, default=24)
    args = ap.parse_args()

    masks_dict = load_masks()
    screens_to_run = CANONICAL_SCREENS
    if args.id:
        screens_to_run = [s for s in CANONICAL_SCREENS if s[0].upper() == args.id.upper()]
        if not screens_to_run:
            print(f"Unknown id: {args.id}")
            sys.exit(1)

    results = []
    for sid, name, ref_file, notes in screens_to_run:
        print(f"Running {sid}: {name} against {ref_file}...")
        res = run_screen(sid, ref_file, masks_dict, bottom=args.bottom, status=args.status, skip_capture=args.skip_capture)
        res["name"] = name
        res["ref"] = ref_file
        print(f"  Result: {res['status']} | {res['pct_bad']}% bad | offset {res['worst_offset']} px")
        results.append(res)

    regenerate_report(results)

if __name__ == "__main__":
    main()
