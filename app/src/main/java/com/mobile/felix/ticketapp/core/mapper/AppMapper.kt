package com.mobile.felix.ticketapp.core.mapper

import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity
import com.mobile.felix.ticketapp.core.data.local.entity.OrderEntity
import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus

fun EventEntity.toDomain() = Event(
    id = id,
    name = name,
    date = date,
    location = location,
    poster = poster,
    description = description
)

fun OrderEntity.toDomain() = Order(
    id = id,
    eventId = eventId,
    eventName = eventName,
    eventDate = eventDate,
    amount = amount,
    purchaseDate = purchaseDate,
    ticketQuantity = ticketQuantity,
    status = OrderStatus.valueOf(status)
)

