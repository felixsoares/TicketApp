# Skill: Create Feature

## Full feature structure to generate
```
feature/<name>/
├── data/
│   ├── repository/   # <Name>RepositoryImpl.kt
│   ├── source/       # <Name>LocalDataSourceImpl.kt
│   └── usecase/      # Get<Name>UseCase.kt  (one file per use case)
├── di/               # <Name>Module.kt
├── domain/
│   ├── repository/   # <Name>Repository.kt  (interface)
│   └── source/       # <Name>LocalDataSource.kt  (interface)
└── presentation/
    ├── action/       # <Name>Action.kt  (sealed class)
    ├── state/        # <Name>UiState.kt  (data class)
    ├── <Name>Screen.kt
    └── <Name>ViewModel.kt
```

## Steps

1. **Core domain model** — if a new entity is needed:
   - `core/domain/model/<Model>.kt` — pure Kotlin `data class`, no Android deps
   - `core/data/local/entity/<Model>Entity.kt` — Room `@Entity`
   - `core/data/local/dao/<Model>Dao.kt` — `suspend` for one-shot, `Flow` for reactive
   - Update `AppDatabase` to include the new entity and DAO
   - Add mapper in `core/mapper/AppMapper.kt`

2. **Domain interfaces** (no Android imports)
   - `feature/<name>/domain/source/<Name>LocalDataSource.kt`
   - `feature/<name>/domain/repository/<Name>Repository.kt`

3. **Data implementations**
   - `feature/<name>/data/source/<Name>LocalDataSourceImpl.kt` — inject DAO + `CoroutineDispatcher`
   - `feature/<name>/data/repository/<Name>RepositoryImpl.kt` — inject data source + `CoroutineDispatcher`
   - `feature/<name>/data/usecase/Get<Name>UseCase.kt` — single `operator fun invoke`

4. **Presentation — MVI**
   - `action/<Name>Action.kt` — `sealed class` with all user intents
   - `state/<Name>UiState.kt` — `data class`, defaults for all fields
   - `<Name>ViewModel.kt` — `StateFlow<XUiState>` + `fun onAction(action: XAction)`
   - `<Name>Screen.kt` — stateless composable, params: `uiState` + `onAction`; use `koinViewModel()`; add `@Preview`

5. **DI** — `feature/<name>/di/<Name>Module.kt`
   ```kotlin
   val <name>Module = module {
       factory { Get<Name>UseCase(get()) }
       single<<Name>LocalDataSource> { <Name>LocalDataSourceImpl(get(), get()) }
       single<<Name>Repository> { <Name>RepositoryImpl(get(), get()) }
       viewModel { <Name>ViewModel(get()) }
   }
   ```

6. **Register** — add `<name>Module` to `MainApplication.startKoin { modules(...) }`

7. **Navigation** — add `@Serializable` route to `core/presentation/Navigation.kt` and wire composable in `NavHost`
