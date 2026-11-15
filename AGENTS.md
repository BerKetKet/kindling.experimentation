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
- Allowed masks: status bar, gesture bar, system keyboard, reading-text area. Never widen a mask to pass. Never edit references.
- All sizes are measured from the references and written as px / 2.625. No eyeballed dp values.
- Dialogs and screens without a reference are "inferred": same visual language, excluded from pass/fail, listed in docs/PROGRESS.md.

## Workflow
- Small git commits with real messages, one branch per phase. Never hand over zips of changes.
- Done means: `./gradlew assembleDebug testDebugUnitTest lintDebug` passes, the app runs on the emulator, and a side-by-side with the reference was checked. Log differences in docs/PROGRESS.md.
- Record choices in docs/DECISIONS.md. Decide instead of asking; ask only when blocked.

## Talking to the user
He does not write code. Plain, short language. Explanations first, then all instructions together at the very end. Commands go in fenced code blocks, bare, with no `#` comment lines (they break his zsh).

## Anti-Overfitting & Layered Container Rules (Mandatory)
- **Never copy viewport cutoffs into item sizes**: A screenshot is a 2D slice of a 3D layered, scrollable screen. Elements partially cut off at the bottom edge or behind the bottom bar/sheet scrim must be built at their full, uniform size inside a scrollable container (`LazyVerticalGrid`, `LazyColumn`, `Column(Modifier.verticalScroll())`) with `Modifier.clipToBounds()`, letting the parent `Scaffold` or sheet clip them naturally.
- **No `isFixture` UI branches or `fixture_*.png` screen slices**: `ui/screens/` and `ui/components/` must never accept `isFixture` or render cropped UI screenshot drawables (`R.drawable.fixture_*`). Only book cover art (`cover_book_*.png`) is permitted in drawables.
- **No `ContentScale.FillBounds`**: Always preserve aspect ratio using `ContentScale.Crop` or `ContentScale.Fit`.

## Interaction Completeness (Mandatory)
- Work from `docs/INTERACTION_SPEC.md`: Every visible control, gesture, and state transition must actually function.
- State is managed in ViewModels backed by Room and DataStore. Composables are stateless and take state and hoisted callbacks. Defaulting callbacks to `{}` without functional wiring is forbidden.
- Full gesture support: swipe page-turn, draggable sheets, long-press contextual menus, swipe-to-dismiss items, and pull-to-sync.
- Live settings effects: Display settings must immediately alter the reader canvas rather than only their internal controls.
- Rigorous verification: Every interaction must have a Compose UI test or an automated adb smoke flow, recorded in `docs/INTERACTION_REPORT.md`. Resting-state pixel fidelity must not regress.
