package com.mobile.felix.ticketapp.feature.home.presentation

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.home.domain.usecase.GetEventsUseCase
import com.mobile.felix.ticketapp.feature.home.domain.usecase.InitDatabaseUseCase
import com.mobile.felix.ticketapp.feature.home.presentation.action.HomeAction
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeViewModelTest {

    @MockK
    private lateinit var getEventsUseCase: GetEventsUseCase

    @MockK
    private lateinit var initDatabaseUseCase: InitDatabaseUseCase

    // SUT (System Under Test)
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    // Shared fake data
    private val fakeEvents = listOf(
        Event(id = 1L, name = "Festival de Verão", date = "15/02/2026", location = "SP", poster = "", price = 120.0, description = ""),
        Event(id = 2L, name = "Rock Night", date = "20/03/2026", location = "RJ", poster = "", price = 80.0, description = "")
    )

    @BeforeEach
    fun setUp() {
        // Set test dispatcher as Main before creating the ViewModel so viewModelScope uses it
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        // relaxUnitFun = true automatically handles initDatabaseUseCase.invoke() (returns Unit)
        viewModel = HomeViewModel(getEventsUseCase, initDatabaseUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should call initDatabase on init`() = runTest {
        // Given — ViewModel already created in setUp

        // Then
        coVerify(exactly = 1) { initDatabaseUseCase.invoke() }
    }

    @Test
    fun `should start with default empty uiState`() = runTest {
        // Given — ViewModel already created in setUp

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.events)
    }

    @Test
    fun `should update uiState with events when getEvents is called successfully`() = runTest {
        // Given
        coEvery { getEventsUseCase.invoke() } returns fakeEvents

        // When
        viewModel.getEvents()

        // Then
        assertEquals(fakeEvents, viewModel.uiState.value.events)
        assertFalse(viewModel.uiState.value.isLoading)
        coVerify(exactly = 1) { getEventsUseCase.invoke() }
    }

    @Test
    fun `should set isLoading true then false when getEvents completes`() = runTest {
        // Given
        val loadingStates = mutableListOf<Boolean>()
        coEvery { getEventsUseCase.invoke() } answers {
            loadingStates.add(viewModel.uiState.value.isLoading)
            fakeEvents
        }

        // When
        viewModel.getEvents()

        // Then — isLoading was true during execution, false after
        assertTrue(loadingStates.first())
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `should update uiState with events when GetEvents action is dispatched`() = runTest {
        // Given
        coEvery { getEventsUseCase.invoke() } returns fakeEvents

        // When
        viewModel.onAction(HomeAction.GetEvents)

        // Then
        assertEquals(fakeEvents, viewModel.uiState.value.events)
        assertFalse(viewModel.uiState.value.isLoading)
        coVerify(exactly = 1) { getEventsUseCase.invoke() }
    }

    @Test
    fun `should not call getEventsUseCase when Idle action is dispatched`() = runTest {
        // Given — no setup needed

        // When
        viewModel.onAction(HomeAction.Idle)

        // Then
        coVerify(exactly = 0) { getEventsUseCase.invoke() }
        assertNull(viewModel.uiState.value.events)
    }

    @Test
    fun `should return empty events list when use case returns empty`() = runTest {
        // Given
        coEvery { getEventsUseCase.invoke() } returns emptyList()

        // When
        viewModel.getEvents()

        // Then
        assertTrue(viewModel.uiState.value.events!!.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        coVerify(exactly = 1) { getEventsUseCase.invoke() }
    }
}


