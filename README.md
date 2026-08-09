# 🎟️ TicketApp

Aplicativo Android para visualização de eventos, compra de ingressos e acompanhamento de pedidos com integração ao terminal **Cielo Smart (Lio)**.

---

## 🛠️ Instruções de Execução

### Pré-requisitos
- **Android Studio**: Ladybug / Jellyfish (ou mais recente) com JDK 17+
- **Android SDK**: `minSdk 29` (Android 10), `compileSdk / targetSdk 36`
- **Dispositivo**: Terminal Cielo Lio / Cielo Smart ou Emulador Android (para telas sem suporte a pagamento físico)

### Passos para rodar
1. **Clonar o repositório**:
   ```bash
   git clone https://github.com/felixsoares/TicketApp.git
   cd TicketApp
   ```
2. **Sincronizar dependências no Gradle**:
   - Certifique-se de que os arquivos AAR de pagamento (`order-manager-2.7.2.aar` e `event-tracker-1.0.1.aar`) estejam presentes na pasta `app/libs/`.
3. **Compilar e Executar**:
   - Selecione a variante `debug` e execute no dispositivo/emulador:
     ```bash
     ./gradlew assembleDebug
     ```
4. **Executar os Testes Unitários**:
   ```bash
   ./gradlew test
   ```

---

## 🚀 Tecnologias & Bibliotecas Utilizadas

| Biblioteca | Versão | Justificativa |
|---|---|---|
| **Kotlin** | `2.4.0` | Linguagem moderna com suporte nativo a Coroutines e Flow. |
| **Jetpack Compose + Material 3** | BOM `2026.02.01` | Toolkit declarativo para UI moderna, sem XML, com componentes M3. |
| **Koin** | `3.4.3` | DI leve e idiológica para Kotlin/Android com suporte nativo a `viewModel` e Compose. |
| **Room + KSP** | `2.7.1` | Persistência local segura com queries reativas via Coroutines/Flow e KSP para build rápido. |
| **Navigation Compose** | `2.9.8` | Navegação reativa e *type-safe* com Kotlin Serialization (`@Serializable`). |
| **Coil 3** | `3.5.0` | Carregamento assíncrono e performático de imagens com suporte a OkHttp e Compose. |
| **Gson** | `2.14.0` | Serialização e deserialização simples de JSON para comunicação Base64 com Cielo Lio. |
| **JUnit 5 + MockK** | `5.12.0` / `1.14.11` | Stack moderna de testes com suporte BDD, mocks assíncronos e runner JUnit Platform. |

---

## 🏗️ Decisões Arquiteturais

A aplicação adota **Clean Architecture** combinada com **MVI (Model-View-Intent)** e modularização interna baseada em **Features**:

1. **MVI (Model-View-Intent)**:
   - **UiState**: Objeto imutável reativo (`StateFlow`) consumido na UI com `collectAsStateWithLifecycle()`.
   - **UiAction**: `sealed interface` que centraliza as intenções do usuário.
   - **UiEffect**: `Channel` / `Flow` de disparo único (*single event*) para side-effects (ex: disparar DeepLink da Cielo).

2. **Isolamento de Camadas (Clean Architecture)**:
   - **Domain**: Zero dependências de framework Android (`Context`, `LiveData`). Modelos puros e interfaces de repositório.
   - **Data**: Repositórios e fontes de dados locais (`RoomDao`), com conversão via Mappers para entidades de domínio.
   - **Presentation**: ViewModels desacoplados de Views/Context, injetados via `koinViewModel()`.

3. **Estrutura por Feature**:
   - Cada funcionalidade (`home`, `eventDetail`, `ticketDetail`, `tickets`) contém suas subpastas `data`, `domain`, `presentation` e `di`, garantindo coesão e independência.

---

## 💳 Integração com a Cielo Smart (Lio)

A comunicação com o aplicativo de pagamento da Cielo Lio foi implementada via **DeepLink bidirecional** e **Foreground Service**:

1. **Requisição de Pagamento (`PaymentRequestUseCase`)**:
   - O objeto `Order` é convertido em JSON via `Gson`, codificado em **Base64** e anexado à URI do esquema da Cielo (`lio://payment?request=<base64>&urlCallback=<scheme_callback>`).

2. **Garantia de Execução em Background (`DeepLinkService`)**:
   - Antes de disparar o Intent da Cielo, o `DeepLinkService` (Foreground Service do tipo `dataSync`) é iniciado para impedir que o sistema destrua o processo do TicketApp enquanto o app da Cielo está em primeiro plano.

3. **Retorno do Pagamento (`MainActivity` / `onNewIntent`)**:
   - A `MainActivity` configurada com `launchMode="singleTask"` e filtro `<data android:scheme="order" android:host="response" />` captura o retorno da Cielo via `onNewIntent`.
   - O payload do parâmetro `response` é decodificado de Base64 para JSON e o status do pedido é atualizado via `UpdateOrderStatusUseCase` (`APPROVED`, `DENIED`, `CANCELLED`).
   - O `DeepLinkService` é finalizado logo após o recebimento do callback.

---

## ⚖️ Trade-offs Considerados

- **Room / Local Database para Simulação**:
  - *Decisão*: Como não há um backend real, a persistência e o mock de pedidos foram feitos localmente no Room com seed em tempo de execução (`InitDatabaseUseCase`).
  - *Trade-off*: Aumentou a complexidade inicial de configuração do banco, mas garantiu um fluxo fim a fim persistente e reativo.

- **DeepLink vs. SDK Direto**:
  - *Decisão*: Utilização da URI de pagamento (`lio://payment`) combinada com SDK local em `.aar`.
  - *Trade-off*: Dependência de esquemas externos do dispositivo físico da Cielo, contornada com tratamento defensivo no recebimento do callback (`deserializeQueryParameter`).

- **MVI com Single `UiState` + `SharedFlow` de Ações**:
  - *Decisão*: Centralizar ações em um `MutableSharedFlow<Action>()` processado sequencialmente no `viewModelScope`.
  - *Trade-off*: Requer tratamento explícito de estados intermediários, mas evita *race conditions* em chamadas assíncronas paralelas.

---

## 🔮 O Que Faria Com Mais Tempo

1. **Testes de UI / UI Automator**:
   - Implementar testes end-to-end com **Compose Test** e **UI Automator** simulando a navegação entre telas e o callback de DeepLink.
2. **Tratamento Offline Avançado & Sincronização em Nuvem**:
   - Integrar um backend remoto (ex: Firebase ou API REST Ktor) com sincronização em segundo plano via WorkManager.
3. **Suporte a Múltiplos Métodos de Pagamento**:
   - Abstrair o provedor de pagamento em uma interface genérica (`PaymentProvider`) para permitir fácil chaveamento entre Cielo, Pix, Cartão de Crédito e Carteiras Digitais.
4. **Design System Dedicado & Animações**:
   - Criar tokens de design reutilizáveis no Compose, suporte a modo escuro/claro e transições de tela fluidas (*Shared Element Transitions*).

---

## 🤖 Aceleração de Desenvolvimento com IA (Copilot Instructions & Skills)

O projeto foi totalmente impulsionado pelo uso de IA com **GitHub Copilot**, utilizando **Custom Instructions** e **Skills** padronizadas no repositório para garantir consistência arquitetural, alta qualidade de código e aceleração de entregas.

### 📄 Arquivos de Configuração de IA
- **`.github/copilot-instructions.md`**: Instruções centrais enxutas e de alta densidade da stack (Compose, MVI, Clean Architecture, Koin, Room). Define regras estritas (ex: domain sem dependências Android, ViewModels desacoplados) e padrões de comunicação.
- **`.github/copilot/skills/create-feature.md`**: Skill com passo a passo estruturado (7 etapas) para geração automatizada de novas features seguindo rigorosamente a arquitetura do projeto.
- **`.github/copilot/skills/unit-testing.md`**: Skill de testes unitários definindo padrões BDD (`should [behavior] when [scenario]`), padrão Given/When/Then, JUnit 5, MockK e `runTest`.

---

## 💡 Momentos Marcantes e Resultados com Uso de IA

| Momento / Desafio | Ação da IA & Recursos Utilizados | Resultado Obtido |
|---|---|---|
| **Otimização das Instruções** | Redução e otimização do `.github/copilot-instructions.md` mantendo 100% da precisão das regras. | Redução de ~197 para ~80 linhas (~60% de economia de tokens por prompt), acelerando respostas e evitando estourar a janela de contexto. |
| **Aprimoramento da Skill `create-feature.md`** | Correção e completude do checklist de 7 passos para criação de features, incluindo template Koin e injeções explícitas. | Criação de um padrão reprodutível e livre de ambiguidades para qualquer nova funcionalidade. |
| **Criação da Feature `cart`** | Aplicação direta da skill `create-feature.md` para criar a tela de pedidos e carrinho. | Geração completa em minutos das camadas: `Order`, `OrderStatus`, `OrderEntity`, `OrderDao`, Data Source, Repository, Use Cases, Koin Module, ViewModel e UI `CartScreen` com chips coloridos por status. |
| **Criação da Feature `receipt`** | Geração da feature de comprovante detalhado do pedido navegável a partir da `CartScreen`. | Implementação completa da tela de comprovante com separação visual de dados do evento, pagamento e número do pedido. |
| **Diagnóstico de DeepLink (`onNewIntent`)** | Análise da falha no recebimento do callback da Cielo Lio. | Identificação de que o `<intent-filter>` de DeepLink necessitava ser separado do filtro `LAUNCHER` no `AndroidManifest.xml` com `<data android:scheme="order" android:host="response" />`, reestabelecendo a navegação de retorno. |
| **Geração de 56 Testes Unitários** | Aplicação da skill `unit-testing.md` para criar testes em `home` (16 testes), `eventDetail` e `ticketDetail` (40 testes). | Cobertura total das camadas de ViewModel, Use Case, Repository e Data Source com BDD, MockK e corrotinas. |
| **Correção de Timeout em Teste de Flow** | Diagnóstico de `UncompletedCoroutinesError` no teste de disparo de DeepLink (`LaunchCieloApp`). | Substituição de `flow.collect { ... }` (coleta infinita) por `flow.first()` (coleta finita com `take(1)`), eliminando o travamento do teste após 1m. |
