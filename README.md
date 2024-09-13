# AREA
An EPITECH 3rd year project that aims to create a cross-platform web app to centralize players data from video-games API's
### Web App (using SvelteKit)
#### Creating a project
If you're seeing this, you've probably already done this step. Congrats!
```bash
# create a new project in the current directory
npm create svelte@latest
# create a new project in my-app
npm create svelte@latest my-app
```
#### Developing
Once you've created a project and installed dependencies with `npm install` (or `pnpm install` or `yarn`), start a development server:
```bash
npm run dev # or start the server and open the app in a new browser tab npm run dev -- --open
```
#### Building To create a production version of your app:
```bash
 npm run build
```
You can preview the production build with `npm run preview`.
> To deploy your app, you may need to install an [adapter](https://kit.svelte.dev/docs/adapters) for your target environment.

### Mobile App (using Kotlin Multiplaform)
This is a Kotlin Multiplatform project targeting Android, iOS. * `/composeApp` is for code that will be shared across your Compose Multiplatform applications. It contains several subfolders: - `commonMain` is for code that’s common for all targets. - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name. For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app, `iosMain` would be the right folder for such calls. * `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project. Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
