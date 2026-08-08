package com.mobile.felix.ticketapp.core.domain.model.payment

data class Item(
    val sku: String,
    val name: String,
    val unitPrice: Long,
    val quantity: Int,
    val unitOfMeasure: String
)