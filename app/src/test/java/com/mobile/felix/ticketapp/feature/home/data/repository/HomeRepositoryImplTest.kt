package com.mobile.felix.ticketapp.feature.home.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.home.domain.source.HomeLocalDataSource
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeRepositoryImplTest {

    @MockK
    private lateinit var localDataSource: HomeLocalDataSource

    // SUT (System Under Test)
    private lateinit var repository: HomeRepositoryImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = HomeRepositoryImpl(localDataSource, Dispatchers.Unconfined)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return events when data source returns a list`() = runTest {
        // Given
        val events = listOf(
            Event(id = 1L, name = "Festival de Verão", date = "15/02/2026", location = "SP", poster = "", price = 120.0, description = ""),
            Event(id = 2L, name = "Rock Night", date = "20/03/2026", location = "RJ", poster = "", price = 80.0, description = "")
        )
        coEvery { localDataSource.getEvents() } returns events

        // When
        val result = repository.getEvents()

        // Then
        assertEquals(events, result)
        assertEquals(2, result.size)
        coVerify(exactly = 1) { localDataSource.getEvents() }
    }

    @Test
    fun `should return empty list when data source returns no events`() = runTest {
        // Given
        coEvery { localDataSource.getEvents() } returns emptyList()

        // When
        val result = repository.getEvents()

        // Then
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { localDataSource.getEvents() }
    }

    @Test
    fun `should propagate exception when data source throws`() = runTest {
        // Given
        val exception = RuntimeException("Source unavailable")
        coEvery { localDataSource.getEvents() } throws exception

        // When
        val result = runCatching { repository.getEvents() }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Source unavailable", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { localDataSource.getEvents() }
    }
}


