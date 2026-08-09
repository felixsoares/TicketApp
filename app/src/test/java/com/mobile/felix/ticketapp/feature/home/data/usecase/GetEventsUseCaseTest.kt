package com.mobile.felix.ticketapp.feature.home.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.home.domain.repository.HomeRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEventsUseCaseTest {

    @MockK
    private lateinit var repository: HomeRepository

    // SUT (System Under Test)
    private lateinit var useCase: GetEventsUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = GetEventsUseCase(repository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return events list when repository returns data`() = runTest {
        // Given
        val events = listOf(
            Event(id = 1L, name = "Rock Night", date = "20/03/2026", location = "SP", poster = "", price = 180.0, description = ""),
            Event(id = 2L, name = "Stand-up Comedy", date = "05/06/2026", location = "RJ", poster = "", price = 50.0, description = "")
        )
        coEvery { repository.getEvents() } returns events

        // When
        val result = useCase.invoke()

        // Then
        assertEquals(events, result)
        assertEquals(2, result.size)
        coVerify(exactly = 1) { repository.getEvents() }
    }

    @Test
    fun `should return empty list when repository returns no events`() = runTest {
        // Given
        coEvery { repository.getEvents() } returns emptyList()

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getEvents() }
    }

    @Test
    fun `should propagate exception when repository throws`() = runTest {
        // Given
        val exception = RuntimeException("Repository failure")
        coEvery { repository.getEvents() } throws exception

        // When
        val result = runCatching { useCase.invoke() }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Repository failure", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { repository.getEvents() }
    }
}


