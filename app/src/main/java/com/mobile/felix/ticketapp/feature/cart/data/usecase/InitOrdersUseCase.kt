package com.mobile.felix.ticketapp.feature.cart.data.usecase

import com.mobile.felix.ticketapp.feature.cart.domain.source.CartLocalDataSource

class InitOrdersUseCase(private val localDataSource: CartLocalDataSource) {
    suspend operator fun invoke() = localDataSource.initOrders()
}

