package com.mobile.felix.ticketapp.feature.eventDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.eventDetail.domain.repository.EventDetailRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetOrderByEventIdUseCaseTest {

    @MockK
    private lateinit var repository: EventDetailRepository

    // SUT (System Under Test)
    private lateinit var useCase: GetOrderByEventIdUseCase

    private val fakeOrder = Order(
        id = 10L, eventId = 1L, eventName = "Festival de Verão",
        eventDate = "15/02/2026", price = 240.0, purchaseDate = "10/01/2026",
        ticketQuantity = 2, status = OrderStatus.WAITING_PAYMENT
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = GetOrderByEventIdUseCase(repository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return order when repository finds order for event id`() = runTest {
        // Given
        coEvery { repository.getOrderByEventId(1L) } returns fakeOrder

        // When
        val result = useCase.invoke(1L)

        // Then
        assertNotNull(result)
        assertEquals(fakeOrder, result)
        assertEquals(OrderStatus.WAITING_PAYMENT, result!!.status)
        coVerify(exactly = 1) { repository.getOrderByEventId(1L) }
    }

    @Test
    fun `should return null when repository finds no order for event id`() = runTest {
        // Given
        coEvery { repository.getOrderByEventId(99L) } returns null

        // When
        val result = useCase.invoke(99L)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { repository.getOrderByEventId(99L) }
    }
}

