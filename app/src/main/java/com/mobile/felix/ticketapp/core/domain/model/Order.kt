package com.mobile.felix.ticketapp.core.domain.model

data class Order(
    val id: Long,
    val eventId: Long,
    val eventName: String,
    val eventDate: String,
    val amount: Double,
    val purchaseDate: String,
    val ticketQuantity: Int,
    val status: OrderStatus
)

