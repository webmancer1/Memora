# Memora - Android Developer Documentation

##  Project Overview

**Memora** is a native Android application designed to simplify funeral management and planning. This repository contains the source code for the Android client, built with modern Android development practices.

##  Tech Stack

The project is built using **Kotlin** and **Jetpack Compose**.

*   **Language:** [Kotlin](https://kotlinlang.org/) (v2.0.21)
*   **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
*   **Build System:** Gradle (Kotlin DSL)
*   **Minimum SDK:** 24 (Android 7.0)
*   **Target SDK:** 36 (Android 16)

### Key Libraries
*   `androidx.core:core-ktx`: Kotlin extensions
*   `androidx.lifecycle:lifecycle-runtime-ktx`: Lifecycle-aware components
*   `androidx.activity:activity-compose`: Compose integration with Activity
*   `androidx.navigation:navigation-compose`: Navigation for Compose
*   `androidx.compose.material3`: Material Design 3 components

##  Architecture

The application follows the recommended **Guide to App Architecture** (MVVM / Clean Architecture principles).


##  Getting Started

### Prerequisites
*   **Android Studio:** Ladybug (2024.2.1) or newer recommended.
*   **JDK:** Java 11 or higher (Gradle uses the embedded JDK usually).

### Setup Instructions

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/memora-android.git
    cd memora-android
    ```

2.  **Open in Android Studio:**
    *   Launch Android Studio.
    *   Select "Open" and navigate to the project directory.

3.  **Sync Gradle:**
    *   Allow Android Studio to download dependencies and sync the project.

4.  **Run the App:**
    *   Connect a physical device or create an AVD (Android Virtual Device).
    *   Click the **Run** button (green play icon) or press `Shift + F10`.

##  Feature Implementation Status

| Feature | Status | Notes |
| :--- | :--- | :--- |
| **User Accounts** |  In Progress | Login & Register screens created. Auth logic pending. |
| **Dashboard** | Pending | |
| **Service Marketplace** |  Pending | |
| **Budget Manager** |  Pending | |
| **Ceremony Planning** |  Pending | |
| **Guest Management** |  Pending | |
| **Memorial Page** |  Pending | |
| **Notifications** |  Pending | |

##  Contribution Guidelines

1.  **Code Style:** Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
2.  **Branching:** Use feature branches (e.g., `feature/login-auth`, `fix/crash-on-start`).
3.  **Commits:** Write clear, descriptive commit messages.
4.  **Testing:** Ensurses new features are covered by unit tests where possible.


