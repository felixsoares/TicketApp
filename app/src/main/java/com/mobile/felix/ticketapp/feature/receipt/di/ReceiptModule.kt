package com.mobile.felix.ticketapp.feature.receipt.di

import com.mobile.felix.ticketapp.feature.receipt.data.repository.ReceiptRepositoryImpl
import com.mobile.felix.ticketapp.feature.receipt.data.source.ReceiptLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.receipt.data.usecase.GetOrderByIdUseCase
import com.mobile.felix.ticketapp.feature.receipt.domain.repository.ReceiptRepository
import com.mobile.felix.ticketapp.feature.receipt.domain.source.ReceiptLocalDataSource
import com.mobile.felix.ticketapp.feature.receipt.presentation.ReceiptViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val receiptModule = module {
    viewModel<ReceiptViewModel> { ReceiptViewModel(get()) }
    single<ReceiptRepository> { ReceiptRepositoryImpl(get()) }
    single<ReceiptLocalDataSource> { ReceiptLocalDataSourceImpl(get()) }
    single<GetOrderByIdUseCase> { GetOrderByIdUseCase(get()) }
}

