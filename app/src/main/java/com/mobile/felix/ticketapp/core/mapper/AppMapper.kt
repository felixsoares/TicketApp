package com.mobile.felix.ticketapp.core.mapper

import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity
import com.mobile.felix.ticketapp.core.domain.Event

fun EventEntity.toDomain() = Event(
    id = id,
    name = name,
    date = date,
    location = location,
    poster = poster,
    description = description
)