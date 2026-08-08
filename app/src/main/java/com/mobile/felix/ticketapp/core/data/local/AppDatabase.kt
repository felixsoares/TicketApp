package com.mobile.felix.ticketapp.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.felix.ticketapp.core.data.local.dao.EventDao
import com.mobile.felix.ticketapp.core.data.local.dao.OrderDao
import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity
import com.mobile.felix.ticketapp.core.data.local.entity.OrderEntity

@Database(
    entities = [
        EventEntity::class,
        OrderEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun orderDao(): OrderDao
}