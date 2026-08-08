package com.mobile.felix.ticketapp.feature.cart.domain.source

import com.mobile.felix.ticketapp.core.domain.model.Order

interface CartLocalDataSource {
    suspend fun getOrders(): List<Order>
    suspend fun initOrders()
}

