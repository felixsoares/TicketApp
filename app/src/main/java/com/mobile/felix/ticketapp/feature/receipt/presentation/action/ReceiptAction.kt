package com.mobile.felix.ticketapp.feature.receipt.presentation.action

sealed interface ReceiptAction {
    data object Idle : ReceiptAction
    data class LoadOrder(val orderId: Long) : ReceiptAction
}

