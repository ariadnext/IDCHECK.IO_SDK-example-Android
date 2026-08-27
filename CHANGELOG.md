# Changelog

## [9.1.1] - 2026-08-07
### Added
- New optional wording key `axt_consent_etsi_purpose` to display the identity verification purpose on the document list screen (ETSI mode). Nothing is displayed if the key is not overridden
- New `INVALID_CONFIGURATION` error detail, returned with the `CUSTOMER_ERROR` cause

### Changed
- New design system: Red Hat Display typeface and updated IDnow brand colors
- NFC reading is now more reliable and consistent across devices and document types, with clearer errors and navigation on both platforms
- SDK screens aligned with the design specifications on both platforms:
  - The close icon now only quits the SDK, back navigation always uses a chevron
  - Corrected titles, error screens, tips and animations
  - Wording fixes, including the French liveness instruction shown with the BioID provider
- ⚠️ Breaking: `minSdk` raised from 21 to 23 (Android 6.0). Your app `minSdk` must be raised before upgrading
- ⚠️ Breaking: a new maven repository must be declared in `your_app/android/build.gradle` to resolve the `io.idnow` dependencies:
  ```kotlin
  maven {
      url "https://raw.githubusercontent.com/idnow/idnow-android-sdk/main"
      content {
          includeGroupByRegex("io\\.idnow.*")
      }
  }
  ```

### Fixed
- Fix verso capture failing on a specific French document
- Fix selfie not being analyzed
- Fix `max-tries-quality-issues-before-capture` limiting the number of liveness attempts with the Unissey provider
- Fix crashes on camera zoom, process death and capture after the end of a session

## [9.1.0] - 2026-02-27
### Added
- New NFC library (activate with Customer Configuration flag)
- Add possibility to hide the skip NFC button (by Customer Configuration flag)
    - ⚠️ It must be specified and discussed with the legal department
- Allow multiple workspace by realm with workspace specified inside the token
- Possibility to override all the SDK wordings inside Customer Configuration
- New resources exclusion line should be added:
  ```kotlin
  packaging {
    resources {
      excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
    }
  }
  ```

## [9.0.3] - 2026-01-21
### Added
- Introduces lottie animation delay
- Introduces consent screen update
- Introduces Unissey translations

## [9.0.2] - 2025-12-08
### Fixed
- fix crash when the user clicks on ‘about data protection’ link
- fix crash on camera on Samsung Android 15 devices

## [9.0.1] - 2025-11-24
### Added
- Introduces russian language
### Fixed
- Session is killed by quitting flow with integrity check setting in online mode.

## [9.0.0] - 2025-10-13
### Added
- Introduces new customisation possibilities
- Introduces accessibility
