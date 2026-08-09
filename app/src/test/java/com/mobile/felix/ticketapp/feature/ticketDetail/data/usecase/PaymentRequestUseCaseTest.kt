package com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.core.payment.PaymentMethod
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PaymentRequestUseCaseTest {

    @MockK
    private lateinit var paymentMethod: PaymentMethod

    // SUT (System Under Test)
    private lateinit var useCase: PaymentRequestUseCase

    private val fakeOrder = Order(
        id = 10L, eventId = 1L, eventName = "Festival de Verão",
        eventDate = "15/02/2026", price = 240.0, purchaseDate = "10/01/2026",
        ticketQuantity = 2, status = OrderStatus.WAITING_PAYMENT
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = PaymentRequestUseCase(paymentMethod)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return checkout uri when payment method generates request`() = runTest {
        // Given
        val expectedUri = "lio://payment?request=abc123"
        every { paymentMethod.paymentRequest(fakeOrder) } returns expectedUri

        // When
        val result = useCase.invoke(fakeOrder)

        // Then
        assertEquals(expectedUri, result)
        verify(exactly = 1) { paymentMethod.paymentRequest(fakeOrder) }
    }

    @Test
    fun `should delegate payment request to PaymentMethod with correct order`() = runTest {
        // Given
        val expectedUri = "lio://payment?request=xyz456&urlCallback=order://response"
        every { paymentMethod.paymentRequest(fakeOrder) } returns expectedUri

        // When
        val result = useCase.invoke(fakeOrder)

        // Then
        assertEquals(expectedUri, result)
        verify(exactly = 1) { paymentMethod.paymentRequest(fakeOrder) }
    }
}

