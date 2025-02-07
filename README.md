# DataSyncApproaches

A single Android project demonstrating **three** distinct ways to handle data synchronization and storage in Android:

1. **Offline-First**: Store data locally first (e.g., using Room) and sync with the server when online.  
2. **Remote-First**: Send data to the server immediately, then cache locally.  
3. **Hybrid**: Attempt real-time server updates, but fall back to local storage if offline.

This repo also covers **error handling** (e.g., catching `HttpException`), **Room** for local storage, **Retrofit** for networking, **WorkManager** for background sync, and the **MVVM** architectural pattern, with **Hilt** for dependency injection.

---

## Features

- **Offline-First** example (like a to-do list) that stores data locally via Room and syncs with a backend service using WorkManager.  
- **Remote-First** example (like a social media feed) that sends data to the server first, then caches it locally.  
- **Hybrid** example (like a banking app) that attempts immediate server sync but queues unsynced data if offline.  
- **Error Handling**: Demonstrates how to avoid crashes on HTTP errors (e.g. 403, 404) by catching exceptions and returning sealed results.  
- **Modular**: Each approach is showcased in a separate repository class for easy comparison.

---

 
**Key directories**:
- **`data/local`**: Room entities, DAO interfaces, local DB setup.  
- **`data/remote`**: API service definitions (Retrofit), network data source.  
- **`repository`**: The three data sync approaches.  
- **`ui`**: Compose screens & ViewModels for each approach.  
- **`worker`**: WorkManager classes for background syncing.

---

## Approaches in Detail

### Offline-First

1. **Local-first**: Data is written to Room immediately (`isSynced = false`).  
2. **Background sync**: Once a connection is detected, WorkManager or a sync function pushes unsynced data to the server.  
3. **Mark synced**: After successful response, update `isSynced = true`.

**Pros**: Great for offline availability, quick local writes.  
**Cons**: Requires more complex conflict resolution if multiple devices change data offline.

---

### Remote-First

1. **Server-first**: The app calls the server (via Retrofit) to create or update data.  
2. **Local cache**: On success, the result is cached in Room for quick reads or offline display.  
3. **Consistency**: The server is always the source of truth, so local data is considered secondary.

**Pros**: Real-time multi-device sync, straightforward data validation.  
**Cons**: Limited offline functionality unless you add fallback logic.

---

### Hybrid

1. **Attempt** immediate server sync.  
2. If offline or the request fails, store data locally (`isSynced = false`).  
3. **Background** sync tries again until everything is up to date.  
4. **Conflict resolution** is crucial if data can change in multiple places.

**Pros**: Best of both worlds—near-real-time updates plus offline resilience.  
**Cons**: The most complex approach, requiring robust error handling.

---

## Technologies Used

- **Kotlin**, **Jetpack Compose**  
- **Room** (local DB)  
- **Retrofit** (network calls)  
- **WorkManager** (background tasks)  
- **Hilt** (dependency injection)  
- **MVVM** (architecture pattern)  
- **Coroutines** / **Flow** (asynchronous operations & state management)

---

## How to Run

1. **Clone** the repo:
   ```bash
   git clone https://github.com/DigitalYogi1992/DataSyncApproaches.git
   cd DataSyncApproaches

2. Open in Android Studio.
3. Sync Gradle.
4. Run on an emulator or physical device.
5. **In MainActivity.kt, swap out the screens:** 
   ```Kotlin
   setContent {
    DataSyncSampleTheme {
        // Choose which approach to see:
        // OfflineFirstScreen()
        // RemoteFirstScreen()
        HybridScreen()
    }}


6. Observe how each approach handles local vs. remote data in real time.


##📌 Read the blog here: [https://medium.com/@shivayogih25/android-data-sync-approaches-offline-first-remote-first-hybrid-done-right-c4d065920164]

🔗 Let’s discuss! How do you handle data sync in your apps? Drop your thoughts in the comments! 💬👇
 

