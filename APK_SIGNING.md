# APK Signing Guide

## Problem
APK was showing "unsigned and invalid" on Nothing 2a because it wasn't properly signed.

## Solution Applied

### 1. Added Signing Configuration
**File:** `app/build.gradle`

Added signing configs for both debug and release builds:
- Uses Android debug keystore
- Password: `android`
- Alias: `androiddebugkey`

### 2. Updated GitHub Actions
**File:** `.github/workflows/build.yml`

- Added keystore generation step
- Updated artifact paths for signed APK
- Added new branches to build triggers

## How to Install

### Option 1: Download from GitHub Actions (Recommended)

1. Go to: `https://github.com/surajpetwal/TaskTracker-MVP/actions`
2. Click on the latest successful build
3. Scroll down to **Artifacts** section
4. Download **release-apk** (or debug-apk)
5. Transfer to your Nothing 2a
6. Tap to install (allow "Install from unknown sources" if prompted)

### Option 2: Direct Download Link
After build completes:
```
https://github.com/surajpetwal/TaskTracker-MVP/actions/runs/[RUN_ID]/artifacts
```

## Build Status

**Current Build:** Running now...
**Expected Result:** Signed APK ready for installation

## Verification

To verify APK is signed:
```bash
# On your computer with Android SDK
apksigner verify --verbose app-release.apk
```

## For Play Store (Future)

For production Play Store release, replace debug keystore with your own:

```gradle
signingConfigs {
    release {
        storeFile file("my-release-key.jks")
        storePassword System.getenv("STORE_PASSWORD")
        keyAlias System.getenv("KEY_ALIAS")
        keyPassword System.getenv("KEY_PASSWORD")
    }
}
```

Store these as GitHub Secrets for CI/CD builds.
