package com.mobile.felix.ticketapp.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_table")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val eventId: Long,
    val eventName: String,
    val eventDate: String,
    val eventPrice: Double,
    val purchaseDate: String,
    val ticketQuantity: Int,
    val status: String
)

