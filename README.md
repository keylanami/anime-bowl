# AnimeBowl 🍜

AnimeBowl is a modern, beautifully designed Android application that serves as your personal anime watch diary. Built entirely with **Jetpack Compose**, it allows you to discover trending anime, search for your favorites using the Jikan API, and keep a personalized log of what you're watching, completed, or planning to watch.

## ✨ Features

* **Google Authentication:** Secure and seamless login using Firebase Authentication.
* **Cloud Sync & Offline-First:** Your personal logs are saved locally using Room Database (ensuring they are always accessible offline) and automatically synced to **Firebase Firestore** when online.
* **Guest Mode:** Users can browse trending anime and search the catalog without needing to log in. Logging in is only required to write reviews and add logs.
* **Custom Review Cover:** Don't like the default anime poster? Upload and crop your own image from the gallery or camera using `android-image-cropper`. Images are efficiently compressed (Base64) and synced to the cloud.
* **Discover Trending Anime:** Fetches the top and currently trending anime dynamically.
* **Search & Add:** Seamlessly search for any anime using a custom Letterboxd-style Bottom Sheet search powered by the Jikan API.
* **Personal Watch Diary:** Log your anime with custom ratings (0.0 - 10.0), watch status, and personal reviews/notes.
* **Dynamic Layouts:** Toggle your personal diary view between a clean List layout and an aesthetic Grid layout.
* **Recycle Bin (Safe Deletion):** Deleted logs are moved to a Recycle Bin, allowing you to restore them or permanently delete them from both local storage and the cloud with a safety confirmation dialog.

## 🛠 Tech Stack & Architecture

This project is built utilizing modern Android development practices and libraries:

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Architecture:** MVVM (Model-View-ViewModel)
* **Backend as a Service (BaaS):** Firebase Authentication, Cloud Firestore
* **Local Persistence:** [Room Database](https://developer.android.com/training/data-storage/room) & DataStore Preferences
* **Network & API:** [Retrofit2](https://square.github.io/retrofit/) & **Moshi**
* **Image Loading:** [Coil Compose](https://coil-kt.github.io/coil/compose/)
* **Image Cropper:** [CanHub Android Image Cropper](https://github.com/CanHub/Android-Image-Cropper)
* **Asynchronous Programming:** Coroutines & StateFlow
* **API Provider:** [Jikan REST API](https://docs.api.jikan.moe/) (Unofficial MyAnimeList API)

## 🚀 Getting Started

To run this project locally on your machine:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/anime-bowl.git
   ```
2. **Open in Android Studio**:
  Open Android Studio, select File > Open, and choose the cloned directory.

3. **Sync the project**:
  Allow Gradle to sync and download all necessary dependencies.

4. **Run the app**:
  Select an emulator or a physical Android device and hit the Run button (Shift + F10).

## Screenshots

| Home Screen | Profile (Diary) | Search & Log |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/30173b37-8d6d-44ab-b0de-23692ad7d078" width="250" alt="Home Screen"/> | <img src="https://github.com/user-attachments/assets/055ead2e-969e-42e3-9c00-b2c160f20948" width="250" alt="Profile Diary"/> | <img src="https://github.com/user-attachments/assets/fd187d5e-52bd-4429-bbc0-946208e702d3" width="250" alt="Search and Log"/> |
| Sign-in | Log out | Guest Mode |
| <img width="1080" height="2400" alt="Screenshot_20260624_233700" src="https://github.com/user-attachments/assets/ae2795ee-f916-4f5e-adbc-0c3e25e081a9" /> | <img width="1080" height="2400" alt="Screenshot_20260624_233647" src="https://github.com/user-attachments/assets/c0177241-47d3-4e07-b5c1-73613a13aac5" /> | <img width="1080" height="2400" alt="Screenshot_20260624_233711" src="https://github.com/user-attachments/assets/a30501b4-546f-46ee-8bc0-5d9b91cfb30c" />



