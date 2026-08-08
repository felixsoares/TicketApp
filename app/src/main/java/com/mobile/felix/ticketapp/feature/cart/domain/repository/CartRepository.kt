package com.mobile.felix.ticketapp.feature.cart.domain.repository

import com.mobile.felix.ticketapp.core.domain.model.Order

interface CartRepository {
    suspend fun getOrders(): List<Order>
}

