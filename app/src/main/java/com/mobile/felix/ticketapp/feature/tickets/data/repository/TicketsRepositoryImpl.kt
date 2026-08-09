package com.mobile.felix.ticketapp.feature.tickets.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.tickets.domain.repository.TicketsRepository
import com.mobile.felix.ticketapp.feature.tickets.domain.source.TicketsLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketsRepositoryImpl(
    private val localDataSource: TicketsLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TicketsRepository {

    override suspend fun getOrders(): List<Order> = withContext(dispatcher) {
        localDataSource.getOrders()
    }
}

