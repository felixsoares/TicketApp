package com.mobile.felix.ticketapp.feature.ticketDetail.di

import com.mobile.felix.ticketapp.feature.ticketDetail.data.repository.TicketDetailRepositoryImpl
import com.mobile.felix.ticketapp.feature.ticketDetail.data.source.TicketDetailLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase.GetOrderByIdUseCase
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.repository.TicketDetailRepository
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.source.TicketDetailLocalDataSource
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.TicketDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ticketDetailModule = module {
    viewModel<TicketDetailViewModel> { TicketDetailViewModel(get()) }
    single<TicketDetailRepository> { TicketDetailRepositoryImpl(get()) }
    single<TicketDetailLocalDataSource> { TicketDetailLocalDataSourceImpl(get()) }
    single<GetOrderByIdUseCase> { GetOrderByIdUseCase(get()) }
}

