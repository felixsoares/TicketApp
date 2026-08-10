package com.mobile.felix.ticketapp.feature.ticketDetail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mobile.felix.ticketapp.core.data.payment.response.ErrorResponse
import com.mobile.felix.ticketapp.core.data.payment.response.SuccessResponse
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase.GetOrderByIdUseCase
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase.PaymentRequestUseCase
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase.UpdateOrderStatusUseCase
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.action.TicketDetailAction
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.state.PaymentUiEffect
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.state.TicketDetailUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TicketDetailViewModel(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val paymentRequestUseCase: PaymentRequestUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<TicketDetailAction>()

    var uiState = MutableStateFlow(TicketDetailUiState())
        private set

    val _effect = Channel<PaymentUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is TicketDetailAction.Idle -> {}
                    is TicketDetailAction.LoadOrder -> loadOrder(action.orderId)
                    is TicketDetailAction.PaymentRequest -> paymentRequest(action.order)
                    is TicketDetailAction.PaymentActionUpdate -> processPayment(action.json)
                }
            }
        }
    }

    private fun processPayment(json: String) = viewModelScope.launch {
        val orderId = uiState.value.order?.id ?: return@launch

        try {
            val response = Gson().fromJson(json, SuccessResponse::class.java)
            val status = if (response != null && response.status == "PAID") {
                OrderStatus.APPROVED
            } else {
                val response = Gson().fromJson(json, ErrorResponse::class.java)
                when (response.code) {
                    2L -> OrderStatus.DENIED
                    else -> OrderStatus.CANCELLED
                }
            }

            updateOrderStatusUseCase.invoke(orderId, status)
            loadOrder(orderId)
        } catch (e: Exception) {
            Log.e("TicketDetailViewModel", "Error parsing payment response: ${e.message}")
        }
    }

    private fun paymentRequest(order: Order) = viewModelScope.launch {
        val uri = paymentRequestUseCase.invoke(order)
        _effect.send(PaymentUiEffect.LaunchCieloApp(uri))
    }

    private fun loadOrder(orderId: Long) = viewModelScope.launch {
        uiState.value = uiState.value.copy(isLoading = true)
        val result = getOrderByIdUseCase(orderId)
        uiState.value = uiState.value.copy(
            order = result,
            hasError = false,
            isLoading = false
        )
    }

    fun onAction(action: TicketDetailAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}

