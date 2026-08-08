package com.mobile.felix.ticketapp.feature.receipt.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.receipt.domain.repository.ReceiptRepository
import com.mobile.felix.ticketapp.feature.receipt.domain.source.ReceiptLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReceiptRepositoryImpl(
    private val localDataSource: ReceiptLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReceiptRepository {

    override suspend fun getOrderById(orderId: Long): Order = withContext(dispatcher) {
        localDataSource.getOrderById(orderId)
    }
}

