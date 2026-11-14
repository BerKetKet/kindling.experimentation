You are continuing work on "Opus Reader", a personal Android EPUB reader whose UI must be a pixel-exact copy of the Kindle Android app, with our own content. Not for publication.

Context: the first draft exists and an audit compared it to Kindle. I reviewed the audit and the images. Its report is not reliable: it claims matches that do not hold, mixes two pair numbering schemes, compares different content, and includes bad Kindle captures. Opus is still far from the references: sheets are full-screen panels instead of bottom sheets, display settings is full-screen, the reader chrome lacks the page-card scrubber mode, fonts are wrong, the nav bar has no labels, settings uses switches instead of checkboxes, and light theme is missing. The full list is in docs/FINDINGS_V1.md.

Read first, in this order: AGENTS.md, docs/FINDINGS_V1.md, docs/CANONICAL_SCREENS.md, docs/PIXEL_ANCHORS.md, and the skills in .agents/skills/ (pixel-fidelity-loop, fixture-mode, ui-fonts, reference-screen-implementation, android-verify-on-device, gutenberg-catalog). Then look at every reference image listed in CANONICAL_SCREENS.md.

Goal: every canonical screen passes the thresholds in AGENTS.md against its Kindle reference, measured by tools/pixdiff.py with fixture data. Work autonomously. Do not ask questions; decide, and log decisions in docs/DECISIONS.md. Commit after each screen with a clear message. Do not edit the reference images. Do not widen masks to pass. Do not touch the render engine.

Phase 0, tooling: delete docs/comparison_pairs/pairs/ and the old report. Add the debug fixture mode and the opus://fixture/<id> deep link. Verify tools/capture.sh works on the phone (1080x2340 at 420 dpi). Create docs/fidelity/ and a script that runs every id and regenerates docs/FIDELITY_REPORT.md.
Phase 1, foundations: fonts (ui-fonts skill), colour and type tokens measured from the references, a RefPx helper (px / 2.625f), and shared components built in a debug gallery and diffed against reference crops: top bar, search pill, labelled bottom nav, list row, cover tile with badges and ribbons, checkbox row, chevron row, modal bottom sheet with scrim, drag handle and rounded top corners.
Phase 2, library area: D01, D02, D03, D04, D05, D06, D07, D08, D09. Collections lives inside the Library tab with the tab bar visible.
Phase 3, reader: D10 (page-card scrubber mode with neighbouring pages, ribbon, centred location text, scrubber), immersive reading canvas with true black in the black theme, D11, D12, D13, D14, D15, D16, then the display settings sheet over the visible page (D17-D20). Then reading themes R03-R10.
Phase 4, light theme: L01-L09, R01, R02, R11-R14. Follow the system theme, and honour the "Update Page Using System Theme" checkbox.
Phase 5, inferred screens: restyle the Gutenberg details, new-collection, device-name dialogs and the missing screens in the same visual language. Mark them inferred.

After each phase, run the full fidelity script and update docs/FIDELITY_REPORT.md. If a screen cannot reach threshold after 8 loop iterations, save crops of the remaining differences, state the real cause, and move on.

At the end, write a short plain-language summary: how many screens pass, which do not and why, and exactly how to build, install and run the fidelity script, as commands in fenced code blocks at the very end.
