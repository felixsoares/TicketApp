package com.mobile.felix.ticketapp.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.mobile.felix.ticketapp.core.data.local.entity.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(orders: List<OrderEntity>)

    @Insert(onConflict = ABORT)
    suspend fun insert(order: OrderEntity) : Long

    @Query("SELECT * FROM order_table")
    suspend fun getAll(): List<OrderEntity>

    @Query("SELECT * FROM order_table WHERE id = :orderId")
    suspend fun getById(orderId: Long): OrderEntity?

    @Query("SELECT * FROM order_table WHERE eventId = :eventId")
    suspend fun getByEventId(eventId: Long): OrderEntity?

    @Query("UPDATE order_table SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: String)
}

