package com.mobile.felix.ticketapp.feature.home.data.source

import com.mobile.felix.ticketapp.core.data.local.dao.EventDao
import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeLocalDataSourceImplTest {

    @MockK
    private lateinit var eventDao: EventDao

    // SUT (System Under Test)
    private lateinit var dataSource: HomeLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dataSource = HomeLocalDataSourceImpl(eventDao, Dispatchers.Unconfined)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should return mapped domain events when dao returns entities`() = runTest {
        // Given
        val entities = listOf(
            EventEntity(id = 1L, name = "Festival de Verão", date = "15/02/2026", price = 120.0, location = "SP", poster = "", description = "Desc"),
            EventEntity(id = 2L, name = "Rock Night", date = "20/03/2026", price = 80.0, location = "RJ", poster = "", description = "Desc")
        )
        coEvery { eventDao.getAll() } returns entities

        // When
        val result = dataSource.getEvents()

        // Then
        assertEquals(2, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("Festival de Verão", result[0].name)
        assertEquals(2L, result[1].id)
        assertEquals("Rock Night", result[1].name)
        coVerify(exactly = 1) { eventDao.getAll() }
    }

    @Test
    fun `should return empty list when dao returns no entities`() = runTest {
        // Given
        coEvery { eventDao.getAll() } returns emptyList()

        // When
        val result = dataSource.getEvents()

        // Then
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { eventDao.getAll() }
    }

    @Test
    fun `should call insertData on dao when initDatabase is invoked`() = runTest {
        // Given — relaxUnitFun handles insertData automatically

        // When
        dataSource.initDatabase()

        // Then
        verify(exactly = 1) { eventDao.insertData(any()) }
    }

    @Test
    fun `should propagate exception when dao throws on getAll`() = runTest {
        // Given
        val exception = RuntimeException("Database read error")
        coEvery { eventDao.getAll() } throws exception

        // When
        val result = runCatching { dataSource.getEvents() }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Database read error", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { eventDao.getAll() }
    }
}


