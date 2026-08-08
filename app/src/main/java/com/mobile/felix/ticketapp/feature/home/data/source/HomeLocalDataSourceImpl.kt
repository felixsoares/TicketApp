package com.mobile.felix.ticketapp.feature.home.data.source

import com.mobile.felix.ticketapp.core.data.database.MockDataBase
import com.mobile.felix.ticketapp.core.data.local.dao.EventDao
import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.mapper.toDomain
import com.mobile.felix.ticketapp.feature.home.domain.source.HomeLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeLocalDataSourceImpl(
    private val eventDao: EventDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeLocalDataSource {

    override suspend fun getEvents(): List<Event> = withContext(dispatcher) {
        val result = eventDao.getAll()
        val events = result.map { entity -> entity.toDomain() }
        return@withContext events
    }

    override suspend fun initDatabase() = withContext(dispatcher) {
        eventDao.insertData(MockDataBase.events)
    }
}