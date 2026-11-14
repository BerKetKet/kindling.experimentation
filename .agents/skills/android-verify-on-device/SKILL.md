---
name: android-verify-on-device
description: Builds, installs and screenshots the app, then compares it with the reference screenshots. Use before reporting any UI work as finished.
---

# Verify on device

Use an emulator (or phone) at 1080x2340, 420 dpi, which is density 2.625.

1. Build and test:
   `./gradlew assembleDebug testDebugUnitTest lintDebug`
2. Install and launch: `adb install -r app/build/outputs/apk/debug/app-debug.apk`
3. Navigate to the screen, then capture: `adb exec-out screencap -p > shot.png`
4. Make a side-by-side image (reference left, app right), look at it, and list every difference: spacing, size, colour, font, missing element.
5. Fix the differences or record them in docs/PROGRESS.md with a reason. Never report "matches" without step 4.
