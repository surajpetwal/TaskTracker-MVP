# Task Tracker Android App

## Project Overview
A task tracking Android app with SQLite database, following a 30-day development plan to reach MVP and beyond.

## MVP Status: ✅ COMPLETE (Days 1-7)

All core features for MVP have been implemented!

## Development Timeline

### Phase 1: Week 1 (Days 1-7) - MVP Release ⭐ Basic Task Tracking
- ✅ **Day 1**: SQLite database (save/load tasks)
- ✅ **Day 2**: 3-window swipe UI (Daily/Calendar/Stats views)
- ✅ **Day 3**: Task CRUD (Create/Read/Update/Delete)
- ✅ **Day 4**: Task points + daily quota system
- ✅ **Day 5**: Missed tasks indicator (red circles)
- ✅ **Day 6**: Upcoming tasks display (horizontal tiles)
- ✅ **Day 7**: Play Store preparation + testing

## Project Structure

```
TaskTracker/
├── app/
│   ├── src/main/
│   │   ├── java/com/tasktracker/
│   │   │   ├── data/
│   │   │   │   ├── TaskDatabase.kt         # SQLite database handler
│   │   │   │   └── PointsManager.kt        # Points & quota management
│   │   │   ├── model/
│   │   │   │   └── Task.kt                 # Task data model
│   │   │   ├── repository/
│   │   │   │   └── TaskRepository.kt       # Data access layer
│   │   │   ├── ui/
│   │   │   │   ├── DailyViewFragment.kt    # Today's tasks view
│   │   │   │   ├── CalendarViewFragment.kt # Calendar view
│   │   │   │   ├── StatsViewFragment.kt    # Statistics view
│   │   │   │   ├── MainPagerAdapter.kt     # ViewPager adapter
│   │   │   │   ├── TaskAdapter.kt          # Task list adapter
│   │   │   │   ├── UpcomingTasksAdapter.kt # Upcoming days adapter
│   │   │   │   └── MissedTasksIndicator.kt # Custom missed tasks view
│   │   │   ├── dialog/
│   │   │   │   └── AddTaskDialog.kt        # Add task dialog
│   │   │   ├── utils/
│   │   │   │   └── DatabaseTest.kt         # Database testing utilities
│   │   │   └── MainActivity.kt             # Main activity
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml       # Main layout
│   │   │   │   ├── fragment_daily_view.xml # Daily view layout
│   │   │   │   ├── fragment_calendar_view.xml # Calendar layout
│   │   │   │   ├── fragment_stats_view.xml # Stats layout
│   │   │   │   ├── item_task.xml           # Task item layout
│   │   │   │   ├── item_upcoming_task.xml  # Upcoming task tile
│   │   │   │   └── dialog_add_task.xml     # Add task dialog
│   │   │   ├── values/
│   │   │   │   ├── strings.xml             # String resources
│   │   │   │   ├── colors.xml              # Color definitions
│   │   │   │   └── themes.xml              # App themes
│   │   │   └── AndroidManifest.xml         # App manifest
│   └── build.gradle                        # App-level build configuration
├── build.gradle                            # Project-level build configuration
├── gradle.properties                       # Gradle properties
├── settings.gradle                         # Gradle settings
├── PRIVACY_POLICY.md                       # Privacy policy
├── PLAYSTORE_LISTING.md                    # Play Store listing content
└── README.md                               # This file
```

## MVP Features Implemented ✅

### Core Features (Days 1-3):
- **SQLite Database**: Complete database setup with `TaskDatabase.kt`
- **Task Model**: Comprehensive `Task.kt` data class with all required fields
- **Repository Pattern**: `TaskRepository.kt` for clean data access
- **CRUD Operations**: Full Create, Read, Update, Delete functionality
- **3-Window Swipe UI**: Daily, Calendar, and Stats views with ViewPager2
- **Task Adapter**: RecyclerView with click handling for task completion

### Gamification (Day 4):
- **Points System**: Earn points for completing tasks
- **Daily Quota**: Set and track daily point goals
- **Progress Visualization**: Progress bar and status messages
- **Streak Tracking**: Consecutive days completion tracking
- **Stats Dashboard**: Total points, completed tasks, streaks

### Visual Indicators (Day 5):
- **Missed Tasks Indicator**: Custom view showing red circles for missed tasks
- **Real-time Updates**: Indicator appears when tasks are missed
- **Visual Feedback**: Clear visual cues for task status

### Planning Features (Day 6):
- **Upcoming Tasks Display**: Horizontal scrollable tiles showing future days
- **Task Preview**: See task count and points for upcoming days
- **Day Labels**: Tomorrow, Day After, or day name for each tile
- **Calendar Integration**: Date-based task organization

### Play Store Ready (Day 7):
- **Privacy Policy**: Complete privacy policy document
- **Play Store Listing**: Title, description, and metadata
- **Unit Tests**: Basic test suite for core functionality
- **Release Configuration**: Build scripts and signing setup
- **String Resources**: Comprehensive localization support

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

## Build Instructions

### Debug Build:
```bash
./gradlew assembleDebug
```
APK Location: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build:
```bash
./gradlew assembleRelease
```
APK Location: `app/build/outputs/apk/release/app-release-unsigned.apk`

### Play Store Bundle:
```bash
./gradlew bundleRelease
```
AAB Location: `app/build/outputs/bundle/release/app-release.aab`

### Run Tests:
```bash
./gradlew test
```

## Next Phase: Week 2 (Days 8-14)
Ready for enhanced features:
- Notifications & reminders
- Task categories/tags
- Data export/import
- Widget support
- Dark mode
