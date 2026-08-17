package com.mobile.felix.ticketapp.feature.qrCode.data.repository

import com.mobile.felix.ticketapp.feature.qrCode.domain.model.QrCodeData
import com.mobile.felix.ticketapp.feature.qrCode.domain.repository.QrCodeRepository
import com.mobile.felix.ticketapp.feature.qrCode.domain.source.QrCodeLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QrCodeRepositoryImpl(
    private val localDataSource: QrCodeLocalDataSource
) : QrCodeRepository {

    override suspend fun generateQrCode(
        orderId: Long,
        content: String,
        width: Int,
        height: Int
    ): QrCodeData = withContext(Dispatchers.IO) {
        val matrix = localDataSource.generateMatrix(content, width, height)
        QrCodeData(
            orderId = orderId,
            content = content,
            width = width,
            height = height,
            matrix = matrix
        )
    }
}

