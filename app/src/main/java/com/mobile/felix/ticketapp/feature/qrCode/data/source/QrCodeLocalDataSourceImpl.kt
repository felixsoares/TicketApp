package com.mobile.felix.ticketapp.feature.qrCode.data.source

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.mobile.felix.ticketapp.feature.qrCode.domain.source.QrCodeLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QrCodeLocalDataSourceImpl : QrCodeLocalDataSource {

    override suspend fun generateMatrix(
        content: String,
        width: Int,
        height: Int
    ): List<List<Boolean>> = withContext(Dispatchers.IO) {
        val bitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            width,
            height
        )

        List(height) { y ->
            List(width) { x ->
                bitMatrix.get(x, y)
            }
        }
    }
}

