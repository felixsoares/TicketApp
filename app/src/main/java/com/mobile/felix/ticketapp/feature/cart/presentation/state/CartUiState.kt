package com.mobile.felix.ticketapp.feature.cart.presentation.state

import com.mobile.felix.ticketapp.core.domain.model.Order

data class CartUiState(
    val isLoading: Boolean = false,
    val orders: List<Order>? = null,
    val errorMessage: String? = null
)

