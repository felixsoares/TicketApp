package com.mobile.felix.ticketapp.feature.receipt.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.receipt.domain.repository.ReceiptRepository

class GetOrderByIdUseCase(private val repository: ReceiptRepository) {
    suspend operator fun invoke(orderId: Long): Order = repository.getOrderById(orderId)
}

