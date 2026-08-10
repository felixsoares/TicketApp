package com.mobile.felix.ticketapp.feature.home.data.usecase

import com.mobile.felix.ticketapp.feature.home.domain.source.HomeLocalDataSource
import com.mobile.felix.ticketapp.feature.home.domain.usecase.InitDatabaseUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InitDatabaseUseCaseTest {

    @MockK
    private lateinit var localDataSource: HomeLocalDataSource

    // SUT (System Under Test)
    private lateinit var useCase: InitDatabaseUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = InitDatabaseUseCase(localDataSource)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should call initDatabase on local data source when invoked`() = runTest {
        // Given — relaxUnitFun handles the Unit-returning suspend function automatically

        // When
        useCase.invoke()

        // Then
        coVerify(exactly = 1) { localDataSource.initDatabase() }
    }

    @Test
    fun `should not call initDatabase more than once per invocation`() = runTest {
        // Given

        // When
        useCase.invoke()

        // Then
        coVerify(exactly = 1) { localDataSource.initDatabase() }
    }

    @Test
    fun `should propagate exception when local data source throws on initDatabase`() = runTest {
        // Given
        val exception = RuntimeException("Database init failed")
        coEvery { localDataSource.initDatabase() } throws exception

        // When
        val result = runCatching { useCase.invoke() }

        // Then
        assertTrue(result.isFailure)
        assertEquals("Database init failed", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { localDataSource.initDatabase() }
    }
}

