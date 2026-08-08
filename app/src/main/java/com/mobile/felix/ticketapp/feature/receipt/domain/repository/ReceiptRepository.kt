package com.mobile.felix.ticketapp.feature.receipt.domain.repository

import com.mobile.felix.ticketapp.core.domain.model.Order

interface ReceiptRepository {
    suspend fun getOrderById(orderId: Long): Order
}

