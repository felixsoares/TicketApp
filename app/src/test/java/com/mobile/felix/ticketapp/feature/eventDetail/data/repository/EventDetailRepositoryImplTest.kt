package com.mobile.felix.ticketapp.feature.eventDetail.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.eventDetail.domain.source.EventDetailLocalDataSource
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EventDetailRepositoryImplTest {

    @MockK
    private lateinit var localDataSource: EventDetailLocalDataSource

    // SUT (System Under Test)
    private lateinit var repository: EventDetailRepositoryImpl

    private val fakeEvent = Event(
        id = 1L, name = "Festival de Verão", date = "15/02/2026",
        location = "SP", poster = "", price = 120.0, description = ""
    )

    private val fakeOrder = Order(
        id = 10L, eventId = 1L, eventName = "Festival de Verão",
        eventDate = "15/02/2026", price = 240.0, purchaseDate = "10/01/2026",
        ticketQuantity = 2, status = OrderStatus.WAITING_PAYMENT
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = EventDetailRepositoryImpl(localDataSource, Dispatchers.Unconfined)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return event when data source returns event by id`() = runTest {
        // Given
        coEvery { localDataSource.getEventById(1L) } returns fakeEvent

        // When
        val result = repository.getEventById(1L)

        // Then
        assertEquals(fakeEvent, result)
        coVerify(exactly = 1) { localDataSource.getEventById(1L) }
    }

    @Test
    fun `should return order when data source returns order by event id`() = runTest {
        // Given
        coEvery { localDataSource.getOrderByEventId(1L) } returns fakeOrder

        // When
        val result = repository.getOrderByEventId(1L)

        // Then
        assertNotNull(result)
        assertEquals(fakeOrder, result)
        coVerify(exactly = 1) { localDataSource.getOrderByEventId(1L) }
    }

    @Test
    fun `should return null when data source returns no order for event id`() = runTest {
        // Given
        coEvery { localDataSource.getOrderByEventId(99L) } returns null

        // When
        val result = repository.getOrderByEventId(99L)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { localDataSource.getOrderByEventId(99L) }
    }

    @Test
    fun `should return generated id when data source saves initial order`() = runTest {
        // Given
        val expectedId = 42L
        coEvery { localDataSource.saveInitialOrder(fakeEvent, 2) } returns expectedId

        // When
        val result = repository.saveInitialOrder(fakeEvent, 2)

        // Then
        assertEquals(expectedId, result)
        coVerify(exactly = 1) { localDataSource.saveInitialOrder(fakeEvent, 2) }
    }

    @Test
    fun `should propagate exception when data source throws on getEventById`() = runTest {
        // Given
        val exception = RuntimeException("Event unavailable")
        coEvery { localDataSource.getEventById(any()) } throws exception

        // When
        val result = runCatching { repository.getEventById(1L) }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Event unavailable", result.exceptionOrNull()?.message)
    }
}

