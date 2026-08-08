package com.mobile.felix.ticketapp.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.felix.ticketapp.core.data.local.dao.EventDao
import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity

@Database(
    entities = [
        EventEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}