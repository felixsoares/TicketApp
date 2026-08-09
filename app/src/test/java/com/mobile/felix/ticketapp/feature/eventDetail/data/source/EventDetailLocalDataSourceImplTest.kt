package com.mobile.felix.ticketapp.feature.eventDetail.data.source

import com.mobile.felix.ticketapp.core.data.local.dao.EventDao
import com.mobile.felix.ticketapp.core.data.local.dao.OrderDao
import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity
import com.mobile.felix.ticketapp.core.data.local.entity.OrderEntity
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
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

class EventDetailLocalDataSourceImplTest {

    @MockK
    private lateinit var eventDao: EventDao

    @MockK
    private lateinit var orderDao: OrderDao

    // SUT (System Under Test)
    private lateinit var dataSource: EventDetailLocalDataSourceImpl

    private val fakeEventEntity = EventEntity(
        id = 1L, name = "Festival de Verão", date = "15/02/2026",
        price = 120.0, location = "SP", poster = "", description = "Desc"
    )

    private val fakeOrderEntity = OrderEntity(
        id = 10L, eventId = 1L, eventName = "Festival de Verão",
        eventDate = "15/02/2026", eventPrice = 120.0, purchaseDate = "10/01/2026",
        ticketQuantity = 2, status = OrderStatus.WAITING_PAYMENT.name
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dataSource = EventDetailLocalDataSourceImpl(eventDao, orderDao, Dispatchers.Unconfined)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return mapped event when dao returns entity by id`() = runTest {
        // Given
        coEvery { eventDao.getById(1L) } returns fakeEventEntity

        // When
        val result = dataSource.getEventById(1L)

        // Then
        assertEquals(1L, result.id)
        assertEquals("Festival de Verão", result.name)
        coVerify(exactly = 1) { eventDao.getById(1L) }
    }

    @Test
    fun `should return mapped order when dao returns order entity by event id`() = runTest {
        // Given
        coEvery { orderDao.getByEventId(1L) } returns fakeOrderEntity

        // When
        val result = dataSource.getOrderByEventId(1L)

        // Then
        assertNotNull(result)
        assertEquals(10L, result!!.id)
        assertEquals(OrderStatus.WAITING_PAYMENT, result.status)
        coVerify(exactly = 1) { orderDao.getByEventId(1L) }
    }

    @Test
    fun `should return null when dao returns no order for event id`() = runTest {
        // Given
        coEvery { orderDao.getByEventId(99L) } returns null

        // When
        val result = dataSource.getOrderByEventId(99L)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { orderDao.getByEventId(99L) }
    }

    @Test
    fun `should call orderDao insert and return generated id when saving initial order`() = runTest {
        // Given
        val expectedId = 42L
        coEvery { orderDao.insert(any()) } returns expectedId
        coEvery { eventDao.getById(any()) } returns fakeEventEntity

        val fakeEvent = fakeEventEntity.run {
            com.mobile.felix.ticketapp.core.domain.model.Event(id, name, date, location, poster, price, description)
        }

        // When
        val result = dataSource.saveInitialOrder(fakeEvent, 2)

        // Then
        assertEquals(expectedId, result)
        coVerify(exactly = 1) { orderDao.insert(any()) }
    }

    @Test
    fun `should propagate exception when eventDao throws on getById`() = runTest {
        // Given
        val exception = RuntimeException("Event not found")
        coEvery { eventDao.getById(any()) } throws exception

        // When
        val result = runCatching { dataSource.getEventById(1L) }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Event not found", result.exceptionOrNull()?.message)
    }
}


