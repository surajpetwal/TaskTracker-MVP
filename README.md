# Task Tracker Android App

## Project Overview
A task tracking Android app with SQLite database, following a 30-day development plan to reach MVP and beyond.

## Development Timeline

### Phase 1: Week 1 (Days 1-7) - MVP Release ⭐ Basic Task Tracking
- ✅ **Day 1**: SQLite database (save/load tasks) - COMPLETED
- 🔄 **Day 2**: 3-window swipe UI (Daily/Calendar views) - IN PROGRESS
- ⏳ **Day 3**: Task CRUD (Create/Read/Update/Delete)
- ⏳ **Day 4**: Task points + daily quota
- ⏳ **Day 5**: Missed tasks (red circles top)
- ⏳ **Day 6**: Upcoming tasks (tiles below current)
- ⏳ **Day 7**: Play Store APK + basic testing → **MVP LAUNCH**

## Project Structure

```
TaskTracker/
├── app/
│   ├── src/main/
│   │   ├── java/com/tasktracker/
│   │   │   ├── data/
│   │   │   │   └── TaskDatabase.kt          # SQLite database handler
│   │   │   ├── model/
│   │   │   │   └── Task.kt                 # Task data model
│   │   │   ├── repository/
│   │   │   │   └── TaskRepository.kt       # Data access layer
│   │   │   ├── utils/
│   │   │   │   └── DatabaseTest.kt         # Database testing utilities
│   │   │   └── MainActivity.kt             # Main activity
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml      # Main layout
│   │   │   ├── values/
│   │   │   │   ├── strings.xml            # String resources
│   │   │   │   ├── colors.xml             # Color definitions
│   │   │   │   └── themes.xml             # App themes
│   │   │   └── AndroidManifest.xml        # App manifest
│   └── build.gradle                       # App-level build configuration
├── build.gradle                           # Project-level build configuration
├── gradle.properties                      # Gradle properties
├── settings.gradle                        # Gradle settings
└── README.md                              # This file
```

## Day 1 Implementation ✅

### Database Features Implemented:
- **SQLite Database**: Complete database setup with `TaskDatabase.kt`
- **Task Model**: Comprehensive `Task.kt` data class with all required fields
- **Repository Pattern**: `TaskRepository.kt` for clean data access
- **CRUD Operations**: Full Create, Read, Update, Delete functionality
- **Date-based Queries**: Get tasks by date, today's tasks, missed tasks
- **Points System**: Task points and daily quota tracking
- **Testing**: Comprehensive database test suite

### Database Schema:
```sql
CREATE TABLE tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    points INTEGER DEFAULT 1,
    is_completed INTEGER DEFAULT 0,
    created_date TEXT NOT NULL,
    due_date TEXT,
    daily_quota INTEGER DEFAULT 10,
    is_missed INTEGER DEFAULT 0
);
```

### Key Features:
- Task persistence with SQLite
- Date-based task organization
- Points and quota tracking
- Missed task tracking
- Comprehensive test coverage

## Next Steps
Ready for Day 2: 3-window swipe UI implementation with Daily and Calendar views.

## Build Instructions
1. Open project in Android Studio
2. Sync Gradle files
3. Run on emulator or device
4. Check Logcat for database test results
