package com.mobile.felix.ticketapp.feature.qrCode.presentation.action

sealed class QrCodeAction {
    data class LoadQrCode(val orderId: Long) : QrCodeAction()
}

