package com.mobile.felix.ticketapp.feature.qrCode.data.source

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QrCodeLocalDataSourceImplTest {

    private lateinit var dataSource: QrCodeLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        dataSource = QrCodeLocalDataSourceImpl()
    }

    @Test
    fun `should generate boolean matrix with specified dimensions using ZXing`() = runTest {
        // Given
        val content = "TICKET-123"
        val width = 50
        val height = 50

        // When
        val matrix = dataSource.generateMatrix(content, width, height)

        // Then
        assertNotNull(matrix)
        assertEquals(height, matrix.size)
        assertEquals(width, matrix[0].size)
    }
}

