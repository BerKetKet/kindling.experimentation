import re, sys, pathlib
root = pathlib.Path(sys.argv[1] if len(sys.argv) > 1 else '.')
main = root / 'app/src/main'
bad = []
def add(kind, p, n=None, txt=''): bad.append((kind, str(p.relative_to(root)), n, txt.strip()[:90]))
for p in main.rglob('*.kt'):
    for i, line in enumerate(p.read_text(errors='ignore').splitlines(), 1):
        if re.search(r'\bisFixture\b', line) and 'ui/' in str(p): add('isFixture branch in UI', p, i, line)
        if 'ContentScale.FillBounds' in line: add('FillBounds (deforms images)', p, i, line)
        if re.search(r'R\.drawable\.fixture_', line): add('fixture image in main code', p, i, line)
        if re.search(r'offset\((x|y) *= *-|clipToBounds\(\)|requiredHeight\(|translation(X|Y) *=|absoluteOffset', line): add('layout hack', p, i, line)
for p in (main / 'res').rglob('*'):
    if p.suffix.lower() in ('.png', '.webp', '.jpg', '.jpeg') and 'mipmap' not in str(p): add('raster image in main/res (covers belong in src/debug)', p)
pat = re.compile(r'kindle\.com|protonmail|gebregiorgis|bereket', re.I)
for p in list(root.glob('app/**/*.kt')) + list(root.glob('docs/**/*.md')) + list(root.glob('*.md')):
    for i, line in enumerate(p.read_text(errors='ignore').splitlines(), 1):
        if pat.search(line): add('personal data', p, i, line)
by = {}
for k, *_ in bad: by[k] = by.get(k, 0) + 1
for k, n in sorted(by.items()): print(f'{n:4d}  {k}')
if '-v' in sys.argv:
    for b in bad: print(b)
print('GUARD FAIL' if bad else 'GUARD OK')
sys.exit(1 if bad else 0)
