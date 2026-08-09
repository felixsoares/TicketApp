package com.mobile.felix.ticketapp.feature.tickets.data.usecase

import com.mobile.felix.ticketapp.feature.tickets.domain.source.TicketsLocalDataSource

class InitOrdersUseCase(private val localDataSource: TicketsLocalDataSource) {
    suspend operator fun invoke() = localDataSource.initOrders()
}

