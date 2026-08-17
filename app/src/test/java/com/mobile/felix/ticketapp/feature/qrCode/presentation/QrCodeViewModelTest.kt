package com.mobile.felix.ticketapp.feature.qrCode.presentation

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.qrCode.domain.model.QrCodeData
import com.mobile.felix.ticketapp.feature.qrCode.domain.usecase.GenerateTicketQrCodeUseCase
import com.mobile.felix.ticketapp.feature.qrCode.presentation.action.QrCodeAction
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase.GetOrderByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QrCodeViewModelTest {

    @MockK
    private lateinit var getOrderByIdUseCase: GetOrderByIdUseCase

    @MockK
    private lateinit var generateTicketQrCodeUseCase: GenerateTicketQrCodeUseCase

    private lateinit var viewModel: QrCodeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

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
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = QrCodeViewModel(getOrderByIdUseCase, generateTicketQrCodeUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should start with default uiState`() = runTest {
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.order)
        assertNull(viewModel.uiState.value.qrCodeData)
        assertFalse(viewModel.uiState.value.isPaid)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `should set error when order is not found`() = runTest {
        // Given
        coEvery { getOrderByIdUseCase(99L) } returns null

        // When
        viewModel.onAction(QrCodeAction.LoadQrCode(99L))

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Pedido não encontrado", viewModel.uiState.value.errorMessage)
        coVerify(exactly = 1) { getOrderByIdUseCase(99L) }
        coVerify(exactly = 0) { generateTicketQrCodeUseCase(any()) }
    }

    @Test
    fun `should set isPaid to false when order is not APPROVED`() = runTest {
        // Given
        coEvery { getOrderByIdUseCase(10L) } returns waitingOrder

        // When
        viewModel.onAction(QrCodeAction.LoadQrCode(10L))

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isPaid)
        assertEquals(waitingOrder, viewModel.uiState.value.order)
        assertEquals(
            "O QR Code só é gerado para ingressos com pagamento aprovado.",
            viewModel.uiState.value.errorMessage
        )
        coVerify(exactly = 1) { getOrderByIdUseCase(10L) }
        coVerify(exactly = 0) { generateTicketQrCodeUseCase(any()) }
    }

    @Test
    fun `should load QR code and set isPaid to true when order is APPROVED`() = runTest {
        // Given
        val qrData = QrCodeData(
            orderId = 10L,
            content = "TICKET-ORDER-10-EVENT-1",
            width = 512,
            height = 512,
            matrix = listOf(listOf(true))
        )
        coEvery { getOrderByIdUseCase(10L) } returns approvedOrder
        coEvery { generateTicketQrCodeUseCase(10L) } returns Result.success(qrData)

        // When
        viewModel.onAction(QrCodeAction.LoadQrCode(10L))

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.isPaid)
        assertEquals(approvedOrder, viewModel.uiState.value.order)
        assertEquals(qrData, viewModel.uiState.value.qrCodeData)
        assertNull(viewModel.uiState.value.errorMessage)
        coVerify(exactly = 1) { getOrderByIdUseCase(10L) }
        coVerify(exactly = 1) { generateTicketQrCodeUseCase(10L) }
    }
}

