# AndroidX Migration & Toolchain Upgrade

## Overview

This document records the upgrade process from legacy Android Support Library to modern AndroidX toolchain.

**Date**: 2026-04-21  
**Original Project**: ashish-andev/Simple-Solitaire (v3.13)  
**Fork**: pisces312/Simple-Solitaire

---

## Upgrade Summary

| Component | Before | After |
|-----------|--------|-------|
| Android Gradle Plugin (AGP) | 3.3.0 | 8.4.2 |
| Gradle Wrapper | 4.10.2 | 8.7 |
| compileSdk | 28 | 34 |
| targetSdk | 28 | 34 |
| Java Version | 1.8 | 17 |
| Support Library | android.support.* | AndroidX |
| Build Tools | 28.0.3 (explicit) | Removed (auto) |

---

## Changes Made

### 1. Build Configuration

#### `build.gradle` (root)
- Added `settings.gradle` for plugin repository management
- Updated Gradle wrapper to 8.7

#### `app/build.gradle`
- AGP 3.3.0 → 8.4.2
- compileSdk 28 → 34
- Removed `buildToolsVersion` (auto-resolved by AGP 8.x)
- Removed deprecated `zipAlignEnabled` (default in release builds)
- Added `buildFeatures { buildConfig true }` (required for BuildConfig generation)
- Updated dependencies to AndroidX equivalents
- Added coordinatorlayout dependency
- Java source/target compatibility → VERSION_17

#### `gradle.properties`
- Created new file with AndroidX migration flags
- `android.useAndroidX=true`
- `android.enableJetifier=true`
- `android.nonTransitiveRClass=false` (critical for R.id switch-case compatibility)

#### `settings.gradle`
- Created for plugin repository configuration
- Added Google Maven and Gradle Plugin Portal

### 2. AndroidManifest.xml
- Removed `package` attribute (now defined in build.gradle namespace)
- Added `android:exported="true"` for launcher activity (required by Android 12+)

### 3. Java Source Files (~45 files)

#### Import Replacements (37 files)
All `android.support.*` imports replaced with AndroidX equivalents:
- `android.support.v4.app.Fragment` → `androidx.fragment.app.Fragment`
- `android.support.v7.app.*` → `androidx.appcompat.app.*`
- `android.support.v4.widget.*` → `androidx.core.widget.*`
- `android.support.v7.widget.*` → `androidx.recyclerview.widget.*`
- etc.

#### Switch-Case → If-Else (8 files)
Due to AGP 8.x making R class fields non-final, switch-case with R.id.xxx no longer compiles.
Files converted:
- GameManager.java
- GameSelector.java
- Manual.java
- ManualFeedback.java
- StatisticsActivity.java
- DialogPreferenceCards.java
- DialogPreferenceEnsureMovabilityMinMoves.java
- Game.java (R.style fix)

#### Special Fixes
- `DialogPreferenceCardBackground.java`: R.attr → android.R.attr
- `Game.java`: R.style.TextAppearance_AppCompat → androidx.appcompat.R.style

### 4. XML Layout Files (13 files)
Widget class name replacements:
- `<android.support.v7.widget.Toolbar>` → `<androidx.appcompat.widget.Toolbar>`
- `<android.support.v4.widget.DrawerLayout>` → `<androidx.drawerlayout.widget.DrawerLayout>`
- `<android.support.design.widget.CoordinatorLayout>` → `<androidx.coordinatorlayout.widget.CoordinatorLayout>`
- `<android.support.design.widget.AppBarLayout>` → `<com.google.android.material.appbar.AppBarLayout>`
- `<android.support.v7.widget.CardView>` → `<androidx.cardview.widget.CardView>`
- `<android.support.v7.widget.RecyclerView>` → `<androidx.recyclerview.widget.RecyclerView>`
- `<android.support.v4.view.ViewPager>` → `<androidx.viewpager.widget.ViewPager>`
- `<com.astuetz.PagerSlidingTabStrip>` → `<com.google.android.material.tabs.TabLayout>`

### 5. Deprecated Library Replacements

#### PagerSlidingTabStrip → Material TabLayout + ViewPager2
- Replaced in AboutActivity and StatisticsActivity
- Rewrote TabsPagerAdapter for both activities using FragmentStateAdapter
- Updated layout files: activty_about.xml, activty_statistics.xml

#### AmbilWarna Color Picker → Inline Implementation
Original library `yuku.ambilwarna` not compatible with AndroidX.
Created inline HSV color picker implementation:
- `yuku/ambilwarna/AmbilWarnaDialog.java`
- `yuku/ambilwarna/SatValView.java`
- `yuku/ambilwarna/HueSliderView.java`

Used in:
- DialogPreferenceBackgroundColor.java
- DialogPreferenceTextColor.java

### 6. ProGuard/R8
Release build uses minification and resource shrinking.
ProGuard rules unchanged (library-specific rules not needed for AndroidX).

---

## Build Results

| Build Type | Status | APK Size |
|------------|--------|----------|
| Debug | ✅ Success | ~22 MB |
| Release (unsigned) | ✅ Success | ~17 MB |
| Release (signed) | ✅ Success | ~17 MB |

---

## Key Lessons

1. **nonTransitiveRClass=false**: Essential when project uses R.id in switch-case statements. AGP 8.x defaults to true, making R fields non-final.

2. **PagerSlidingTabStrip**: Legacy library (2013) no longer maintained. Material TabLayout is the modern replacement.

3. **AmbilWarna**: Color picker library not updated for AndroidX. Inline implementation avoids dependency issues.

4. **buildConfig feature**: AGP 8.x requires explicit `buildFeatures { buildConfig true }` to generate BuildConfig class.

5. **android:exported**: Required for all exported components since Android 12 (API 31).

---

## Signing Configuration

- Keystore: `my-android-release.keystore`
- Alias: `pisces312`
- Location: Backup settings directory

---

## Privacy & Security

See `PRIVACY_CHECK.md` for detailed analysis. Key findings:
- No permissions declared (completely offline)
- No third-party analytics/ads SDK
- No network requests
- All data stored locally in SharedPreferences

---

## Future Maintenance

- Consider updating minSdk from 14 to 21 (Android 5.0) for better compatibility
- Lint warning: duplicate IDs in fragment_about_tab1.xml (existing issue, not introduced by upgrade)
- Monitor Material library updates for future TabLayout/ViewPager2 improvements