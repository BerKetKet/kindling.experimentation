---
name: reference-screen-implementation
description: Implements or fixes an Android Compose screen so it matches a reference screenshot. Use when building or revising any screen from docs/UI_SPEC.md or docs/reference.
---

# Implement a screen from a reference

1. Open the screen's entry in docs/UI_SPEC.md and its image in docs/reference/. Note every tap target and its destination.
2. Measure the image: margins, row heights, icon sizes, font sizes, colours. Convert px to dp by dividing by 2.625. Put results in the theme (colours, type scale, spacing), not inline.
3. Build the composable stateless, with a dark `@Preview` using fake data that looks like the screenshot.
4. Wire navigation exactly as the spec says. Unbuilt destinations get a simple placeholder route.
5. Reuse shared components (top bar, search field, bottom sheet, list row, cover tile). Do not copy-paste.
6. Run the android-verify-on-device skill and fix visible differences before moving on.
