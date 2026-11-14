You are building the front end of a personal Android EPUB reader (working name "Opus Reader"). It must look and behave like the Kindle Android app, with our own content. Not for publication.

Before anything else, read AGENTS.md, docs/UI_SPEC.md and look at every image in docs/reference/. They define the target. The rules in AGENTS.md and the skills in .agents/skills/ apply to all work.

The render engine is being built separately. Do not build it. Reader screens use the `ReaderEngine` interface with a fake implementation.

Work autonomously through these phases. Do not stop to ask questions; decide, and log the choice in docs/DECISIONS.md. Commit after each step with a clear message.

Phase 0: Create the Gradle project (stack in AGENTS.md), git repo, theme (colours, type scale, spacing sampled from the references), and the shared components: top bar, search field, bottom sheet, list row, cover tile, toggle row.
Phase 1: Navigation shell: bottom tabs Home | Library | More, the floating resume cover, and routes for all 20 screens following the tap map in docs/UI_SPEC.md.
Phase 2: Library (grid, list, filter sheet, view and sort sheet, collections), Home, More, Settings, Notifications. Use in-memory fake data. Implement filtering, sorting and view switching for real.
Phase 3: Reader chrome (top bar, scrubber, bookmark ribbon, tap zones), Contents, Search, Annotations with filter, sort and export sheets, and the four display-settings tabs. Settings persist in DataStore and are exposed through `ReaderEngine`.
Phase 4: Replace the store with Project Gutenberg using the gutenberg-catalog skill, and add Import (file picker and share intent) in place of Send-to-Kindle.

After each phase, run the android-verify-on-device skill on every screen touched, and update docs/PROGRESS.md: what is done, differences from the reference, what is inferred. When everything is done, write a short plain-language summary and the exact commands to build and run, in fenced code blocks at the very end.
