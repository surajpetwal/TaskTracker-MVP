# Branch Strategy

## Overview
Each day of development has its own branch for isolated testing and builds.

## Branch Structure

### Main Branches
- `main` - Primary branch (test/debug builds)
- `day-3-crud` - Current stable development branch

### Day-wise Feature Branches
- `day-1-database` - SQLite database setup
- `day-2-ui` - Basic UI implementation
- `day-3-crud` - CRUD operations
- `day-4-points` - Points system
- `day-5-missed` - Missed tasks tracking
- `day-6-upcoming` - Upcoming tasks view
- `day-7-apk` - APK generation
- `day-8-notifications` - Notifications & reminders
- `day-9-categories` - Task categories/tags (current)
- `day-10-export` - Data export/backup (planned)
- `day-11-settings` - App settings (planned)
- `day-12-widgets` - Home screen widgets (planned)
- `day-13-themes` - Custom themes (planned)
- `day-14-polish` - Final polish & bug fixes (planned)

### Special Branches
- `debug` - Debug testing branch
- `debug-testing` - Experimental features

## Workflow

### Starting New Day
```bash
# From previous day's branch
git checkout day-8-notifications

# Create new day branch
git checkout -b day-9-categories

# Push to remote
git push -u origin day-9-categories
```

### Daily Development
1. Work on `day-X-feature` branch
2. Commit regularly with clear messages
3. Push to remote after each feature
4. GitHub Actions auto-builds the branch
5. Test APK from artifacts

### Merging to Main
```bash
# When day is complete
git checkout main
git merge day-9-categories
git push origin main
```

## Build Status
Each branch has independent GitHub Actions builds:
- URL: `https://github.com/surajpetwal/TaskTracker-MVP/actions`
- Filter by branch to see specific build

## Current Active Branch
**day-9-categories** - Task categories & tags feature
