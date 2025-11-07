# BookBrowser — README


How to run the tests

Prerequisites
- JDK 11 installed
- Android SDK and an emulator (or a connected device)
- A working `adb` on your PATH
- Prefer Android Studio for a simpler experience (it will install/launch emulators for you)

Run unit tests (fast, JVM tests):

```bash
# run all unit tests
./gradlew test --console=plain

```

Run instrumentation tests (androidTest) — require an emulator or device:

```bash
# run all connected instrumentation tests (slow)
./gradlew :app:connectedDebugAndroidTest --console=plain


```



How to run the app

Using Android Studio
- Open the project in Android Studio (recommended).
- Build and run on an emulator or device via the Run button.

From the command-line
- Build and install the debug APK to a device/emulator:

```bash
# build and install
./gradlew :app:installDebug --console=plain

```


Caching approach and architecture used

Caching approach (implemented in repository)
- The app follows a simple network + local cache pattern (a lightweight NetworkBoundResource variant):
  - `BookRepositoryImpl` fetches data from the remote API (via `BookApiService`) inside an IO dispatcher (`@IoDispatcher`), maps DTOs to domain models, and writes entities to the local Room DB via `bookDao.insertBooks(...)`.
  - On success, the repository emits `Resource.Success(books)`.
  - On error, the repository calls `getCachedOrError(e)` which reads from the database: if cached entries exist it returns `Resource.Success(cached)`, otherwise `Resource.Error(message)`.
- Files to inspect:
  - `app/src/main/java/com/jukti/bookbrowser/data/repository/BookRepositoryImpl.kt` — the real repository implementation with DB writes and error handling.
  - `app/src/main/java/com/jukti/bookbrowser/data/local/dao/BookDao.kt` and `app/src/main/java/com/jukti/bookbrowser/data/local/BookDatabase.kt` — Room DAO & DB.
  - `app/src/main/java/com/jukti/bookbrowser/domain/model/Resource.kt` — sealed class used to express Loading/Success/Error states.

Why this approach
- Benefits:
  - Users can see cached data even when offline.
  - Network responses are persisted for faster subsequent launches.
- Limitations:
  - No sophisticated freshness/expiry policy is implemented (simple overwrite on fetch).
  - No background refresh syncing; fetch happens only when the repository method is called.

High-level architecture
- Layers:
  - UI: Compose screens (e.g., `BookListScreen`, `BookList` component, `LoadingProgress`, `ErrorScreen`, `BookCard`).
  - ViewModel: `BookListViewModel` exposes `uiState: StateFlow<BookListUiState>` and maps Resource<> -> UiState.
  - Domain/usecase: `GetScienceFictionBooksUseCase` returns Flow<Resource<List<Book>>> from the repository.
  - Data/Repository: `BookRepositoryImpl` (network + Room) implements `BookRepository` interface.
  - DI: Hilt (`DataModule`, `DispatcherModule`) wires implementations and dispatchers.


Trade-offs or known issues

1) Test determinism / flakiness
- Tests that observe asynchronous flows and UI recompositions can be flaky if the test does not control timing precisely.
- The current approach uses a test fake (`FakeBookRepository`) with `holdEmission()` / `releaseEmission()` so tests can assert the Loading state deterministically. However this pattern requires:
  - The test Hilt module replacement (`TestDataModule`) must be applied; tests should be annotated with `@UninstallModules(DataModule::class)`.
  - The test runner must be the Hilt test runner (`HiltTestRunner`) so injection and test modules work.
- If tests still flake you can:
  - Ensure the fake has no artificial delay during instrumentation tests (set `fake.delayInMillis = 0` in the test module), or
  - Adopt injected `TestCoroutineDispatcher`/`TestCoroutineScheduler` approach for deterministic coroutine scheduling in tests (more work but robust). See comments in code where dispatcher modules are present.

2) Caching trade-offs
- The repository stores responses into Room and reads cached data on errors; this is simple, reliable, and good for offline UX.
- Trade-offs:
  - No TTL or freshness policy — cached data may be stale indefinitely.
  - No synchronization strategy when network and DB diverge (no conflict resolution or partial updates).
  - No background sync or WorkManager integration (could be added later).

3) Error messages & UX
- Errors are currently surfaced via `Resource.Error` with a plain message. For production, map errors to localized user-facing messages, and provide Retry actions in the UI.

4) Dependency & test runner details
- Tests rely on Hilt test runner and Hilt test modules. If you see injection issues in tests, verify `testInstrumentationRunner` in `app/build.gradle.kts` is set to `com.jukti.bookbrowser.HiltTestRunner` and `HiltTestRunner` is present in `src/androidTest`.

5) Compose test library versions
- If you change Compose test dependencies (manual override vs BOM-driven), ensure the runtime and test versions are compatible. Mismatched versions can cause `createAndroidComposeRule(..., launchActivity = false)` overload absence or other API differences.


