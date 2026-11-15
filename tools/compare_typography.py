#!/usr/bin/env python3
import argparse
import json
import sys
from pathlib import Path
from measure import load, bg_color, bands, band_info

ROOT = Path(__file__).resolve().parent.parent

SCREEN_MAP = {
    "D01": "docs/reference/01_library-grid.png",
    "D02": "docs/reference/02_home.png",
    "D03": "docs/reference/03_more.png",
    "D04": "docs/reference/04_settings.png",
    "D05": "docs/reference/05_library-filter-sheet.png",
    "D06": "docs/reference/06_library-view-sort-sheet.png",
    "D07": "docs/reference/07_library-list.png",
    "D08": "docs/reference/08_collections.png",
    "D09": "docs/reference/09_notifications.png",
    "D10": "docs/reference/10_reader-chrome-visible.png",
    "D11": "docs/reference/11_contents-toc.png",
    "D12": "docs/reference/12_reader-search.png",
    "D13": "docs/reference/13_annotations.png",
    "D14": "docs/reference/14_annotations-filter-sheet.png",
    "D15": "docs/reference/15_annotations-sort-sheet.png",
    "D16": "docs/reference/16_annotations-share-via-email.png",
    "D17": "docs/reference/17_display-settings-font-tab.png",
    "D18": "docs/reference/18_display-settings-layout-tab.png",
    "D19": "docs/reference/19_display-settings-themes-tab.png",
    "D20": "docs/reference/20_display-settings-more-tab.png",
}

def needleman_wunsch(ref_bands, app_bands, gap_penalty=45.0):
    n, m = len(ref_bands), len(app_bands)
    dp = [[0.0] * (m + 1) for _ in range(n + 1)]
    ptr = [[0] * (m + 1) for _ in range(n + 1)]
    for i in range(1, n + 1):
        dp[i][0] = i * gap_penalty
        ptr[i][0] = 1
    for j in range(1, m + 1):
        dp[0][j] = j * gap_penalty
        ptr[0][j] = 2

    for i in range(1, n + 1):
        r0, r1 = ref_bands[i - 1]
        rc = (r0 + r1) / 2.0
        rh = r1 - r0 + 1
        for j in range(1, m + 1):
            a0, a1 = app_bands[j - 1]
            ac = (a0 + a1) / 2.0
            ah = a1 - a0 + 1
            cost = abs(ac - rc) + 0.5 * abs(ah - rh)
            match = dp[i - 1][j - 1] + cost
            delete = dp[i - 1][j] + gap_penalty
            insert = dp[i][j - 1] + gap_penalty
            best = min(match, delete, insert)
            dp[i][j] = best
            if best == match:
                ptr[i][j] = 0
            elif best == delete:
                ptr[i][j] = 1
            else:
                ptr[i][j] = 2

    aligned = []
    i, j = n, m
    while i > 0 or j > 0:
        if i > 0 and j > 0 and ptr[i][j] == 0:
            aligned.append((ref_bands[i - 1], app_bands[j - 1]))
            i -= 1
            j -= 1
        elif i > 0 and (j == 0 or ptr[i][j] == 1):
            aligned.append((ref_bands[i - 1], None))
            i -= 1
        else:
            aligned.append((None, app_bands[j - 1]))
            j -= 1
    aligned.reverse()
    return aligned

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--id", required=True, help="Screen ID e.g. D18")
    ap.add_argument("--y0", type=int, default=90, help="Top y cutoff")
    ap.add_argument("--y1", type=int, default=2276, help="Bottom y cutoff")
    args = ap.parse_args()

    sid = args.id.upper()
    ref_path = ROOT / SCREEN_MAP[sid]
    app_path = ROOT / "docs" / "fidelity" / f"{sid}_app.png"
    r, p = load(str(ref_path)), load(str(app_path))
    r_sub = r[args.y0:args.y1]
    p_sub = p[args.y0:args.y1]
    r_bg = bg_color(r_sub)
    p_bg = bg_color(p_sub)
    rb = [(b[0] + args.y0, b[1] + args.y0) for b in bands(r_sub, r_bg)]
    pb = [(b[0] + args.y0, b[1] + args.y0) for b in bands(p_sub, p_bg)]

    pairs = needleman_wunsch(rb, pb)
    out = []
    for r_b, a_b in pairs:
        if r_b and a_b:
            out.append({
                "ref": list(r_b),
                "app": list(a_b),
                "dy_top": a_b[0] - r_b[0],
                "dh": (a_b[1] - a_b[0]) - (r_b[1] - r_b[0]),
            })
        elif r_b:
            out.append({"ref": list(r_b), "app": None, "dy_top": None, "dh": None})
        else:
            out.append({"ref": None, "app": list(a_b), "dy_top": None, "dh": None})
    print(json.dumps({"id": sid, "ref_bands": len(rb), "app_bands": len(pb), "alignment": out}, indent=2))

if __name__ == "__main__":
    main()
