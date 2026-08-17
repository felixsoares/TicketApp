package com.mobile.felix.ticketapp

import android.app.Application
import com.mobile.felix.ticketapp.core.di.localModule
import com.mobile.felix.ticketapp.core.di.paymentModule
import com.mobile.felix.ticketapp.feature.eventDetail.di.eventDetailModule
import com.mobile.felix.ticketapp.feature.home.di.homeModule
import com.mobile.felix.ticketapp.feature.qrCode.di.qrCodeModule
import com.mobile.felix.ticketapp.feature.ticketDetail.di.ticketDetailModule
import com.mobile.felix.ticketapp.feature.tickets.di.ticketsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                localModule, homeModule, eventDetailModule, ticketsModule, ticketDetailModule,
                paymentModule, qrCodeModule
            )
        }
    }
}