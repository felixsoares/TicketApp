package com.mobile.felix.ticketapp.feature.home.data.repository

import com.mobile.felix.ticketapp.core.domain.Event
import com.mobile.felix.ticketapp.feature.home.domain.repository.HomeRepository
import com.mobile.felix.ticketapp.feature.home.domain.source.HomeLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val localDataSource: HomeLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeRepository {

    override suspend fun getEvents(): List<Event> = withContext(dispatcher) {
        return@withContext localDataSource.getEvents()
    }

}