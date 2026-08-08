package com.mobile.felix.ticketapp.core.di

import androidx.room.Room
import com.mobile.felix.ticketapp.core.data.local.AppDatabase
import com.mobile.felix.ticketapp.core.data.local.dao.EventDao
import com.mobile.felix.ticketapp.core.data.local.dao.OrderDao
import org.koin.dsl.module

const val TICKETAPP_DATABASE_NAME = "ticketapp_database"

val localModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            TICKETAPP_DATABASE_NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single<EventDao> { get<AppDatabase>().eventDao() }
    single<OrderDao> { get<AppDatabase>().orderDao() }
}

