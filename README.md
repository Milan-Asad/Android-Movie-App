ANDROID MOVIE (TMDB) APP

## Android take home test for Yassir

DESCRIPTION:
App fetches data from API and displays it in a card.

TECH STACK:
- Kotlin
- Jetpack Compose
- Coroutines
- Retrofit
- LiveData
- MVVM architecture (using ViewModel)

EXPLANATION:
- I’m using the MVVM architecture for this app to ensure a clear separation of concerns. This separation makes the app maintainable both in the short term and the long term, making the development more efficent.
- The app is created with a dark mode by default, as many popular apps (such as Netflix, Amazon Prime and NOW TV) have dark themes. Additionally, dark mode would suit those who watch videos in the night time, making it visually appealing.
- The movies are displayed in a card. The app also has a backstack button in the movie overview section (when you press on a movie).
- Coroutines are used for asynchronous operations to handle background tasks efficiently without blocking the main thread. This prevents the app from crashing and ensures a smooth user experience.
- A loading icon is added to show the user the movies are being loaded.


IMAGES:

![image](https://github.com/Milan-Asad/AndroidMovieApp/assets/79909176/1994a11e-ece2-4dfb-9fd3-7d413093a32d)
![image](https://github.com/Milan-Asad/AndroidMovieApp/assets/79909176/9338e6c4-82db-4f83-b51a-8856087085b7)
![image](https://github.com/Milan-Asad/AndroidMovieApp/assets/79909176/16d65b71-351a-4bbd-8071-4147069ce78a)


IMAGE 1: Home screen

IMAGE 2: Detailed overview screen

IMAGE 3: Image loading icon (inspired from NOW TV)

App was tested using the Google Pixel 4XL (latest Android version)

