package com.mobile.felix.ticketapp.feature.tickets.data.source

import com.mobile.felix.ticketapp.core.data.database.MockDataBase
import com.mobile.felix.ticketapp.core.data.local.dao.OrderDao
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.mapper.toDomain
import com.mobile.felix.ticketapp.feature.tickets.domain.source.TicketsLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketsLocalDataSourceImpl(
    private val orderDao: OrderDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TicketsLocalDataSource {

    override suspend fun getOrders(): List<Order> = withContext(dispatcher) {
        orderDao.getAll().map { it.toDomain() }
    }

    override suspend fun initOrders() = withContext(dispatcher) {
        orderDao.insertAll(MockDataBase.orders)
    }
}

