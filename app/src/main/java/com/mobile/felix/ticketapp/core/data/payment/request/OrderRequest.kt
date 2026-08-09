package com.mobile.felix.ticketapp.core.data.payment.request

import cielo.orders.domain.SubAcquirer

data class OrderRequest(
    val clientID: String,
    val accessToken: String,
    val value: Double,
    val paymentCode: String?,
    val installments: Int,
    val email: String,
    val merchantCode: String?,
    val reference: String,
    val items: MutableList<Item>,
    val subAcquirer: SubAcquirer? = null
)