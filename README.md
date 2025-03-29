# TMDBApp

TMDBApp is an Android application that allows users to browse movies and view their details using the TMDB (The Movie Database) API. The app provides movie lists, details, and other related information.

## Description

This is an Android app that fetches movie data from TMDB's API. It features:
- A list of movies fetched from the API
- Detailed information about each movie when clicked
- Pagination for loading movies in batches
- Error handling and retry logic for network requests

## Technologies Used

- **Kotlin**: Primary programming language for Android development.
- **Jetpack Compose**: For building modern UIs.
- **Retrofit**: For making network calls and parsing JSON responses.
- **Paging 3**: For loading large datasets incrementally.
- **Hilt**: For dependency injection.
- **Room**: For local data storage (if implemented in future versions).
- **Glide**: For image loading.

## Setup & Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/TMDBApp.git
    ```

2. Open the project in **Android Studio**.

3. If you are using a physical device or emulator, make sure to set up the **Android SDK** and **JDK** correctly in Android Studio.

4. Make sure to add your **TMDB API key** in the `local.properties` file:
    ```
    tmdb_api_key=YOUR_API_KEY
    ```

5. **Sync Gradle** and run the project.

## How to Run the App

1. **Build the project**: Go to `Build > Make Project` in Android Studio to compile the code.

2. **Run the project**: Select an emulator or a connected device, then click `Run` or use `Shift + F10` to launch the app.

3. The app should now launch on your emulator or physical device.

## Folder Structure

The project follows a standard Android architecture and folder structure: