package com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.repository.TicketDetailRepository
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase.GetOrderByIdUseCase
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

class GetOrderByIdUseCaseTest {

    @MockK
    private lateinit var repository: TicketDetailRepository

    // SUT (System Under Test)
    private lateinit var useCase: GetOrderByIdUseCase

    private val fakeOrder = Order(
        id = 10L, eventId = 1L, eventName = "Rock Night",
        eventDate = "20/03/2026", price = 180.0, purchaseDate = "15/02/2026",
        ticketQuantity = 1, status = OrderStatus.APPROVED
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = GetOrderByIdUseCase(repository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return order when repository finds order by id`() = runTest {
        // Given
        coEvery { repository.getOrderById(10L) } returns fakeOrder

        // When
        val result = useCase(10L)

        // Then
        assertNotNull(result)
        assertEquals(fakeOrder, result)
        assertEquals(OrderStatus.APPROVED, result!!.status)
        coVerify(exactly = 1) { repository.getOrderById(10L) }
    }

    @Test
    fun `should return null when repository finds no order for id`() = runTest {
        // Given
        coEvery { repository.getOrderById(99L) } returns null

        // When
        val result = useCase(99L)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { repository.getOrderById(99L) }
    }
}

