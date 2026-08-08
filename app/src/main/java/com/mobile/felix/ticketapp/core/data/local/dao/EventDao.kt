package com.mobile.felix.ticketapp.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Insert(onConflict = REPLACE)
    fun insertData(data: List<EventEntity>)

    @Query("SELECT * FROM event")
    suspend fun getAll(): List<EventEntity>

    @Query("SELECT * FROM event WHERE id = :eventId")
    suspend fun getById(eventId: Long): EventEntity
}