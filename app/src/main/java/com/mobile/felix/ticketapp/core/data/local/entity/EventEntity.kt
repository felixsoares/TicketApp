package com.mobile.felix.ticketapp.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "event"
)
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val date: String,
    val price: Double,
    val location: String,
    val poster: String,
    val description: String,
)
