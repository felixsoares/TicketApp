package com.mobile.felix.ticketapp.feature.receipt.presentation.state

import com.mobile.felix.ticketapp.core.domain.model.Order

data class ReceiptUiState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val errorMessage: String? = null
)

