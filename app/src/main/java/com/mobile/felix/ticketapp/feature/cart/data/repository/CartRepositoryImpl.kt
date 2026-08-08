package com.mobile.felix.ticketapp.feature.cart.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.cart.domain.repository.CartRepository
import com.mobile.felix.ticketapp.feature.cart.domain.source.CartLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepositoryImpl(
    private val localDataSource: CartLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CartRepository {

    override suspend fun getOrders(): List<Order> = withContext(dispatcher) {
        localDataSource.getOrders()
    }
}

