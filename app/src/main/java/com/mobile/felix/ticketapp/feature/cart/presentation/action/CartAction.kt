package com.mobile.felix.ticketapp.feature.cart.presentation.action

sealed interface CartAction {
    data object Idle : CartAction
    data object LoadOrders : CartAction
}

