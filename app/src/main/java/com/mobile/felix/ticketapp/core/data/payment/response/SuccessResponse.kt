package com.mobile.felix.ticketapp.core.data.payment.response

data class SuccessResponse(
    val createdAt: String,
    val id: String,
    val items: List<Item>,
    val status: String,
    val type: String,
    val updatedAt: String,
)