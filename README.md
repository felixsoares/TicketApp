# 🎟️ TicketApp

Aplicativo Android para visualização de eventos, gerenciamento de pedidos e compra de ingressos via integração de pagamento.

---

## 🚀 Tecnologias & Versões

| Categoria | Tecnologia | Versão |
|---|---|---|
| **Linguagem** | Kotlin | `2.4.0` |
| **UI** | Jetpack Compose (Material 3) | BOM `2026.02.01` |
| **Arquitetura** | Clean Architecture + MVI | — |
| **Injeção de Dependência** | Koin | `3.4.3` |
| **Banco de Dados** | Room + KSP | `2.7.1` |
| **Navegação** | Navigation Compose (Type-safe) | `2.9.8` |
| **Imagens** | Coil 3 | `3.5.0` |
| **Testes** | JUnit 5 + MockK + Coroutines Test | `5.12.0` / `1.14.11` / `1.10.0` |
| **Build System** | AGP + KSP + Version Catalog | `9.2.1` / `2.3.9` |

---

## 🏗️ Arquitetura & Padrões

O projeto é organizado por **features** seguindo os princípios de **Clean Architecture** e **MVI** (Model-View-Intent).

### MVI (Model-View-Intent)
- **UiState**: `StateFlow` imutável consumido via `collectAsStateWithLifecycle()`.
- **UiAction**: `sealed interface` representando interações do usuário.
- **UiEffect**: `Channel` / `Flow` para efeitos colaterais únicos (ex: navegação DeepLink).

### Estrutura de Pastas
```
com.mobile.felix.ticketapp/
├── core/
│   ├── data/ (local/Room, payment, mappers)
│   ├── domain/ (modelos compartilhados: Event, Order, OrderStatus)
│   ├── di/ (LocalModule)
│   └── presentation/ (Navigation, rotas @Serializable, componentes comuns)
└── feature/
    ├── home/
    ├── eventDetail/
    ├── ticketDetail/
    ├── tickets/
    └── receipt/
        ├── data/ (repository, source, usecases)
        ├── di/ (FeatureModule)
        ├── domain/ (interfaces de repository e source)
        └── presentation/ (Action, State, ViewModel, Screen)
```

---

## 💳 Integração de Pagamento & DeepLink

- **SDK Lio/Cielo**: Integração para processamento de pagamentos via DeepLink (`lio://payment?...`).
- **Foreground Service**: `DeepLinkService` mantém o app ativo durante o fluxo externo de pagamento.
- **Tratamento de Callback**: `MainActivity` recebe o retorno via `onNewIntent` (`order://response?...`), descriptografa o payload Base64 e atualiza o estado do pedido (`APPROVED`, `DENIED`, `CANCELLED`).

---

## 🧪 Testes Unitários

- **Runner**: JUnit 5 (`useJUnitPlatform()`).
- **Mocks**: MockK (`coEvery`, `coVerify`, `@MockK`).
- **Corrotinas**: `kotlinx-coroutines-test` (`runTest`, `UnconfinedTestDispatcher`).
- **BDD**: Nomenclatura em backticks `` `should [behavior] when [scenario]` `` e estrutura **Given / When / Then**.
