package com.mobile.felix.ticketapp.feature.qrCode.domain.source

interface QrCodeLocalDataSource {
    suspend fun generateMatrix(content: String, width: Int, height: Int): List<List<Boolean>>
}

