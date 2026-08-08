package com.mobile.felix.ticketapp.feature.receipt.domain.source

import com.mobile.felix.ticketapp.core.domain.model.Order

interface ReceiptLocalDataSource {
    suspend fun getOrderById(orderId: Long): Order
}

