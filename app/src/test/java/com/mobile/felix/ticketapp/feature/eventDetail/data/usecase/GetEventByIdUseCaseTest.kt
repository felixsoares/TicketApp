package com.mobile.felix.ticketapp.feature.eventDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.eventDetail.domain.repository.EventDetailRepository
import com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase.GetEventByIdUseCase
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

class GetEventByIdUseCaseTest {

    @MockK
    private lateinit var repository: EventDetailRepository

    // SUT (System Under Test)
    private lateinit var useCase: GetEventByIdUseCase

    private val fakeEvent = Event(
        id = 1L, name = "Festival de Verão", date = "15/02/2026",
        location = "SP", poster = "", price = 120.0, description = ""
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = GetEventByIdUseCase(repository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return event when repository returns event by id`() = runTest {
        // Given
        coEvery { repository.getEventById(1L) } returns fakeEvent

        // When
        val result = useCase.invoke(1L)

        // Then
        assertEquals(fakeEvent, result)
        assertEquals("Festival de Verão", result.name)
        coVerify(exactly = 1) { repository.getEventById(1L) }
    }

    @Test
    fun `should propagate exception when repository throws on getEventById`() = runTest {
        // Given
        val exception = RuntimeException("Event not found")
        coEvery { repository.getEventById(any()) } throws exception

        // When
        val result = runCatching { useCase.invoke(99L) }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Event not found", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { repository.getEventById(99L) }
    }
}

