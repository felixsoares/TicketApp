package com.mobile.felix.ticketapp.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.mobile.felix.ticketapp.core.data.local.entity.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(orders: List<OrderEntity>)

    @Query("SELECT * FROM order_table")
    suspend fun getAll(): List<OrderEntity>

    @Query("SELECT * FROM order_table WHERE id = :orderId")
    suspend fun getById(orderId: Long): OrderEntity
}

