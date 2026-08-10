package com.mobile.felix.ticketapp.feature.tickets.di

import com.mobile.felix.ticketapp.feature.tickets.data.repository.TicketsRepositoryImpl
import com.mobile.felix.ticketapp.feature.tickets.data.source.TicketsLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.tickets.domain.usecase.GetOrdersUseCase
import com.mobile.felix.ticketapp.feature.tickets.domain.repository.TicketsRepository
import com.mobile.felix.ticketapp.feature.tickets.domain.source.TicketsLocalDataSource
import com.mobile.felix.ticketapp.feature.tickets.presentation.TicketsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ticketsModule = module {
    viewModel<TicketsViewModel> { TicketsViewModel(get()) }
    single<TicketsRepository> { TicketsRepositoryImpl(get()) }
    single<TicketsLocalDataSource> { TicketsLocalDataSourceImpl(get()) }
    single<GetOrdersUseCase> { GetOrdersUseCase(get()) }
}

