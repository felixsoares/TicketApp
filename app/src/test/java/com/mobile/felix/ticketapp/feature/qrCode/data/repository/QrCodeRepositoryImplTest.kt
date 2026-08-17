package com.mobile.felix.ticketapp.feature.qrCode.data.repository

import com.mobile.felix.ticketapp.feature.qrCode.domain.source.QrCodeLocalDataSource
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QrCodeRepositoryImplTest {

    @MockK
    private lateinit var localDataSource: QrCodeLocalDataSource

    private lateinit var repository: QrCodeRepositoryImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = QrCodeRepositoryImpl(localDataSource)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `should delegate matrix generation to localDataSource and return QrCodeData`() = runTest {
        // Given
        val matrix = listOf(
            listOf(true, false),
            listOf(false, true)
        )
        coEvery { localDataSource.generateMatrix("CONTENT", 100, 100) } returns matrix

        // When
        val result = repository.generateQrCode(orderId = 5L, content = "CONTENT", width = 100, height = 100)

        // Then
        assertEquals(5L, result.orderId)
        assertEquals("CONTENT", result.content)
        assertEquals(100, result.width)
        assertEquals(100, result.height)
        assertEquals(matrix, result.matrix)
        coVerify(exactly = 1) { localDataSource.generateMatrix("CONTENT", 100, 100) }
    }
}

