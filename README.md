# AREA
An EPITECH 3rd year project that aims to create a cross-platform web app to centralize players data from video-games API's

[Our production build of the web is found here.](https://area-app.vercel.app/)

You can also access you project-wide documentation [here](https://github.com/RotClub/AREA/wiki).
### Web App (using SvelteKit)
Once you have cloned the project and installed dependencies with `npm install` , start a development server:
```bash
npm run dev # or start the server and open the app in a new browser tab npm run dev -- --open
```
#### Building To create a production version of your app:
```bash
 npm run build
```
### Mobile App (using Kotlin Multiplaform)
This is a Kotlin Multiplatform project targeting Android, iOS. * `/composeApp` is for code that will be shared across your Compose Multiplatform applications. It contains several subfolders: - `commonMain` is for code that’s common for all targets. - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name. For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app, `iosMain` would be the right folder for such calls. * `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project. Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
