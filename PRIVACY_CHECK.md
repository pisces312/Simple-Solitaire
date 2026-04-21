# Privacy & Security Check

**Date**: 2026-04-21  
**Version**: 3.13 (post-AndroidX upgrade)

---

## Summary

Simple-Solitaire is a **completely offline, privacy-friendly** solitaire card game with no data collection, no tracking, and no network requests.

---

## Checklist

### Permissions ✅ NONE
- AndroidManifest.xml declares **zero permissions**
- No INTERNET, STORAGE, CAMERA, LOCATION, or any other permissions
- App runs entirely offline, no system resource access needed

### Third-Party Dependencies ✅ Clean
All dependencies are official Android libraries:
- `com.google.android.material:material`
- `androidx.core:core`
- `androidx.appcompat:appcompat`
- `androidx.cardview:cardview`
- `androidx.constraintlayout:constraintlayout`
- `androidx.fragment:fragment`
- `androidx.viewpager2:viewpager2`
- `androidx.preference:preference`
- `androidx.coordinatorlayout:coordinatorlayout`

**No**:
- Ad SDKs (AdMob, Facebook Ads, etc.)
- Analytics SDKs (Firebase, Umeng, etc.)
- Tracking SDKs
- Social SDKs

### Network Requests ✅ NONE
- No HTTP/HTTPS URLs in source code (except one StackOverflow reference comment)
- No UrlConnection, OkHttp, Retrofit, Volley usage
- No background data upload

### Hardcoded Secrets ✅ NONE
- No API keys
- No tokens/secrets
- No passwords/credentials
- Only game progress stored in SharedPreferences

### Data Storage ✅ Local Only
- SharedPreferences for game statistics and settings
- `allowBackup="true"` - game data migrates with user
- `fullBackupContent` backs up only sharedpref
- No cloud sync, no external storage

### Potential Concerns ⚠️ Minor
- `allowBackup="true"`: Data accessible via backup (not a risk for game data)
- No encryption: Game data stored in plaintext (not sensitive data)

---

## Rating

🟢 **Excellent** - Highly recommended for privacy-conscious users.

This app perfectly matches the requirement for offline, single-player games with zero privacy concerns.