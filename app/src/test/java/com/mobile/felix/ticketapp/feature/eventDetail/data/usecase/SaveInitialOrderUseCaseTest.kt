package com.mobile.felix.ticketapp.feature.eventDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.eventDetail.domain.repository.EventDetailRepository
import com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase.SaveInitialOrderUseCase
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

class SaveInitialOrderUseCaseTest {

    @MockK
    private lateinit var repository: EventDetailRepository

    // SUT (System Under Test)
    private lateinit var useCase: SaveInitialOrderUseCase

    private val fakeEvent = Event(
        id = 1L, name = "Festival de Verão", date = "15/02/2026",
        location = "SP", poster = "", price = 120.0, description = ""
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = SaveInitialOrderUseCase(repository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return generated order id when repository saves initial order`() = runTest {
        // Given
        val expectedOrderId = 42L
        coEvery { repository.saveInitialOrder(fakeEvent, 2) } returns expectedOrderId

        // When
        val result = useCase.invoke(fakeEvent, 2)

        // Then
        assertEquals(expectedOrderId, result)
        coVerify(exactly = 1) { repository.saveInitialOrder(fakeEvent, 2) }
    }

    @Test
    fun `should propagate exception when repository throws on saveInitialOrder`() = runTest {
        // Given
        val exception = RuntimeException("Save failed")
        coEvery { repository.saveInitialOrder(any(), any()) } throws exception

        // When
        val result = runCatching { useCase.invoke(fakeEvent, 1) }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Save failed", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { repository.saveInitialOrder(any(), any()) }
    }
}

