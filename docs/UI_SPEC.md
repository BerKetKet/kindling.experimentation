# UI spec — tap map

Numbers match docs/reference/NN_*.png. dp = px / 2.625 (screenshots are 1080x2340). Dark theme is the only theme captured.

Persistent: bottom tabs Home | Library | More; floating resume-cover (bottom-left) opens the last book.


## 01 Library · grid  (ref: 01_library-grid.png)

- Filter icon → 05 Filter sheet
- Sort icon → 06 View & sort sheet
- Cover tap → 10 Reader
- Cover long-press → book menu (not captured)
- Search bar → filters the library in place
- Resume cover (bottom-left) → 10 Reader
- Tabs → 02 Home | 01 Library | 03 More

## 02 Home  (ref: 02_home.png)

- Bell with red dot → 09 Notifications
- Search bar → global search
- Chips Explore / All / … → swap the shelf set
- From Your Library cover → 10 Reader
- See All → 01 Library
- Shelf cover (store rows) → book details (not captured) – Gutenberg shelves
- Tabs → 02 | 01 | 03

## 03 More  (ref: 03_more.png)

- Search bar → global search
- Sync now → runs sync, updates "last synced"
- Reading Challenges → stats screen (later)
- Your Lists → lists screen (later)
- Notebooks → all annotations, all books (like 13)
- Gear icon → 04 Settings
- Kindle+ / Family Sharing → drop

## 04 Settings  (ref: 04_settings.png)

- Back arrow → 03 More
- Device name, Push notifications → detail screens
- Download to SD card → checkbox
- Local file access → import folders (sideload)
- Send-to-Kindle email → replace: Import via file picker / share
- Sync → checkbox
- Manage additional fonts → font manager
- 1-Click payment → drop

## 05 Library · Filter sheet  (ref: 05_library-filter-sheet.png)

- Read state: Read (n) / Unread (n) → toggles
- Content type: Books / Samples / Docs → toggles
- DONE → 01 with filter applied

## 06 Library · View & sort sheet  (ref: 06_library-view-sort-sheet.png)

- Group by Series → toggle
- Grid → 01 Library grid
- List → 07 Library list
- Collections → 08 Collections
- Sort: Most Recent / Title / Author A-Z / Z-A → apply
- DONE → back to 01 or 07

## 07 Library · list  (ref: 07_library-list.png)

- Filter icon → 05 Filter sheet
- Sort icon → 06 View & sort sheet
- Row (cover, title, author, % and size) → 10 Reader
- Check circle → downloaded / remove download
- Tabs → 02 | 01 | 03

## 08 Collections  (ref: 08_collections.png)

- Plus icon → new-collection dialog (not captured)
- Sort icon → sort sheet
- CREATE A NEW COLLECTION → same dialog
- Collection row (when any exist) → its books

## 09 Notifications  (ref: 09_notifications.png)

- Back arrow → previous tab
- Trash icon → clear all
- Item tap → its target (book or feature)
- Own app: sync, download, update messages only

## 10 Reader · chrome visible  (ref: 10_reader-chrome-visible.png)

- Back → 01 Library
- List icon → 11 Contents
- Magnifier → 12 Search
- Notebook icon → 13 Annotations
- Aa → 17 Display settings sheet
- Three dots → overflow menu (not captured)
- Bookmark ribbon (top-right) → toggle bookmark
- Page centre tap → show/hide chrome; edge taps turn page
- Scrubber, "Location 91 of 879 · 10%" → scrub with page previews
- Grid icon (bottom-left) → page thumbnails (not captured)
- Sample banner → drop (purchase text)

## 11 Contents (TOC)  (ref: 11_contents-toc.png)

- X → 10 Reader
- Row tap → jump to chapter, then 10
- Current chapter → bold with left bar; children indented

## 12 Reader · search  (ref: 12_reader-search.png)

- Back arrow → 10 Reader
- Query field + keyboard → results list (not captured)
- Result tap → jump and highlight in 10

## 13 Annotations  (ref: 13_annotations.png)

- Back arrow → 10 Reader
- Share icon → 16 Export sheet
- Sort icon → 15 Sort sheet
- Filter button → 14 Filter sheet
- Item tap → jump to location in 10
- Item dots → item menu (not captured)
- Add note → note editor (not captured)
- Star → toggle favourite

## 14 Annotations · Filter sheet  (ref: 14_annotations-filter-sheet.png)

- Type chips: Favorite, Note, Highlight, Underline, Outline, Bookmark (with counts) → toggle
- Colour chips: Default, Gray, Red, Orange, Yellow, Green, Aqua, Blue, Purple, more below → toggle
- X → 13

## 15 Annotations · Sort sheet  (ref: 15_annotations-sort-sheet.png)

- Location / Date created / Date modified → pick
- Ascending / Descending → pick
- Dismiss → 13

## 16 Annotations · Share via email  (ref: 16_annotations-share-via-email.png)

- Email field and + → add address
- Address chips → remove with x
- Send → replace: export Markdown/text via Android share sheet
- X → 13

## 17 Display settings · Font tab  (ref: 17_display-settings-font-tab.png)

- Tabs Font / Layout / Themes / More → 17 | 18 | 19 | 20
- Font carousel: Bookerly, Caecilia, Droid Serif, Georgia… → select
- Size slider (A to A) → reflow
- Spacing → spacing controls (below the fold)
- Brightness slider + Auto → adjust
- Drag handle / tap above → 10

## 18 Display settings · Layout tab  (ref: 18_display-settings-layout-tab.png)

- Page colour: white, sepia, green, black → select
- Update page using system theme → checkbox
- Continuous scrolling → toggle
- Margins (3 presets) → select
- Alignment: justified / left → select
- Orientation lock → toggle

## 19 Display settings · Themes tab  (ref: 19_display-settings-themes-tab.png)

- Custom + Save → save current settings as a theme
- Compact / Standard / Large / Low Vision → apply preset

## 20 Display settings · More tab  (ref: 20_display-settings-more-tab.png)

- Assistive reader (text-to-speech) → toggle
- Reading ruler, Reading progress → pickers
- Visible clock, volume keys turn pages, page-turn animation, annotation menu → toggles

## Replacements (Kindle-specific → ours)
- Store, shelves, Prime/Kindle+ chips → Project Gutenberg browse (Gutendex API), plus local imports
- Send-to-Kindle email, Local file access → Import (file picker, share intent, watched folder)
- Purchase banner, 1-Click, Family Sharing, Kindle+ → removed
- Share via email → export annotations as Markdown/text
- Notifications → local activity feed (sync, downloads, updates)
## Not in the screenshots (design from convention, keep the same look)
Book details, reader overflow menu, text-selection menu (highlight colours, note, dictionary), page thumbnails, search results, new-collection dialog, annotation item menu, note editor.
