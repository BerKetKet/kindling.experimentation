---
name: natural-layout
description: Build complete, uniform Jetpack Compose components that clip naturally inside scrollable containers without viewport overfitting.
---
# Natural Layout Skill

1. **Never copy viewport cutoffs**: An element cut off at the bottom edge of a reference screenshot is a complete element inside a scrollable container (`LazyColumn`, `LazyVerticalGrid`, or `verticalScroll`). Always build the full-height element and let the container clip it naturally.
2. **Zero screenshot-slice drawables**: Never crop UI bars, rows, chips, or sheets from reference PNGs into `res/drawable/fixture_*.png`. Only genuine book cover images (`cover_0..11.png`) and vector icons (`ic_*.xml`) are permitted.
3. **Zero `isFixture` UI branches**: Every screen composable must render the exact same live, interactive Jetpack Compose UI in both normal navigation and deep-link evaluation.
4. **Tighter Kindle Typography Scale**: Use `AmazonEmberFontFamily` and `BookerlyFontFamily` with `PlatformTextStyle(includeFontPadding = false)` and Kindle's tighter `12.5sp`–`15sp` scale so text rows never wrap prematurely or drift downward.
5. **Run Guard Before Every Commit**: Always run `python3 tools/guard.py .` and `python3 tools/compare_typography.py --id <ID>` to verify `0–1 px` band alignment.
