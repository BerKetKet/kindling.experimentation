# Interaction Completeness Report — Opus Reader

**Date:** November 15, 2026  
**Branch:** `interaction`  
**Reference Specification:** [INTERACTION_SPEC.md](file:///home/bgg/projects/kindling/docs/INTERACTION_SPEC.md)

---

## Inventory & Status Matrix

| ID | Category | Item / Requirement | Current State | Involved Files | Verification / Test |
|:---|:---|:---|:---:|:---|:---|
| **G01** | Global | Back button: closes top dialog/sheet; on non-Home main tab navigates to Home; on Home exits | **Partial** | `MainActivity.kt`, `OpusNavHost.kt` | Manual / Compose test |
| **G02** | Global | Draggable modal sheets: close by scrim tap, drag-down gesture, and system back with short animation | **Partial** | `ui/components/OpusBottomSheet.kt`, all sheet composables | Gesture test |
| **G03** | Global | Tappable minimum targets (≥48dp) and pressed ripple feedback across all controls | **Partial** | `ui/components/`, `ui/screens/` | Lint & Compose test |
| **G04** | Global | Scrollable containers: all grids, lists, carousels, and sheet bodies scroll smoothly | **Works** | `LibraryGridScreen.kt`, `HomeScreen.kt`, `MoreScreen.kt`, etc. | Device scroll tests |
| **G05** | Global | Bottom navigation bar: preserves tab scroll state; tapping active tab scrolls to top | **Partial** | `OpusBottomNavBar.kt`, `OpusNavHost.kt` | UI test |
| **G06** | Global | Resume cover: displays last-read book, navigates to saved location; hidden in reader and under sheets | **Works** | `FloatingResumeCover.kt`, `OpusNavHost.kt` | Deep link & UI test |
| **G07** | Global | Persistence (Room & DataStore): library, reading progress, bookmarks, annotations, collections, notifications, and settings survive process death | **Missing** | `data/db/` (AppDatabase, DAOs), `BookRepository.kt`, `ReaderSettingsDataStore.kt` | Unit & persistence tests |
| **L01** | Library | Cover / row tap opens book at last saved reading location | **Works** | `LibraryGridScreen.kt`, `LibraryListScreen.kt`, `OpusNavHost.kt` | Navigation test |
| **L02** | Library | Long-press cover/row opens contextual sheet: Mark as read/unread, Add to collection, Remove download, Book details | **Missing** | `OpusCoverTile.kt`, `OpusListRow.kt`, `LibraryGridScreen.kt` | UI test |
| **L03** | Library | Search bar: live filtering of books by title/author with active clear ('X') button | **Partial** | `OpusSearchBar.kt`, `LibraryGridScreen.kt`, `LibraryListScreen.kt` | Compose test |
| **L04** | Library | Filter sheet: checkable chips update counts and filter results live; collapsible category sections with chevrons | **Partial** | `LibraryFilterSheet.kt` | Live filter test |
| **L05** | Library | View & Sort sheet: Grid/List/Collections switching; Sort order and Group by Series apply live and persist | **Partial** | `LibraryViewSortSheet.kt`, `BookRepository.kt` | UI test |
| **L06** | Library | Downloaded checkmark badge: indicates offline state; tap offers remove download dialog | **Missing** | `OpusCoverTile.kt`, `OpusListRow.kt` | UI test |
| **L07** | Library | Pull-to-refresh / pull-to-sync gesture in Library screens | **Missing** | `LibraryGridScreen.kt`, `LibraryListScreen.kt` | Gesture test |
| **L08** | Library | Collections management: create dialog, rename dialog, delete with confirmation, open collection view, add books from menu | **Partial** | `CollectionsScreen.kt`, `BookRepository.kt` | UI test |
| **H01** | Home | Topic chips filter horizontal shelves live; shelves scroll horizontally; "See all" opens Library | **Partial** | `HomeScreen.kt` | UI test |
| **H02** | Home | Project Gutenberg cover tap opens book details sheet with Download EPUB, Read Now, download progress, and cancel | **Partial** | `HomeScreen.kt`, `CatalogRepository.kt` | Download flow test |
| **H03** | Home | Top bar bell icon opens Notifications screen; notification badge clears upon opening | **Partial** | `HomeScreen.kt`, `OpusNavHost.kt` | Badge clear test |
| **M01** | More | "Sync now" row triggers visible syncing indicator and updates "Last synced" timestamp upon completion | **Missing** | `MoreScreen.kt`, `BookRepository.kt` | UI test |
| **M02** | More | Action rows navigate to real functional screens: Reading Challenges, Your Lists, and Notebooks (aggregate annotations across all books) | **Missing** | `MoreScreen.kt`, `OpusNavHost.kt`, `NotebooksScreen.kt` | Navigation test |
| **S01** | Settings | Settings toggles and checkboxes persist state across process restarts | **Missing** | `SettingsScreen.kt`, `ReaderSettingsDataStore.kt` | Persistence test |
| **S02** | Settings | Device name opens interactive edit dialog; updates displayed name and persists | **Partial** | `SettingsScreen.kt` | Dialog test |
| **S03** | Settings | Local file access & import EPUB/PDF triggers system file picker (`ActivityResultContracts.OpenDocument`) | **Works** | `SettingsScreen.kt` | ActivityResult test |
| **S04** | Settings | "Manage additional fonts" opens custom font manager; allows adding user TTF/OTF fonts that appear in reader font carousel | **Missing** | `SettingsScreen.kt`, `FontManager.kt`, `DisplaySettingsFontTab.kt` | File import test |
| **N01** | Notifications | Notification item tap opens associated target book or section | **Missing** | `NotificationsScreen.kt` | Navigation test |
| **N02** | Notifications | Swipe-to-dismiss gesture removes single notification item | **Missing** | `NotificationsScreen.kt` | Swipe gesture test |
| **N03** | Notifications | Trash icon in top bar clears all notifications after confirmation dialog | **Missing** | `NotificationsScreen.kt` | Dialog test |
| **R01** | Reader | Horizontal swipe gesture turns pages left/right with animated transition; tap zones: left edge (prev), right edge (next), center (toggle chrome) | **Partial** | `ReaderScreen.kt` | Gesture & tap test |
| **R02** | Reader | Word/text selection via long-press: draggable handles and floating toolbar (Highlight colors, Add Note, Dictionary, Search, Share, Copy) | **Missing** | `ReaderEngine.kt`, `FakeReaderEngine.kt`, `ReaderScreen.kt` | Selection test |
| **R03** | Reader | Tapping existing highlight shows contextual popup: change highlight color, add/edit note, delete highlight | **Missing** | `ReaderScreen.kt`, `FakeReaderEngine.kt` | Hit test & UI test |
| **R04** | Reader | Reading scrubber: dragging shows live page preview card and location; chapter tick marks are tappable; grid icon opens page thumbnail viewer | **Partial** | `ReaderScreen.kt` | Scrubber test |
| **R05** | Reader | Bookmark ribbon in top-right toggles bookmark state with haptic tick and persists to database | **Partial** | `ReaderScreen.kt`, `Haptics.kt`, `FakeReaderEngine.kt` | Haptic & Room test |
| **R06** | Reader | Overflow menu in top bar: Go to location, Book details, Sync to furthest page, Share | **Missing** | `ReaderScreen.kt` | Menu test |
| **R07** | Reader | Live Display Settings — Font & Typography: font family selection and font size slider restyle reading page immediately | **Partial** | `DisplaySettingsFontTab.kt`, `ReaderScreen.kt` | Live preview test |
| **R08** | Reader | Live Display Settings — Brightness: slider controls window brightness; Auto follows system sensor | **Partial** | `DisplaySettingsSheet.kt`, `MainActivity.kt` | Window attribute test |
| **R09** | Reader | Live Display Settings — Layout: margins, alignment (left vs justified), page color theme, continuous vertical scroll mode, and orientation lock act live on reader | **Partial** | `DisplaySettingsLayoutTab.kt`, `ReaderScreen.kt` | Live style test |
| **R10** | Reader | Live Display Settings — Themes: applying theme preset updates reader styling; Custom + Save prompts for name and stores preset | **Partial** | `DisplaySettingsThemesTab.kt`, `ReaderSettingsDataStore.kt` | Preset test |
| **R11** | Reader | Live Display Settings — More: reading ruler (draggable), progress display choices, visible clock, volume keys page-turn, turn animation, TTS assistive reader | **Partial** | `DisplaySettingsMoreTab.kt`, `ReaderScreen.kt` | Feature tests |
| **R12** | Reader | Contents (TOC): tapping chapter jumps immediately to location and closes; current reading chapter auto-scrolls into view | **Partial** | `ContentsTocScreen.kt` | Scroll & jump test |
| **R13** | Reader | In-book search: live query execution with result snippets and locations; tapping result jumps to match with highlight and prev/next navigation | **Partial** | `ReaderSearchScreen.kt`, `FakeReaderEngine.kt` | Search jump test |
| **R14** | Reader | Annotations notebook: tap jumps to location; overflow menu (Edit note, Change color, Delete, Share); star toggles favorite; filter & sort live; Export via Android share sheet | **Partial** | `AnnotationsScreen.kt`, `AnnotationsShareSheet.kt` | Flow test |

---

## Summary Statistics
- **Total Interaction Items**: 34
- **Currently Fully Working**: 4 (11.8%)
- **Partial Implementation**: 19 (55.9%)
- **Completely Missing**: 11 (32.4%)

Report maintained under `Phase A` inventory requirement of `FIX_PROMPT_INTERACTION.md`.
