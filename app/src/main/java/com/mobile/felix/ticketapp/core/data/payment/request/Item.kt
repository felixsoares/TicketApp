package com.mobile.felix.ticketapp.core.data.payment.request

data class Item(
    val sku: String,
    val name: String,
    val unitPrice: Double,
    val quantity: Int,
    val unitOfMeasure: String,
    val description: String,
    val details: String,
)