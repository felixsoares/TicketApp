package com.mobile.felix.ticketapp.feature.qrCode.domain.model

data class QrCodeData(
    val orderId: Long,
    val content: String,
    val width: Int = 512,
    val height: Int = 512,
    val matrix: List<List<Boolean>>
)

