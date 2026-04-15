# TaskTracker MVP - Development Log

This document tracks all development progress across all branches.

---

## Branch: `day-9-categories` (Active)
**Status:** In Progress  
**Last Updated:** 2025-04-16

### Features Implemented:
1. **Category Field in Task Model**
   - Added `category: String = "General"` field to Task data class
   - Default category is "General"

2. **Database Schema Update**
   - Updated `TaskDatabase.kt` to version 2
   - Added `COLUMN_CATEGORY` to database schema
   - Implemented `onUpgrade` to add category column to existing databases
   - Updated all CRUD operations:
     - `insertTask` - includes category
     - `getAllTasks` - reads and maps category
     - `updateTask` - updates category
     - `getTasksByDate` - includes category
     - `getMissedTasks` - includes category

3. **CategoryManager Utility**
   - Created `CategoryManager.kt` with 8 preset categories
   - Each category has a unique color
   - Methods:
     - `getCategoryColor()` - get color for a category
     - `getAllCategories()` - list all available categories
     - `isValidCategory()` - validate category

4. **Category Colors Added** (`colors.xml`)
   - General: #9E9E9E (Gray)
   - Work: #2196F3 (Blue)
   - Personal: #9C27B0 (Purple)
   - Health: #4CAF50 (Green)
   - Study: #FF9800 (Orange)
   - Shopping: #E91E63 (Pink)
   - Finance: #009688 (Teal)
   - Social: #FF5722 (Deep Orange)

5. **Add Task Dialog Enhancement**
   - Added category dropdown (AutoCompleteTextView) to `dialog_add_task.xml`
   - Updated `AddTaskDialog.kt` to:
     - Populate category dropdown
     - Pass selected category to Task creation
     - Default to "General" if not selected

6. **Task Item UI Update**
   - Added category chip to `item_task.xml`
   - Updated `TaskAdapter.kt` to:
     - Display category chip with color coding
     - Use CategoryManager for color mapping

### Commits:
- `b528acd` - Day 9: Add category support to Task model and database schema
- `0bf210f` - Day 9: Add category selection UI and CategoryManager
- `3548cd8` - Day 9: Add category chip display to task items
- `eb4a4d8` - Day 9: Add feature summary documentation

### Pending Work:
- Add category filter chips to Daily View
- Implement category filtering logic
- Test build on GitHub Actions

---

## Branch: `day-8-notifications` (Complete)
**Status:** Complete  
**Completed:** 2025-04-15

### Features Implemented:
- Notification system with NotificationHelper
- WorkManager for scheduling notifications
- BootReceiver for reboot persistence
- Notification settings dialog
- Daily summary notifications

---

## Branch: `main` (Test/Debug)
**Status:** Test/Debug Branch  
**Purpose:** Main branch serves as test/debug branch, not production

---

## Previous Branches (Day 1-7)
All day-wise branches from Day 1-7 have been merged or archived as part of the development strategy.

---

## Branching Strategy
See `BRANCH_STRATEGY.md` for complete details on day-wise development approach.

---

## APK Signing
See `APK_SIGNING.md` for information on APK signing and installation.

---

## GitHub Actions
Builds are triggered on push to day branches. Check Actions tab for build status.
