# Play Store Assets - Task Tracker

## Required Assets Checklist

### 1. App Icon (512x512 PNG)
**Location:** `app/src/main/res/mipmap-xxxhdpi/ic_launcher_foreground.png`
**Or create:** High-res version for Play Store

**Design specs:**
- Size: 512 x 512 pixels
- Format: PNG or JPEG
- Max file size: 1 MB
- No transparency (fill with background color)
- Safe zone: 384x384 pixels in center

### 2. Feature Graphic (1024x500 PNG)
**This is the banner shown on Play Store**

**Design specs:**
- Size: 1024 x 500 pixels
- Format: PNG or JPEG
- No transparency
- Keep text minimal and centered
- Avoid bottom 50 pixels (may be covered)

**Suggested design:**
```
[Blue gradient background #6200EE]
   
   📋 Task Tracker
   Track • Earn • Succeed
   
[Bottom safe zone - keep empty]
```

### 3. Phone Screenshots (Minimum 2, Maximum 8)
**Required sizes:**
- 1080x1920 (9:16) - Portrait
- 1920x1080 (16:9) - Landscape (optional)

**Required screenshots:**
1. **Daily View** - Show task list, points, quota progress
2. **Calendar View** - Show date picker and tasks
3. **Stats View** - Show statistics dashboard
4. **Add Task Dialog** - Show task creation

### 4. Tablet Screenshots (Optional but recommended)
- 7-inch: 1080x1920 or 1920x1080
- 10-inch: 2560x1600 or 1600x2560

### 5. Promo Video (Optional, 30-120 seconds)
- MP4 or 3GP format
- Max 1 GB
- 16:9 aspect ratio
- Show app features and UI flow

## Quick Asset Generation

### Create Icon (512x512):
Use the existing launcher icon or create enhanced version.

### Create Feature Graphic:
```bash
# Using ImageMagick (if available)
convert -size 1024x500 xc:'#6200EE' feature_graphic.png
```

### Capture Screenshots:
1. Run app on emulator/device
2. Use Android Studio: **View → Tool Windows → Logcat → Screen Capture**
3. Or use device: Power + Volume Down

## Store Listing Content (Already Created):
- ✅ `PLAYSTORE_LISTING.md` - Full description, features
- ✅ `PRIVACY_POLICY.md` - Privacy policy
- ✅ `strings.xml` - App strings

## Next Steps:
1. Generate screenshots from running app
2. Create 512x512 app icon
3. Create 1024x500 feature graphic
4. Build signed release AAB
5. Upload to Play Console
