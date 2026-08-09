package com.mobile.felix.ticketapp.feature.eventDetail.di

import com.mobile.felix.ticketapp.feature.eventDetail.data.repository.EventDetailRepositoryImpl
import com.mobile.felix.ticketapp.feature.eventDetail.data.source.EventDetailLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.eventDetail.data.usecase.GetEventByIdUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.data.usecase.GetOrderByEventIdUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.data.usecase.SaveInitialOrderUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.domain.repository.EventDetailRepository
import com.mobile.felix.ticketapp.feature.eventDetail.domain.source.EventDetailLocalDataSource
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.EventDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventDetailModule = module {
    viewModel<EventDetailViewModel> { EventDetailViewModel(get(), get(), get()) }
    single<EventDetailRepository> { EventDetailRepositoryImpl(get()) }
    single<EventDetailLocalDataSource> { EventDetailLocalDataSourceImpl(get(), get()) }
    single<GetEventByIdUseCase> { GetEventByIdUseCase(get()) }
    single<GetOrderByEventIdUseCase> { GetOrderByEventIdUseCase(get()) }
    single<SaveInitialOrderUseCase> { SaveInitialOrderUseCase(get()) }
}