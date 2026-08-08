package com.mobile.felix.ticketapp.feature.home.data.usecase

import com.mobile.felix.ticketapp.feature.home.domain.source.HomeLocalDataSource

class InitDatabaseUseCase(
    private val localDataSource: HomeLocalDataSource
) {
    suspend fun invoke() {
        localDataSource.initDatabase()
    }
}