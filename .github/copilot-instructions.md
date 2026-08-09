# TicketApp — Copilot Instructions

## Communication
- Chat: **Portuguese (pt-BR)** | Code comments: **English** | Commits: Conventional Commits

---

## Stack
| Concern | Library | Version |
|---|---|---|
| UI | Jetpack Compose + Material 3 | BOM `2026.02.01` |
| Language | Kotlin | `2.4.0` |
| DI | Koin | `3.4.3` |
| Database | Room + KSP | `2.7.1` |
| Images | Coil (`AsyncImage`) | `3.5.0` |
| Navigation | Navigation Compose (type-safe) | `2.9.8` |

> All versions in `gradle/libs.versions.toml`. Never hardcode versions in `build.gradle.kts`.

---

## Architecture — Clean + MVI + Feature-based

```
com.mobile.felix.ticketapp/
├── core/
│   ├── data/local/       # Room: DAOs, Database, Entities
│   ├── di/               # LocalModule (DB singleton)
│   └── domain/           # Shared models — zero Android deps
├── feature/<name>/
│   ├── data/
│   │   ├── repository/   # XRepositoryImpl
│   │   ├── source/       # XLocalDataSourceImpl
│   │   └── usecase/      # One use case per file
│   ├── di/               # <Feature>Module (Koin)
│   ├── domain/
│   │   ├── repository/   # XRepository (interface)
│   │   └── source/       # XLocalDataSource (interface)
│   └── presentation/
│       ├── action/       # XAction (sealed class)
│       ├── state/        # XUiState (data class)
│       ├── XScreen.kt
│       └── XViewModel.kt
└── MainActivity.kt
```

**Hard rules:**
- Domain layer: no `Context`, `LiveData`, or Android imports
- Use cases: single `operator fun invoke` only
- ViewModels: no `Context` or `View` references
- Navigation args: IDs only, never complex objects
- Composables: stateless — hoist all state to ViewModel

---

## MVI Contract

```kotlin
sealed class XAction { data class Load(val id: String) : XAction() }

data class XUiState(val isLoading: Boolean = false, val error: String? = null)

class XViewModel(private val useCase: XUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(XUiState())
    val uiState: StateFlow<XUiState> = _uiState.asStateFlow()
    fun onAction(action: XAction) { /* ... */ }
}
```

- State → `StateFlow` (never `LiveData`), collected with `collectAsStateWithLifecycle()`
- Side effects → `SharedFlow<UiEffect>` (navigation, toasts)

---

## Koin DI

```kotlin
val featureModule = module {
    factory { XUseCase(get()) }
    single<XRepository> { XRepositoryImpl(get()) }
    viewModel { XViewModel(get()) }
}
```

- `single {}` → repositories, DAOs | `factory {}` → use cases | `viewModel {}` → ViewModels
- Register every module in `MainApplication.startKoin { modules(...) }`

---

## Room

- Entities: `XEntity` | Domain models separate, connected via mappers
- DAOs: `suspend` for one-shot, `Flow` for reactive queries

---

## Navigation

```kotlin
@Serializable data object HomeRoute
@Serializable data class TicketRoute(val eventId: String)
```

Routes defined in `core/presentation/Navigation.kt`.

---

## Compose

- No XML — Compose only
- Screen receives `uiState` + `onAction` lambda; injected with `koinViewModel()`
- Naming: `XScreen`, `XCard`, `XPreview` | Add `@Preview` to all components

---

## Tests

- Unit tests: **MockK** + `runTest` (coroutines-test) + **Turbine** (Flow assertions)
- Files: `<ClassName>Test.kt` in `src/test/`
- UI tests: Compose Test / Espresso in `src/androidTest/`

---

## Code Conventions

- Models/states → `data class` | Actions/results → `sealed class` | Routes → `object`
- No `!!` — use `?.`, `?: return`, or `requireNotNull("message")`
- Coroutines: `Dispatchers.IO` for data ops; inject dispatcher for testability
- Wrap async results in `Result<T>` or a custom `sealed class Resource<T>`

## Specialized Skills Index
For domain-specific tasks, strictly follow the rules defined in the corresponding skill files:

- **Unit Testing**: When generating unit tests or test classes, follow [.github/copilot/skills/unit-testing.md](.github/copilot/skills/unit-testing.md).
- **Architecture & Features**: When creating new features or ViewModels, follow [.github/copilot/skills/create-feature.md](.github/copilot/skills/create-feature.md).
