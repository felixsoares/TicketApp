package com.mobile.felix.ticketapp.feature.cart.di

import com.mobile.felix.ticketapp.feature.cart.data.repository.CartRepositoryImpl
import com.mobile.felix.ticketapp.feature.cart.data.source.CartLocalDataSourceImpl
import com.mobile.felix.ticketapp.feature.cart.data.usecase.GetOrdersUseCase
import com.mobile.felix.ticketapp.feature.cart.data.usecase.InitOrdersUseCase
import com.mobile.felix.ticketapp.feature.cart.domain.repository.CartRepository
import com.mobile.felix.ticketapp.feature.cart.domain.source.CartLocalDataSource
import com.mobile.felix.ticketapp.feature.cart.presentation.CartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {
    viewModel<CartViewModel> { CartViewModel(get(), get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }
    single<CartLocalDataSource> { CartLocalDataSourceImpl(get()) }
    single<GetOrdersUseCase> { GetOrdersUseCase(get()) }
    single<InitOrdersUseCase> { InitOrdersUseCase(get()) }
}

