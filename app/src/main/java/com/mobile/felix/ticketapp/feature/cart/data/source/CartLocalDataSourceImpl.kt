package com.mobile.felix.ticketapp.feature.cart.data.source

import com.mobile.felix.ticketapp.core.data.database.MockDataBase
import com.mobile.felix.ticketapp.core.data.local.dao.OrderDao
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.mapper.toDomain
import com.mobile.felix.ticketapp.feature.cart.domain.source.CartLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartLocalDataSourceImpl(
    private val orderDao: OrderDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CartLocalDataSource {

    override suspend fun getOrders(): List<Order> = withContext(dispatcher) {
        orderDao.getAll().map { it.toDomain() }
    }

    override suspend fun initOrders() = withContext(dispatcher) {
        orderDao.insertAll(MockDataBase.orders)
    }
}

