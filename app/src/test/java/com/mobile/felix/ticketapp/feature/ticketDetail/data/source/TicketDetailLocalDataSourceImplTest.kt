package com.mobile.felix.ticketapp.feature.ticketDetail.data.source

import com.mobile.felix.ticketapp.core.data.local.dao.OrderDao
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

class TicketDetailLocalDataSourceImplTest {

    @MockK
    private lateinit var orderDao: OrderDao

    // SUT (System Under Test)
    private lateinit var dataSource: TicketDetailLocalDataSourceImpl

    private val fakeOrderEntity = OrderEntity(
        id = 10L, eventId = 1L, eventName = "Festival de Verão",
        eventDate = "15/02/2026", eventPrice = 120.0, purchaseDate = "10/01/2026",
        ticketQuantity = 2, status = OrderStatus.WAITING_PAYMENT.name
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dataSource = TicketDetailLocalDataSourceImpl(orderDao, Dispatchers.Unconfined)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return mapped order when dao returns entity by id`() = runTest {
        // Given
        coEvery { orderDao.getById(10L) } returns fakeOrderEntity

        // When
        val result = dataSource.getOrderById(10L)

        // Then
        assertNotNull(result)
        assertEquals(10L, result!!.id)
        assertEquals(OrderStatus.WAITING_PAYMENT, result.status)
        coVerify(exactly = 1) { orderDao.getById(10L) }
    }

    @Test
    fun `should return null when dao returns null for order id`() = runTest {
        // Given
        coEvery { orderDao.getById(99L) } returns null

        // When
        val result = dataSource.getOrderById(99L)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { orderDao.getById(99L) }
    }

    @Test
    fun `should call updateOrderStatus on dao with correct status name`() = runTest {
        // Given — relaxUnitFun handles updateOrderStatus automatically

        // When
        dataSource.updateOrderStatus(10L, OrderStatus.APPROVED)

        // Then
        coVerify(exactly = 1) { orderDao.updateOrderStatus(10L, OrderStatus.APPROVED.name) }
    }

    @Test
    fun `should propagate exception when dao throws on getById`() = runTest {
        // Given
        val exception = RuntimeException("Order not found")
        coEvery { orderDao.getById(any()) } throws exception

        // When
        val result = runCatching { dataSource.getOrderById(10L) }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Order not found", result.exceptionOrNull()?.message)
    }
}


