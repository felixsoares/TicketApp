package com.mobile.felix.ticketapp.feature.ticketDetail.presentation.state

sealed interface PaymentUiEffect {
    data class LaunchCieloApp(val checkoutUri: String) : PaymentUiEffect
}