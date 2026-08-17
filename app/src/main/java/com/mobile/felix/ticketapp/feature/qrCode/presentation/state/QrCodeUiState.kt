package com.mobile.felix.ticketapp.feature.qrCode.presentation.state

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.qrCode.domain.model.QrCodeData

data class QrCodeUiState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val qrCodeData: QrCodeData? = null,
    val isPaid: Boolean = false,
    val errorMessage: String? = null
)

