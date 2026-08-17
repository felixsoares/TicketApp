package com.mobile.felix.ticketapp.feature.qrCode.domain.usecase

import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.qrCode.domain.model.QrCodeData
import com.mobile.felix.ticketapp.feature.qrCode.domain.repository.QrCodeRepository
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase.GetOrderByIdUseCase

class GenerateTicketQrCodeUseCase(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val qrCodeRepository: QrCodeRepository
) {
    suspend operator fun invoke(orderId: Long): Result<QrCodeData> {
        val order = getOrderByIdUseCase(orderId)
            ?: return Result.failure(NoSuchElementException("Order with ID $orderId not found"))

        if (order.status != OrderStatus.APPROVED) {
            return Result.failure(IllegalStateException("QR code is only available for paid tickets"))
        }

        val content = "TICKET-ORDER-${order.id}-EVENT-${order.eventId}"
        val qrCodeData = qrCodeRepository.generateQrCode(orderId = order.id, content = content)
        return Result.success(qrCodeData)
    }
}

