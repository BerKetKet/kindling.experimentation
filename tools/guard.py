#!/usr/bin/env python3
import os
import re
import sys
from pathlib import Path

FORBIDDEN_DRAWABLE_PATTERNS = [
    "fixture_*.png",
    "fixture_*.webp",
    "fixture_*.jpg",
]

FORBIDDEN_KT_PATTERNS = [
    (r"\bisFixture\b", "Forbidden 'isFixture' UI branch"),
    (r"R\.drawable\.fixture_", "Forbidden 'R.drawable.fixture_*' screenshot slice"),
    (r"ContentScale\.FillBounds", "Forbidden 'ContentScale.FillBounds' aspect-ratio distortion"),
    (r"if\s*\(\s*row\s*==\s*3\s*\)", "Forbidden per-row viewport-cutoff height hack"),
]

def check_repo(root: Path) -> list[str]:
    violations = []
    for res_dir in [root / "app/src/main/res/drawable", root / "app/src/debug/res/drawable"]:
        if res_dir.exists():
            for pat in FORBIDDEN_DRAWABLE_PATTERNS:
                for p in res_dir.glob(pat):
                    violations.append(f"Forbidden drawable slice: {p.relative_to(root)}")

    java_dir = root / "app/src/main/java"
    if java_dir.exists():
        for kt in sorted(java_dir.rglob("*.kt")):
            text = kt.read_text(encoding="utf-8")
            for regex, msg in FORBIDDEN_KT_PATTERNS:
                if re.search(regex, text):
                    violations.append(f"{kt.relative_to(root)}: {msg}")
    return violations

def main():
    target = Path(sys.argv[1]).resolve() if len(sys.argv) > 1 else Path(__file__).resolve().parent.parent
    violations = check_repo(target)
    if violations:
        print("GUARD FAILED — anti-overfitting / natural-layout violations detected:")
        for v in violations:
            print(f"  - {v}")
        sys.exit(2)
    print("GUARD PASSED: 0 screenshot slices, 0 fixture branches, 0 FillBounds hacks.")

if __name__ == "__main__":
    main()
