package com.mobile.felix.ticketapp.feature.cart.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.cart.domain.repository.CartRepository

class GetOrdersUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(): List<Order> = repository.getOrders()
}

