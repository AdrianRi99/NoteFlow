
# 📝 NoteFlow

A modern Android note-taking application built with a strong focus on **clean architecture, scalability, and production-ready engineering practices**.

NoteFlow allows users to create, edit, and organize notes with categories, images, and recurring reminders, while also providing a daily inspiration feature with local caching and background updates.



# 📱 Screenshots

## Dashboard

<p align="left">
  <img src="https://github.com/user-attachments/assets/dbe43127-969d-4605-9e17-b3e0faf345bc" width="40%">
</p>

## Note Detail

<p align="left">
  <img src="https://github.com/user-attachments/assets/4d2f8b26-81e4-48c6-9277-09bb93798539" width="40%">
</p>

## Create / Edit Note

<p align="left">
  <img src="https://github.com/user-attachments/assets/bf685452-3e80-4252-955a-8acaa2e5e532" width="40%">
</p>

# ✨ Features

* 📝 Create, edit and delete notes
* 🏷️ Categorize notes with tags
* 🖼️ Attach images to notes
* ⏰ Recurring reminders (weekly scheduling)
* 📜 Daily quote with offline caching
* 🔍 Search and filter notes
* 📊 Dashboard with stats and quick overview
* ⚡ Smooth and modern UI built with Jetpack Compose



# 🏗️ Architecture

This project follows **Clean Architecture principles** with a clear separation of concerns:

```
presentation/
    screens, viewmodels, ui components

domain/
    models, use cases, repository interfaces

data/
    repository implementations
    local (Room, DataStore)
    remote (API)
```

### Key Concepts

* **Use Cases** → encapsulate business logic
* **Repository Pattern** → abstraction over data sources
* **Single Source of Truth**
* **Reactive UI with StateFlow & SharedFlow**
* **Unidirectional Data Flow**



# 🧠 Tech Stack

* **Kotlin**
* **Jetpack Compose**
* **MVVM**
* **Clean Architecture**
* **Hilt (Dependency Injection)**
* **Room (Local Database)**
* **DataStore (Preferences)**
* **WorkManager (Background Tasks)**
* **Ktor (Networking)**
* **Coroutines & Flow**



# 🧪 Testing

The project includes a structured testing approach:

* ✅ **Unit Tests**

  * UseCases
  * ViewModels
* ✅ **Integration Tests**

  * Room database (DAO tests)
* ✅ **UI Tests**

  * Jetpack Compose UI testing
* ✅ **Testable Architecture**

  * Fake repositories & dependency injection for isolation



# ⚙️ How it works

### Notes

* Stored locally using Room
* Reactive updates via Flow

### Reminders

* Scheduled using AlarmManager
* Automatically rescheduled after trigger

### Quotes

* Loaded from API (Ktor)
* Cached locally using DataStore
* Refreshed daily via WorkManager



# 🚀 Getting Started

```bash
git clone https://github.com/AdrianRi99/NoteFlow.git
```

Open in **Android Studio** and run the app.


# 📌 Project Goals

* Demonstrate modern Android development practices
* Showcase clean architecture and scalability
* Provide a production-ready app structure
* Serve as a reference project for best practices
