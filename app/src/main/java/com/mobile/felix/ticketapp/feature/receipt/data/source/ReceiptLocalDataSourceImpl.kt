package com.mobile.felix.ticketapp.feature.receipt.data.source

import com.mobile.felix.ticketapp.core.data.local.dao.OrderDao
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.mapper.toDomain
import com.mobile.felix.ticketapp.feature.receipt.domain.source.ReceiptLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReceiptLocalDataSourceImpl(
    private val orderDao: OrderDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReceiptLocalDataSource {

    override suspend fun getOrderById(orderId: Long): Order = withContext(dispatcher) {
        orderDao.getById(orderId).toDomain()
    }
}

