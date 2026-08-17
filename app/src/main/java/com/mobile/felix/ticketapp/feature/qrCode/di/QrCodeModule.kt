package com.mobile.felix.ticketapp.feature.qrCode.di

import com.mobile.felix.ticketapp.feature.qrCode.data.repository.QrCodeRepositoryImpl
import com.mobile.felix.ticketapp.feature.qrCode.data.source.QrCodeLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.qrCode.domain.repository.QrCodeRepository
import com.mobile.felix.ticketapp.feature.qrCode.domain.source.QrCodeLocalDataSource
import com.mobile.felix.ticketapp.feature.qrCode.domain.usecase.GenerateTicketQrCodeUseCase
import com.mobile.felix.ticketapp.feature.qrCode.presentation.QrCodeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val qrCodeModule = module {
    single<QrCodeLocalDataSource> { QrCodeLocalDataSourceImpl() }
    single<QrCodeRepository> { QrCodeRepositoryImpl(get()) }
    factory { GenerateTicketQrCodeUseCase(get(), get()) }
    viewModel { QrCodeViewModel(get(), get()) }
}

