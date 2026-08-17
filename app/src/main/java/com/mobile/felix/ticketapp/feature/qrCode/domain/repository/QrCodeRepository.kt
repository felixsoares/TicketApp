package com.mobile.felix.ticketapp.feature.qrCode.domain.repository

import com.mobile.felix.ticketapp.feature.qrCode.domain.model.QrCodeData

interface QrCodeRepository {
    suspend fun generateQrCode(orderId: Long, content: String, width: Int = 512, height: Int = 512): QrCodeData
}

