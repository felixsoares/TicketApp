package com.mobile.felix.ticketapp.feature.ticketDetail.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.source.TicketDetailLocalDataSource
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

class TicketDetailRepositoryImplTest {

    @MockK
    private lateinit var localDataSource: TicketDetailLocalDataSource

    // SUT (System Under Test)
    private lateinit var repository: TicketDetailRepositoryImpl

    private val fakeOrder = Order(
        id = 10L, eventId = 1L, eventName = "Festival de Verão",
        eventDate = "15/02/2026", price = 240.0, purchaseDate = "10/01/2026",
        ticketQuantity = 2, status = OrderStatus.WAITING_PAYMENT
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = TicketDetailRepositoryImpl(localDataSource, Dispatchers.Unconfined)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return order when data source returns order by id`() = runTest {
        // Given
        coEvery { localDataSource.getOrderById(10L) } returns fakeOrder

        // When
        val result = repository.getOrderById(10L)

        // Then
        assertNotNull(result)
        assertEquals(fakeOrder, result)
        coVerify(exactly = 1) { localDataSource.getOrderById(10L) }
    }

    @Test
    fun `should return null when data source finds no order for id`() = runTest {
        // Given
        coEvery { localDataSource.getOrderById(99L) } returns null

        // When
        val result = repository.getOrderById(99L)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { localDataSource.getOrderById(99L) }
    }

    @Test
    fun `should call updateOrderStatus on data source with correct parameters`() = runTest {
        // Given — relaxUnitFun handles updateOrderStatus automatically

        // When
        repository.updateOrderStatus(10L, OrderStatus.APPROVED)

        // Then
        coVerify(exactly = 1) { localDataSource.updateOrderStatus(10L, OrderStatus.APPROVED) }
    }

    @Test
    fun `should propagate exception when data source throws on getOrderById`() = runTest {
        // Given
        val exception = RuntimeException("Source error")
        coEvery { localDataSource.getOrderById(any()) } throws exception

        // When
        val result = runCatching { repository.getOrderById(10L) }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Source error", result.exceptionOrNull()?.message)
    }
}

