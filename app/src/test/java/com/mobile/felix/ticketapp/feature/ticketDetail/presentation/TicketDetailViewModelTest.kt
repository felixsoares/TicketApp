package com.mobile.felix.ticketapp.feature.ticketDetail.presentation

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase.GetOrderByIdUseCase
import com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase.PaymentRequestUseCase
import com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase.UpdateOrderStatusUseCase
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.action.TicketDetailAction
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.state.PaymentUiEffect
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TicketDetailViewModelTest {

    @MockK
    private lateinit var getOrderByIdUseCase: GetOrderByIdUseCase

    @MockK
    private lateinit var paymentRequestUseCase: PaymentRequestUseCase

    @MockK
    private lateinit var updateOrderStatusUseCase: UpdateOrderStatusUseCase

    // SUT (System Under Test)
    private lateinit var viewModel: TicketDetailViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeOrder = Order(
        id = 10L, eventId = 1L, eventName = "Festival de Verão",
        eventDate = "15/02/2026", price = 240.0, purchaseDate = "10/01/2026",
        ticketQuantity = 2, status = OrderStatus.WAITING_PAYMENT
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = TicketDetailViewModel(
            getOrderByIdUseCase,
            paymentRequestUseCase,
            updateOrderStatusUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should start with default empty uiState`() = runTest {
        // Given — ViewModel created in setUp

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.order)
        assertFalse(viewModel.uiState.value.hasError)
    }

    @Test
    fun `should update uiState with order when LoadOrder action is dispatched`() = runTest {
        // Given
        coEvery { getOrderByIdUseCase(10L) } returns fakeOrder

        // When
        viewModel.onAction(TicketDetailAction.LoadOrder(10L))

        // Then
        assertEquals(fakeOrder, viewModel.uiState.value.order)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.hasError)
        coVerify(exactly = 1) { getOrderByIdUseCase(10L) }
    }

    @Test
    fun `should emit LaunchCieloApp effect when PaymentRequest action is dispatched`() = runTest {
        // Given
        val expectedUri = "lio://payment?request=abc123"
        coEvery { paymentRequestUseCase.invoke(fakeOrder) } returns expectedUri

        // When
        viewModel.onAction(TicketDetailAction.PaymentRequest(fakeOrder))

        // Then
        coVerify(exactly = 1) { paymentRequestUseCase.invoke(fakeOrder) }
    }

    @Test
    fun `should update order status to APPROVED when payment response is PAID`() = runTest {
        // Given — load the order first so orderId is available
        coEvery { getOrderByIdUseCase(10L) } returns fakeOrder
        viewModel.onAction(TicketDetailAction.LoadOrder(10L))

        val approvedOrder = fakeOrder.copy(status = OrderStatus.APPROVED)
        coEvery { getOrderByIdUseCase(10L) } returns approvedOrder

        val paidJson = """{"status":"PAID","description":"Approved"}"""

        // When
        viewModel.onAction(TicketDetailAction.PaymentActionUpdate(paidJson))

        // Then
        coVerify(exactly = 1) { updateOrderStatusUseCase.invoke(10L, OrderStatus.APPROVED) }
        assertEquals(OrderStatus.APPROVED, viewModel.uiState.value.order?.status)
    }

    @Test
    fun `should update order status to DENIED when payment response has code 2`() = runTest {
        // Given — load the order first
        coEvery { getOrderByIdUseCase(10L) } returns fakeOrder
        viewModel.onAction(TicketDetailAction.LoadOrder(10L))

        val deniedOrder = fakeOrder.copy(status = OrderStatus.DENIED)
        coEvery { getOrderByIdUseCase(10L) } returns deniedOrder

        val deniedJson = """{"code":2,"description":"Denied by issuer"}"""

        // When
        viewModel.onAction(TicketDetailAction.PaymentActionUpdate(deniedJson))

        // Then
        coVerify(exactly = 1) { updateOrderStatusUseCase.invoke(10L, OrderStatus.DENIED) }
        assertEquals(OrderStatus.DENIED, viewModel.uiState.value.order?.status)
    }

    @Test
    fun `should update order status to CANCELLED when payment response has unknown code`() = runTest {
        // Given — load the order first
        coEvery { getOrderByIdUseCase(10L) } returns fakeOrder
        viewModel.onAction(TicketDetailAction.LoadOrder(10L))

        val cancelledOrder = fakeOrder.copy(status = OrderStatus.CANCELLED)
        coEvery { getOrderByIdUseCase(10L) } returns cancelledOrder

        val cancelledJson = """{"code":99,"description":"Unknown"}"""

        // When
        viewModel.onAction(TicketDetailAction.PaymentActionUpdate(cancelledJson))

        // Then
        coVerify(exactly = 1) { updateOrderStatusUseCase.invoke(10L, OrderStatus.CANCELLED) }
    }

    @Test
    fun `should not call any use case when Idle action is dispatched`() = runTest {
        // Given — no setup needed

        // When
        viewModel.onAction(TicketDetailAction.Idle)

        // Then
        coVerify(exactly = 0) { getOrderByIdUseCase(any()) }
        coVerify(exactly = 0) { paymentRequestUseCase.invoke(any()) }
        coVerify(exactly = 0) { updateOrderStatusUseCase.invoke(any(), any()) }
    }

    @Test
    fun `should not process payment when order is null on PaymentActionUpdate`() = runTest {
        // Given — no order loaded in state (orderId is null)
        assertNull(viewModel.uiState.value.order)

        val paidJson = """{"status":"PAID"}"""

        // When
        viewModel.onAction(TicketDetailAction.PaymentActionUpdate(paidJson))

        // Then — updateOrderStatus should NOT be called since orderId is null
        coVerify(exactly = 0) { updateOrderStatusUseCase.invoke(any(), any()) }
    }

    @Test
    fun `should emit LaunchCieloApp with correct uri when payment method builds checkout link`() = runTest {
        // Given
        val checkoutUri = "lio://payment?request=base64data&urlCallback=order://response"
        coEvery { paymentRequestUseCase.invoke(fakeOrder) } returns checkoutUri

        // When
        viewModel.onAction(TicketDetailAction.PaymentRequest(fakeOrder))

        // Then — first() collects one emission and completes, avoiding infinite suspension
        val receivedEffect = viewModel.effect.first()
        assertNotNull(receivedEffect)
        assertEquals(checkoutUri, (receivedEffect as PaymentUiEffect.LaunchCieloApp).checkoutUri)
    }
}



