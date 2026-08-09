package com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.payment.PaymentMethod

class PaymentRequestUseCase(
    private val paymentMethod: PaymentMethod
) {
    suspend fun invoke(order: Order): String {
        return paymentMethod.paymentRequest(order)
    }
}