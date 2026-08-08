package com.mobile.felix.ticketapp.core.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.mobile.felix.ticketapp.core.data.local.AppDatabase
import com.mobile.felix.ticketapp.core.data.local.dao.EventDao
import org.koin.dsl.module

const val TICKETAPP_DATABASE_NAME = "ticketapp_database"

val localModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            TICKETAPP_DATABASE_NAME
        )
            .build()
    }

    single<EventDao> { get<AppDatabase>().eventDao() }
}