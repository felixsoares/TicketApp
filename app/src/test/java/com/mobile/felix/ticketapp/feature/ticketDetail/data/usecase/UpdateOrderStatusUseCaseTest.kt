package com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.repository.TicketDetailRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateOrderStatusUseCaseTest {

    @MockK
    private lateinit var repository: TicketDetailRepository

    // SUT (System Under Test)
    private lateinit var useCase: UpdateOrderStatusUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = UpdateOrderStatusUseCase(repository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should call repository updateOrderStatus with APPROVED status`() = runTest {
        // Given — relaxUnitFun handles updateOrderStatus automatically

        // When
        useCase.invoke(10L, OrderStatus.APPROVED)

        // Then
        coVerify(exactly = 1) { repository.updateOrderStatus(10L, OrderStatus.APPROVED) }
    }

    @Test
    fun `should call repository updateOrderStatus with DENIED status`() = runTest {
        // Given

        // When
        useCase.invoke(10L, OrderStatus.DENIED)

        // Then
        coVerify(exactly = 1) { repository.updateOrderStatus(10L, OrderStatus.DENIED) }
    }

    @Test
    fun `should call repository updateOrderStatus with CANCELLED status`() = runTest {
        // Given

        // When
        useCase.invoke(10L, OrderStatus.CANCELLED)

        // Then
        coVerify(exactly = 1) { repository.updateOrderStatus(10L, OrderStatus.CANCELLED) }
    }
}

