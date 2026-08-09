package com.mobile.felix.ticketapp.feature.ticketDetail.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.repository.TicketDetailRepository
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.source.TicketDetailLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketDetailRepositoryImpl(
    private val localDataSource: TicketDetailLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TicketDetailRepository {

    override suspend fun getOrderById(orderId: Long): Order = withContext(dispatcher) {
        localDataSource.getOrderById(orderId)
    }
}

