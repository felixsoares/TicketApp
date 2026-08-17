package com.mobile.felix.ticketapp.feature.qrCode.domain.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.qrCode.domain.model.QrCodeData
import com.mobile.felix.ticketapp.feature.qrCode.domain.repository.QrCodeRepository
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase.GetOrderByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerateTicketQrCodeUseCaseTest {

    @MockK
    private lateinit var getOrderByIdUseCase: GetOrderByIdUseCase

    @MockK
    private lateinit var qrCodeRepository: QrCodeRepository

    private lateinit var useCase: GenerateTicketQrCodeUseCase

    private val approvedOrder = Order(
        id = 10L,
        eventId = 1L,
        eventName = "Festival de Verão",
        eventDate = "15/02/2026",
        price = 240.0,
        purchaseDate = "10/01/2026",
        ticketQuantity = 2,
        status = OrderStatus.APPROVED
    )

    private val waitingOrder = approvedOrder.copy(status = OrderStatus.WAITING_PAYMENT)

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = GenerateTicketQrCodeUseCase(getOrderByIdUseCase, qrCodeRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should generate QR code successfully when order status is APPROVED`() = runTest {
        // Given
        val expectedData = QrCodeData(
            orderId = 10L,
            content = "TICKET-ORDER-10-EVENT-1",
            width = 512,
            height = 512,
            matrix = listOf(listOf(true, false), listOf(false, true))
        )
        coEvery { getOrderByIdUseCase(10L) } returns approvedOrder
        coEvery { qrCodeRepository.generateQrCode(10L, "TICKET-ORDER-10-EVENT-1") } returns expectedData

        // When
        val result = useCase(10L)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedData, result.getOrNull())
        coVerify(exactly = 1) { getOrderByIdUseCase(10L) }
        coVerify(exactly = 1) { qrCodeRepository.generateQrCode(10L, "TICKET-ORDER-10-EVENT-1") }
    }

    @Test
    fun `should return failure when order is not found`() = runTest {
        // Given
        coEvery { getOrderByIdUseCase(99L) } returns null

        // When
        val result = useCase(99L)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Order with ID 99 not found", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { qrCodeRepository.generateQrCode(any(), any(), any(), any()) }
    }

    @Test
    fun `should return failure when order status is WAITING_PAYMENT`() = runTest {
        // Given
        coEvery { getOrderByIdUseCase(10L) } returns waitingOrder

        // When
        val result = useCase(10L)

        // Then
        assertTrue(result.isFailure)
        assertEquals("QR code is only available for paid tickets", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { qrCodeRepository.generateQrCode(any(), any(), any(), any()) }
    }

    @Test
    fun `should return failure when order status is DENIED`() = runTest {
        // Given
        val deniedOrder = approvedOrder.copy(status = OrderStatus.DENIED)
        coEvery { getOrderByIdUseCase(10L) } returns deniedOrder

        // When
        val result = useCase(10L)

        // Then
        assertTrue(result.isFailure)
        assertEquals("QR code is only available for paid tickets", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { qrCodeRepository.generateQrCode(any(), any(), any(), any()) }
    }
}

