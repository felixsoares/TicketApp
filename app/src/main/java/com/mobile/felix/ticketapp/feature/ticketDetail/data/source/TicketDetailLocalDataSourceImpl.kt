package com.mobile.felix.ticketapp.feature.ticketDetail.data.source

import com.mobile.felix.ticketapp.core.data.local.dao.OrderDao
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.core.mapper.toDomain
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.source.TicketDetailLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketDetailLocalDataSourceImpl(
    private val orderDao: OrderDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TicketDetailLocalDataSource {

    override suspend fun getOrderById(orderId: Long): Order? = withContext(dispatcher) {
        orderDao.getById(orderId)?.toDomain()
    }

    override suspend fun updateOrderStatus(
        orderId: Long,
        status: OrderStatus
    ) = withContext(dispatcher) {
        orderDao.updateOrderStatus(orderId, status.name)
    }
}

