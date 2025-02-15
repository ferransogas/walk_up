<div align="center">
<h1><i>walk up</i></h1>
<img alt="walk up icon" height="192" src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" width="192"/>
</div>

## Overview

**_walk up_** is a minimalist alarm app designed with a simple premise: to ensure you actually get out of bed. Unlike traditional alarm apps that let you dismiss them with a simple tap, Walk Up requires you to physically walk for 25 seconds before the alarm stops ringing. This simple but effective approach aims to solve the common problem of setting multiple alarms only to dismiss them all and fall back asleep.

## Key Features

- **Single Alarm System**: Intentionally designed with only one configurable alarm to encourage better wake-up habits
- **Walking Detection**: Uses device sensors to detect genuine walking movement
- **Progress Tracking**: Visual circular progress indicator shows how close you are to completing the required walking time
- **Material 3 Design**: Clean, modern UI following the latest Material Design guidelines
- **Lightweight**: Minimal resource usage and battery impact
- **Wide Compatibility**: Supports Android 9 through 15

## Installation

The app requires Android 9.0 (API level 28) or higher. No special permissions are needed beyond those for:
- Displaying notifications
- Setting alarms
- Using device sensors
- Running at startup (for alarm persistence)

You can download the latest APK from the [Releases](https://github.com/ferransogas/walk_up/releases) page.

## Technical Implementation

### Architecture
The app follows modern Android development practices with a focus on:
- Kotlin language features
- Jetpack Compose for UI
- DataStore for preferences
- Foreground services for alarm reliability

### Core Components

#### Alarm Management
- `AlarmViewModel`: Centralizes alarm logic and scheduling
- `AlarmReceiver`: BroadcastReceiver for handling alarm triggers
- `AlarmForegroundService`: Ensures the alarm continues even when the app is in background
- `BootReceiver`: Restores the alarm after device restart

#### Walking Detection
The `WalkDetector` class uses an algorithm that combines data from multiple sensors:
- Accelerometer
- Gyroscope
- Linear acceleration sensor

It applies filtering techniques to accurately detect walking patterns while rejecting false positives from simple movement or shaking.

#### UI Screens
- Main alarm screen with time display and toggle
- Time picker for setting the alarm
- Dismissal screen with circular progress indicator
- Permission request dialogs
- Light and dark theme support

## Testing notes
While the app supports Android 9 through 15, it has been primarily tested on an Android 14 device. Features such as vibration patterns and walking detection require real device sensors and couldn't be fully validated on emulators. The core alarm functionality works across the supported Android versions, but sensor sensitivity and detection accuracy may vary slightly between different device models and Android versions.

## Challenges & Learnings

### Android System Integration
- Working with AlarmManager across different Android versions
- Handling system restrictions on background tasks
- Managing sensor permissions and battery optimization exceptions

### Sensor Fusion
- Developing algorithms to accurately detect walking
- Filtering sensor noise and false positives
- Optimizing for battery life while maintaining accuracy

### Foreground Services
- Implementing reliable alarm triggering regardless of app state
- Creating user-friendly notifications
- Handling Android's increasingly strict background execution limits

### Permissions & User Experience
- Requesting necessary permissions while explaining their purpose
- Providing fallbacks when permissions are denied
- Ensuring the app works across multiple Android versions with different permission models

## Technical Skills Demonstrated

- **Kotlin**: Coroutines, Flow, extension functions
- **Android Architecture**: ViewModel, Navigation, DataStore
- **Jetpack Compose**: Modern declarative UI
- **Sensor APIs**: Working with device hardware
- **System Integration**: AlarmManager, NotificationManager, VibratorManager
- **Persistence**: DataStore for preferences management
- **Broadcast Receivers**: For system events like boot completion
- **Service Lifecycle**: Handling foreground services properly

## Future Improvements

While maintaining the app's deliberate simplicity, potential enhancements could include:
- Improved walking detection algorithm
- Battery optimization strategies
- Full Boot Resilience: Currently the alarm is reset after a reboot only if the app is started at least once. A future improvement would be to reset the alarm immediately after boot without requiring user interaction
- Modern Permission Handling: Implement more user-friendly permission requests using `shouldRequestPermissionRationale()` and the newer Android permission flows
