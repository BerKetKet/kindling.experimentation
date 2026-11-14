---
name: pixel-fidelity-loop
description: Drives a screen to pixel parity with its Kindle reference using fixture data, tools/capture.sh, tools/measure.py and tools/pixdiff.py. Use whenever building, restyling or verifying any screen or component.
---

# Pixel-fidelity loop

Per screen id from docs/CANONICAL_SCREENS.md:

1. Measure the reference: `python3 tools/measure.py <reference.png>`. Record bands, colours and sizes (px and dp) in docs/PIXEL_ANCHORS.md under the screen id. Use these numbers, not guesses.
2. Make the screen render from fixture data identical to the reference (skill: fixture-mode).
3. Build, install, open the fixture: `adb shell am start -a android.intent.action.VIEW -d opus://fixture/<id>`
4. Capture: `tools/capture.sh docs/fidelity/<id>_app.png`
5. Diff: `python3 tools/pixdiff.py <reference.png> docs/fidelity/<id>_app.png docs/fidelity/<id> --bottom 24` plus `--mask` rectangles for cover images, keyboard or reading text.
6. Open `<id>_diff.png` (reference | app | red = different) and read `worst_offsets`. Fix the largest offsets first: layout bands, then colours, then text size and weight, then letter spacing.
7. Repeat up to 8 times. If still over threshold, stop, save crops of the remaining differences to docs/fidelity/, and report the cause honestly in docs/FIDELITY_REPORT.md.

Report format in docs/FIDELITY_REPORT.md: one row per id with pct_bad, worst band offset in px, masks used, status (pass, fail, inferred). Regenerate it from the tools, never type numbers by hand.
Component-level: build shared components in a debug gallery and diff them against crops of the references before using them in screens.
