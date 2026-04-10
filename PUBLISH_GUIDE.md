# Task Tracker - Play Store Publishing Guide

## Quick Start - 5 Steps to Publish

### Step 1: Generate Signing Key (One-time only)
```bash
cd /home/Suraj/CascadeProjects/TaskTracker
keytool -genkey -v -keystore tasktracker.keystore -alias tasktracker -keyalg RSA -keysize 2048 -validity 10000
```
**Remember your password!** You'll need it for all future releases.

### Step 2: Configure Signing
Edit `app/build.gradle` and add:
```gradle
android {
    signingConfigs {
        release {
            storeFile file("../tasktracker.keystore")
            storePassword "YOUR_PASSWORD"
            keyAlias "tasktracker"
            keyPassword "YOUR_PASSWORD"
        }
    }
    
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }
}
```

### Step 3: Build Release AAB
```bash
./gradlew bundleRelease
```
Output: `app/build/outputs/bundle/release/app-release.aab`

### Step 4: Create Play Console Account
1. Go to [play.google.com/console](https://play.google.com/console)
2. Pay $25 one-time fee
3. Complete verification

### Step 5: Upload & Publish
1. Create new app → "Task Tracker"
2. Upload `app-release.aab` to Production
3. Fill store listing using content from `PLAYSTORE_LISTING.md`
4. Upload assets (icon, screenshots, feature graphic)
5. Submit for review

---

## Store Listing Checklist

### App Details
- [ ] App name: "Task Tracker"
- [ ] Short description: "Track tasks, earn points, build streaks"
- [ ] Full description: Copy from `PLAYSTORE_LISTING.md`
- [ ] Category: Productivity

### Graphics
- [ ] App icon (512x512 PNG)
- [ ] Feature graphic (1024x500 PNG)
- [ ] Phone screenshots (2-8 images)
- [ ] Optional: Tablet screenshots

### Content
- [ ] Privacy policy URL (upload `PRIVACY_POLICY.md` to GitHub Pages or similar)
- [ ] App access: All functionality available without login
- [ ] Ads: No ads
- [ ] Content rating: Everyone

---

## Required Content (Already Created)

| Asset | File | Status |
|-------|------|--------|
| Privacy Policy | `PRIVACY_POLICY.md` | ✅ Ready |
| Store Listing | `PLAYSTORE_LISTING.md` | ✅ Ready |
| App Strings | `strings.xml` | ✅ Ready |
| Screenshots Guide | `PLAYSTORE_ASSETS.md` | ✅ Ready |

---

## Build Commands

```bash
# Debug build (for testing)
./gradlew assembleDebug

# Release build (requires signing key)
./gradlew assembleRelease

# Play Store bundle (preferred)
./gradlew bundleRelease

# Run tests
./gradlew test
```

---

## Post-Upload Timeline

1. **Upload:** Immediate
2. **Processing:** 5-30 minutes
3. **Review:** 1-3 days (usually 24 hours)
4. **Live on Play Store:** After approval

## Troubleshooting

**Build fails?**
- Check Java version: `java -version` (should be 17 or 21)
- Check Android SDK path is set

**AAB upload fails?**
- Ensure AAB is signed
- Check package name is unique
- Verify versionCode is incremented

**Need help?**
- Play Console Help: [support.google.com/googleplay](https://support.google.com/googleplay)

---

## 🎉 You're Ready!

Your TaskTracker MVP is complete and ready for the Play Store. Follow these steps and your app will be live within days!
