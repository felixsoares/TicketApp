package com.mobile.felix.ticketapp.feature.ticket.di

import com.mobile.felix.ticketapp.feature.ticket.data.repository.TicketRepositoryImpl
import com.mobile.felix.ticketapp.feature.ticket.data.source.TicketLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.ticket.data.usecase.GetEventByIdUseCase
import com.mobile.felix.ticketapp.feature.ticket.domain.repository.TicketRepository
import com.mobile.felix.ticketapp.feature.ticket.domain.source.TicketLocalDataSource
import com.mobile.felix.ticketapp.feature.ticket.presentation.TicketViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ticketModule = module {
    viewModel<TicketViewModel> { TicketViewModel(get()) }
    single<TicketRepository> { TicketRepositoryImpl(get()) }
    single<TicketLocalDataSource> { TicketLocalDataSourceImpl(get()) }
    single<GetEventByIdUseCase> { GetEventByIdUseCase(get()) }
}