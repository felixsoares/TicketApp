# Skill: Kotlin Unit Testing with MockK & JUnit 5

## Naming Conventions & Structure

- **Test Method Naming**: Use backticks (`` `...` ``) following the exact BDD pattern:
  `should [expected behavior] when [scenario]`
  *Example:* `` `should return success state when payment is processed successfully`() ``
- **Test Body Arrangement**: Divide the test logic into three explicitly commented phases:
  `// Given`, `// When`, `// Then`.
- **Framework Stack**:
    - Test Runner & Annotations: **JUnit 5** (`org.junit.jupiter.api.*`)
    - Mocking & Verification: **MockK** (`io.mockk.*`)
    - Coroutine Testing: **Kotlinx Coroutines Test** (`kotlinx.coroutines.test.runTest`)

## Lifecycle Management (Setup & Teardown)

- **Setup (`@BeforeEach`)**: Initialize MockK annotations (`MockKAnnotations.init(this, relaxUnitFun = true)`) and instantiate the SUT (System Under Test) with mock dependencies.
- **Teardown (`@AfterEach`)**: Reset mocks and clear state using `clearAllMocks()` and `unmockkAll()` to ensure test isolation across executions.

## MockK Execution Rules

- Use `coEvery` and `coVerify` for `suspend` functions.
- Use `every` and `verify` for standard blocking/synchronous functions.
- Always explicitly verify call counters where applicable (e.g., `coVerify(exactly = 1)` or `coVerify(exactly = 0)`).

---

## Canonical Reference Template

```kotlin
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SampleViewModelTest {

    @MockK
    private lateinit var repository: SampleRepository

    // SUT (System Under Test)
    private lateinit var viewModel: SampleViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = SampleViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return success when repository returns valid data`() = runTest {
        // Given
        val expectedData = "Data"
        coEvery { repository.fetchData() } returns Result.success(expectedData)

        // When
        val result = viewModel.loadData()

        // Then
        assertEquals(Result.success(expectedData), result)
        coVerify(exactly = 1) { repository.fetchData() }
    }

    @Test
    fun `should throw exception when repository fails`() = runTest {
        // Given
        val exception = RuntimeException("Network Error")
        coEvery { repository.fetchData() } throws exception

        // When & Then
        runCatching { viewModel.loadData() }
            .onFailure { error ->
                assertEquals("Network Error", error.message)
            }

        coVerify(exactly = 1) { repository.fetchData() }
    }
}