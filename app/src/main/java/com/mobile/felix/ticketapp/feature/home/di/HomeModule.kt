package com.mobile.felix.ticketapp.feature.home.di

import com.mobile.felix.ticketapp.feature.home.data.repository.HomeRepositoryImpl
import com.mobile.felix.ticketapp.feature.home.data.source.HomeLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.home.data.usecase.GetEventsUseCase
import com.mobile.felix.ticketapp.feature.home.data.usecase.InitDatabaseUseCase
import com.mobile.felix.ticketapp.feature.home.domain.repository.HomeRepository
import com.mobile.felix.ticketapp.feature.home.domain.source.HomeLocalDataSource
import com.mobile.felix.ticketapp.feature.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel<HomeViewModel> { HomeViewModel(get(), get()) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    single<HomeLocalDataSource> { HomeLocalDataSourceImpl(get()) }
    single<GetEventsUseCase> { GetEventsUseCase(get()) }
    single<InitDatabaseUseCase> { InitDatabaseUseCase(get()) }
}