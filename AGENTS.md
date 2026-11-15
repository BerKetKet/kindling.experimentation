# AGENTS.md — Opus Reader (working name)

Personal Android EPUB reader, not published. The UI copies the Kindle Android app (dark theme first). The content is ours.

## Read first
- docs/UI_SPEC.md: every screen, what each tap does, what to replace.
- docs/reference/*.png: the ground truth. 1080x2340 px at 2.625 density, so dp = px / 2.625.

## Stack
Kotlin, Jetpack Compose, Material 3 restyled to match the references, Navigation-Compose, Room (library, annotations), DataStore (settings), Coil. minSdk 26. One `app` module, package `app.opusreader`.

## Hard boundaries
- The render engine is separate work. Reader screens talk only to the `ReaderEngine` interface in `engine/`. Ship a `FakeReaderEngine` that paginates plain text from a bundled sample. Never touch Rust or `engine/native/`.
- Data goes through repository interfaces. Use in-memory fakes until told otherwise.
- No Amazon or Kindle names, logos or icons in strings or assets. The app name is one constant.
- Replace store and Amazon features as listed under "Replacements" in docs/UI_SPEC.md.

## UI rules
- Match the references: layout, spacing, type scale, colours (sample pixels, do not guess), icons, sheet behaviour.
- Dark and light themes (light follows the system theme, see docs/CANONICAL_SCREENS.md). Every screen gets a Compose preview.
- Tap targets at least 48dp. Content descriptions on icon buttons.
- Screens not in the screenshots follow the same look. Mark them in docs/PROGRESS.md as "inferred".

## Pixel fidelity (the main goal now)
- Source of truth: docs/CANONICAL_SCREENS.md, docs/FINDINGS_V1.md, docs/PIXEL_ANCHORS.md. The old audit report in docs/ is not authoritative.
- A screen is done only when tools/pixdiff.py shows it within threshold against its reference, using fixture data (skill: pixel-fidelity-loop). "Looks the same" is never a result; numbers are.
- Thresholds per screen, outside masks: at most 2% of pixels differ by more than 8 per channel, every element band edge within 1 px of the reference, colours exact. Chrome-only areas should be under 0.5%.
- Allowed masks: status bar, gesture bar, system keyboard, reading-text area, and strips where the reference shows an element cut off or covered by the screen edge, nav bar, sheet or scrim (mask only that strip). Never widen a mask to pass. Never edit references.
- All sizes are measured from the references and written as px / 2.625. No eyeballed dp values.
- Dialogs and screens without a reference are "inferred": same visual language, excluded from pass/fail, listed in docs/PROGRESS.md.

## Real UI only (no pasted pixels, no fixture branches)
Pixel fidelity means a real Compose UI that renders like the reference. It never means showing the reference.
- Never put a crop, slice or recreation of a reference screenshot into the app, as a drawable, bitmap, asset or generated image. Every icon is a vector, every text run is real text in the real font, every row, sheet, bar and chip is a composable.
- One code path. No `isFixture` parameter or `if (fixture)` branch in UI code. Fixture mode supplies data and state only (debug repository, scroll position, open sheet, theme). Production and fixture run the same composables.
- The only raster images allowed are book covers, and only for fixtures, in `app/src/debug/res`. Nothing raster in `app/src/main/res` except launcher icons.
- Never use `ContentScale.FillBounds` (it deforms), fixed heights on content items, negative offsets, manual clips or translations to reproduce a cut or covered element. A screenshot is a frozen viewport of a scrollable, layered UI; copy the design, not the accident.
- Build every element at full natural size inside a real container (LazyColumn, LazyVerticalGrid, LazyRow, Pager with contentPadding for peeks, Scaffold with an opaque bottom bar, modal sheets over the complete live screen). Fixture data goes beyond the fold. Reproduce a reference view with scroll state.
- Allowed masks stay as listed above. Score cannot be improved by replacing live UI with images; if it ever is, the run is invalid.
- Personal data: no real email addresses, device names or account names in code, docs or assets. Use same-shaped placeholders and mask the line if widths differ.
- Run `python3 tools/guard.py .` before every commit. It must print GUARD OK. `tools/run_fidelity.py` must refuse to run if the guard fails.
- Liveness check: after a screen passes, change a fixture string (or font size) and re-capture; the text region must change. A screen whose pixels do not respond to its data is a pasted image.

## Workflow
- Small git commits with real messages, one branch per phase. Never hand over zips of changes.
- Done means: `./gradlew assembleDebug testDebugUnitTest lintDebug` passes, the app runs on the emulator, and a side-by-side with the reference was checked. Log differences in docs/PROGRESS.md.
- Record choices in docs/DECISIONS.md. Decide instead of asking; ask only when blocked.

## Talking to the user
He does not write code. Plain, short language. Explanations first, then all instructions together at the very end. Commands go in fenced code blocks, bare, with no `#` comment lines (they break his zsh).
