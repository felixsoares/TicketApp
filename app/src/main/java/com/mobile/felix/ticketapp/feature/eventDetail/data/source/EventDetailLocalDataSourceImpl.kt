package com.mobile.felix.ticketapp.feature.eventDetail.data.source

import com.mobile.felix.ticketapp.core.data.local.dao.EventDao
import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.mapper.toDomain
import com.mobile.felix.ticketapp.feature.eventDetail.domain.source.EventDetailLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventDetailLocalDataSourceImpl(
    private val eventDao: EventDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : EventDetailLocalDataSource {
    override suspend fun getEventById(id: Long): Event = withContext(dispatcher) {
        return@withContext eventDao.getById(id).toDomain()
    }
}