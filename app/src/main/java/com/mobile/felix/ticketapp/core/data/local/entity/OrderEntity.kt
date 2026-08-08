package com.mobile.felix.ticketapp.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_table")
data class OrderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val eventId: Long,
    val eventName: String,
    val eventDate: String,
    val amount: Double,
    val purchaseDate: String,
    val ticketQuantity: Int,
    val status: String
)

