package com.mobile.felix.ticketapp.core.domain.model

data class Event(
    val id: Long,
    val name: String,
    val date: String,
    val location: String,
    val poster: String,
    val description: String,
)