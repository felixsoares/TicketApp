package com.mobile.felix.ticketapp.core.payment

import com.mobile.felix.ticketapp.core.domain.model.Order

interface PaymentMethod {
    fun paymentRequest(order: Order) : String
}