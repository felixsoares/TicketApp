package com.mobile.felix.ticketapp.feature.eventDetail.presentation

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase.GetEventByIdUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase.GetOrderByEventIdUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase.SaveInitialOrderUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.action.EventDetailAction
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventDetailViewModelTest {

    @MockK
    private lateinit var getEventByIdUseCase: GetEventByIdUseCase

    @MockK
    private lateinit var getOrderByEventIdUseCase: GetOrderByEventIdUseCase

    @MockK
    private lateinit var saveInitialOrderUseCase: SaveInitialOrderUseCase

    // SUT (System Under Test)
    private lateinit var viewModel: EventDetailViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeEvent = Event(
        id = 1L, name = "Festival de Verão", date = "15/02/2026",
        location = "SP", poster = "", price = 120.0, description = ""
    )

    private val fakeOrder = Order(
        id = 10L, eventId = 1L, eventName = "Festival de Verão",
        eventDate = "15/02/2026", price = 240.0, purchaseDate = "10/01/2026",
        ticketQuantity = 2, status = OrderStatus.WAITING_PAYMENT
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = EventDetailViewModel(
            getEventByIdUseCase,
            getOrderByEventIdUseCase,
            saveInitialOrderUseCase
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
        assertNull(viewModel.uiState.value.event)
        assertNull(viewModel.uiState.value.orderId)
        assertEquals("", viewModel.uiState.value.message)
    }

    @Test
    fun `should update uiState with event when GetEventById action is dispatched`() = runTest {
        // Given
        coEvery { getEventByIdUseCase.invoke(1L) } returns fakeEvent

        // When
        viewModel.onAction(EventDetailAction.GetEventById(1L))

        // Then
        assertEquals(fakeEvent, viewModel.uiState.value.event)
        assertFalse(viewModel.uiState.value.isLoading)
        coVerify(exactly = 1) { getEventByIdUseCase.invoke(1L) }
    }

    @Test
    fun `should save order and set orderId when BuyTicket dispatched and no existing order`() = runTest {
        // Given — no existing order for this event
        val expectedOrderId = 42L
        coEvery { getOrderByEventIdUseCase.invoke(fakeEvent.id) } returns null
        coEvery { saveInitialOrderUseCase.invoke(fakeEvent, 2) } returns expectedOrderId

        // When
        viewModel.onAction(EventDetailAction.BuyTicket(fakeEvent, 2))

        // Then
        assertEquals(expectedOrderId, viewModel.uiState.value.orderId)
        assertFalse(viewModel.uiState.value.isLoading)
        coVerify(exactly = 1) { saveInitialOrderUseCase.invoke(fakeEvent, 2) }
    }

    @Test
    fun `should save order when existing order has CANCELLED status`() = runTest {
        // Given — existing order is cancelled, should allow new purchase
        val cancelledOrder = fakeOrder.copy(status = OrderStatus.CANCELLED)
        val expectedOrderId = 55L
        coEvery { getOrderByEventIdUseCase.invoke(fakeEvent.id) } returns cancelledOrder
        coEvery { saveInitialOrderUseCase.invoke(fakeEvent, 1) } returns expectedOrderId

        // When
        viewModel.onAction(EventDetailAction.BuyTicket(fakeEvent, 1))

        // Then
        assertEquals(expectedOrderId, viewModel.uiState.value.orderId)
        coVerify(exactly = 1) { saveInitialOrderUseCase.invoke(fakeEvent, 1) }
    }

    @Test
    fun `should save order when existing order has DENIED status`() = runTest {
        // Given — existing order is denied, should allow new purchase
        val deniedOrder = fakeOrder.copy(status = OrderStatus.DENIED)
        val expectedOrderId = 66L
        coEvery { getOrderByEventIdUseCase.invoke(fakeEvent.id) } returns deniedOrder
        coEvery { saveInitialOrderUseCase.invoke(fakeEvent, 1) } returns expectedOrderId

        // When
        viewModel.onAction(EventDetailAction.BuyTicket(fakeEvent, 1))

        // Then
        assertEquals(expectedOrderId, viewModel.uiState.value.orderId)
        coVerify(exactly = 1) { saveInitialOrderUseCase.invoke(fakeEvent, 1) }
    }

    @Test
    fun `should set message and not save order when active order already exists`() = runTest {
        // Given — existing WAITING_PAYMENT order blocks new purchase
        coEvery { getOrderByEventIdUseCase.invoke(fakeEvent.id) } returns fakeOrder

        // When
        viewModel.onAction(EventDetailAction.BuyTicket(fakeEvent, 2))

        // Then
        assertEquals("Você já possui um pedido para este evento.", viewModel.uiState.value.message)
        assertNull(viewModel.uiState.value.orderId)
        assertFalse(viewModel.uiState.value.isLoading)
        coVerify(exactly = 0) { saveInitialOrderUseCase.invoke(any(), any()) }
    }

    @Test
    fun `should set message and not save order when APPROVED order already exists`() = runTest {
        // Given — approved order also blocks new purchase
        val approvedOrder = fakeOrder.copy(status = OrderStatus.APPROVED)
        coEvery { getOrderByEventIdUseCase.invoke(fakeEvent.id) } returns approvedOrder

        // When
        viewModel.onAction(EventDetailAction.BuyTicket(fakeEvent, 1))

        // Then
        assertNotNull(viewModel.uiState.value.message)
        assertTrue(viewModel.uiState.value.message.isNotEmpty())
        coVerify(exactly = 0) { saveInitialOrderUseCase.invoke(any(), any()) }
    }

    @Test
    fun `should not call any use case when Idle action is dispatched`() = runTest {
        // Given — no setup needed

        // When
        viewModel.onAction(EventDetailAction.Idle)

        // Then
        coVerify(exactly = 0) { getEventByIdUseCase.invoke(any()) }
        coVerify(exactly = 0) { saveInitialOrderUseCase.invoke(any(), any()) }
        coVerify(exactly = 0) { getOrderByEventIdUseCase.invoke(any()) }
    }
}



