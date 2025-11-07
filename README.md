# BookBrowser — README


## How to run the tests

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




## How to run the app

Using Android Studio
- Open the project in Android Studio (recommended).
- Build and run on an emulator or device via the Run button.

From the command-line
- Build and install the debug APK to a device/emulator:

```bash
# build and install
./gradlew :app:installDebug --console=plain

```


## Caching approach and architecture used

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

## High-level architecture
- Layers:
  - UI: Compose screens (e.g., `BookListScreen`, `BookList` component, `LoadingProgress`, `ErrorScreen`, `BookCard`).
  - ViewModel: `BookListViewModel` exposes `uiState: StateFlow<BookListUiState>` and maps Resource<> -> UiState.
  - Domain/usecase: `GetScienceFictionBooksUseCase` returns Flow<Resource<List<Book>>> from the repository.
  - Data/Repository: `BookRepositoryImpl` (network + Room) implements `BookRepository` interface.
  - DI: Hilt (`DataModule`, `DispatcherModule`) wires implementations and dispatchers.

Principles applied
- Unidirectional Data Flow (UDF):
  - Data flows one-way: the repository/use-case layer emits a `Flow<Resource<List<Book>>>`, the `ViewModel` collects it and maps it to a UI state (`BookListUiState`), and the UI observes that state via `collectAsStateWithLifecycle()`.
  - UI events (if any) are forwarded to the `ViewModel` as intents/commands — the `ViewModel` is the single source that mutates state. 
- Separation of Concerns:
  - UI layer only renders state and exposes user intents.
  - `ViewModel` handles orchestration, state mapping, and small presentation logic.
  - Use cases encapsulate business rules and orchestrate repository calls.
  - Repository is responsible for data access, caching, and error recovery (network <-> local DB).
  - This separation keeps components small, focused, and easily testable.
- Dependencies point inward (Dependency Rule):
  - Inner layers (domain/use-cases, UI models) do not depend on Android or framework concerns; concrete implementations (Room, Retrofit, Dispatchers) are provided to outer layers via interfaces and Hilt.
  - `BookRepository` is an interface in the domain/data boundary; `BookRepositoryImpl` (with Room/Retrofit) is injected at runtime. Tests/fakes can provide alternate implementations without touching inner layers.

