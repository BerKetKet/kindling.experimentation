import sys, json, argparse
import numpy as np
from PIL import Image, ImageFilter
from measure import load, bg_color, bands

ap = argparse.ArgumentParser()
ap.add_argument('ref')
ap.add_argument('app')
ap.add_argument('out')
ap.add_argument('--mask', action='append', default=[], help='x0,y0,x1,y1 in px; repeatable')
ap.add_argument('--tol', type=int, default=12)
ap.add_argument('--edge-tol', type=int, default=2, help='px dilation around high-contrast glyph/icon edges')
ap.add_argument('--status', type=int, default=90, help='top px to mask (status bar)')
ap.add_argument('--bottom', type=int, default=180, help='bottom px to mask (bottom nav / viewport cutoff zone)')
a = ap.parse_args()

r, p = load(a.ref), load(a.app)
assert r.shape == p.shape, f'size mismatch {r.shape} vs {p.shape}'
keep = np.ones(r.shape[:2], bool)
keep[:a.status] = False
if a.bottom:
    keep[-a.bottom:] = False
mask_rects = []
for m in a.mask:
    x0, y0, x1, y1 = map(int, m.split(','))
    keep[y0:y1, x0:x1] = False
    mask_rects.append((x0, y0, x1, y1))

if a.edge_tol > 0:
    r_gray = Image.fromarray(r).convert('L')
    edges = r_gray.filter(ImageFilter.FIND_EDGES)
    for _ in range(a.edge_tol):
        edges = edges.filter(ImageFilter.MaxFilter(3))
    edge_mask = np.array(edges) > 40
    keep = keep & (~edge_mask)

d = np.abs(r.astype(int) - p.astype(int)).max(axis=2)
bad = (d > a.tol) & keep

def band_unmasked(b):
    s, e = b
    if e >= r.shape[0] - a.bottom or s < a.status:
        return False
    for (x0, y0, x1, y1) in mask_rects:
        if (x1 - x0) >= r.shape[1] * 0.8 and s >= y0 and e <= y1:
            return False
    return True

rb = [b for b in bands(r, bg_color(r)) if band_unmasked(b)]
pb = [b for b in bands(p, bg_color(p)) if band_unmasked(b)]
offs = []
for (s, e) in rb:
    c = (s + e) / 2
    if pb:
        j = min(pb, key=lambda q: abs((q[0] + q[1]) / 2 - c))
        offs.append(dict(ref=[s, e], app=list(j), dy_top=j[0] - s, dh=(j[1] - j[0]) - (e - s)))
    else:
        offs.append(dict(ref=[s, e], app=None))

heat = np.zeros_like(r)
heat[bad] = (255, 0, 60)
heat[~bad] = (r[~bad] * 0.35).astype(np.uint8)
sheet = Image.new('RGB', (r.shape[1] * 3 + 20, r.shape[0]), (80, 0, 80))
for i, im in enumerate((r, p, heat)):
    sheet.paste(Image.fromarray(im), (i * (r.shape[1] + 10), 0))
sheet.save(a.out + '_diff.png')

res = dict(
    pct_bad=round(100 * bad.sum() / max(keep.sum(), 1), 2),
    bands_ref=len(rb),
    bands_app=len(pb),
    worst_offsets=sorted(offs, key=lambda o: -abs(o.get('dy_top', 999) if o['app'] else 999))[:8]
)
print(json.dumps(res, indent=1))
