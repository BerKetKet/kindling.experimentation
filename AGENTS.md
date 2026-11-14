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
- Dark theme only for now. Every screen gets a Compose preview.
- Tap targets at least 48dp. Content descriptions on icon buttons.
- Screens not in the screenshots follow the same look. Mark them in docs/PROGRESS.md as "inferred".

## Workflow
- Small git commits with real messages, one branch per phase. Never hand over zips of changes.
- Done means: `./gradlew assembleDebug testDebugUnitTest lintDebug` passes, the app runs on the emulator, and a side-by-side with the reference was checked. Log differences in docs/PROGRESS.md.
- Record choices in docs/DECISIONS.md. Decide instead of asking; ask only when blocked.

## Talking to the user
He does not write code. Plain, short language. Explanations first, then all instructions together at the very end. Commands go in fenced code blocks, bare, with no `#` comment lines (they break his zsh).
