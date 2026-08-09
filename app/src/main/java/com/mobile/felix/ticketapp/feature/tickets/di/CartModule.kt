package com.mobile.felix.ticketapp.feature.tickets.di

import com.mobile.felix.ticketapp.feature.tickets.data.repository.TicketsRepositoryImpl
import com.mobile.felix.ticketapp.feature.tickets.data.source.TicketsLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.tickets.data.usecase.GetOrdersUseCase
import com.mobile.felix.ticketapp.feature.tickets.data.usecase.InitOrdersUseCase
import com.mobile.felix.ticketapp.feature.tickets.domain.repository.TicketsRepository
import com.mobile.felix.ticketapp.feature.tickets.domain.source.TicketsLocalDataSource
import com.mobile.felix.ticketapp.feature.tickets.presentation.TicketsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {
    viewModel<TicketsViewModel> { TicketsViewModel(get(), get()) }
    single<TicketsRepository> { TicketsRepositoryImpl(get()) }
    single<TicketsLocalDataSource> { TicketsLocalDataSourceImpl(get()) }
    single<GetOrdersUseCase> { GetOrdersUseCase(get()) }
    single<InitOrdersUseCase> { InitOrdersUseCase(get()) }
}

