---
name: fixture-mode
description: Makes the app render exactly the content shown in a Kindle reference so pixel diffs are meaningful. Use when a screen needs fixed data, scroll position, open sheet or theme for comparison.
---

# Fixture mode

- Debug-only build type. Deep link `opus://fixture/<id>` opens the screen with fixed state for that id. Release builds contain none of this.
- Content must equal the reference: titles, authors, percentages, sizes, counts, badges, ribbons, selected tabs, scroll offset, open sheet, theme. Read them off the reference image.
- Covers: crop each cover from the reference into app/src/debug/assets/fixture_covers/ and use those bitmaps (personal debug use only). Where a cover cannot be cropped cleanly, use a flat block and add a mask rectangle for it.
- Reading text: the engine is separate. Use `FakeReaderEngine` and mask the text area; compare page colour, margins and chrome only.
- Time, battery and notifications are masked or set by tools/capture.sh demo mode.
- Keep a table of id, fixture state, masks in docs/fidelity/MASKS.md.
