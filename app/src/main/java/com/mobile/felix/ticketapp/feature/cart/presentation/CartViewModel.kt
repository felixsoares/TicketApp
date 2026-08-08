package com.mobile.felix.ticketapp.feature.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.feature.cart.data.usecase.GetOrdersUseCase
import com.mobile.felix.ticketapp.feature.cart.data.usecase.InitOrdersUseCase
import com.mobile.felix.ticketapp.feature.cart.presentation.action.CartAction
import com.mobile.felix.ticketapp.feature.cart.presentation.state.CartUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val initOrdersUseCase: InitOrdersUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<CartAction>()

    var uiState = MutableStateFlow(CartUiState())
        private set

    init {
        initOrders()

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is CartAction.Idle -> {}
                    is CartAction.LoadOrders -> loadOrders()
                }
            }
        }
    }

    private fun initOrders() = viewModelScope.launch {
        initOrdersUseCase()
    }

    private fun loadOrders() = viewModelScope.launch {
        uiState.value = uiState.value.copy(isLoading = true)
        val result = runCatching { getOrdersUseCase() }
        uiState.value = uiState.value.copy(
            orders = result.getOrNull(),
            errorMessage = result.exceptionOrNull()?.message,
            isLoading = false
        )
    }

    fun onAction(action: CartAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}

