package com.mobile.felix.ticketapp.core.data.payment.response

data class Item(
    val id: String,
    val name: String,
    val quantity: Long,
    val sku: String,
    val unitOfMeasure: String,
    val unitPrice: Long,
)