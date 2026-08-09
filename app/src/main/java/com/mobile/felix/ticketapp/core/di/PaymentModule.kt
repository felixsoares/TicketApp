package com.mobile.felix.ticketapp.core.di

import com.mobile.felix.ticketapp.core.payment.PaymentMethod
import com.mobile.felix.ticketapp.core.payment.PaymentMethodImpl
import org.koin.dsl.module

val paymentModule = module {
    single<PaymentMethod> { PaymentMethodImpl(get()) }
}