package com.mobile.felix.ticketapp.feature.qrCode.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.core.presentation.ErrorView
import com.mobile.felix.ticketapp.core.presentation.LoadingView
import com.mobile.felix.ticketapp.feature.qrCode.domain.model.QrCodeData
import com.mobile.felix.ticketapp.feature.qrCode.presentation.action.QrCodeAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun QrCodeScreen(
    modifier: Modifier = Modifier,
    orderId: Long
) {
    val viewModel: QrCodeViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.onAction(QrCodeAction.LoadQrCode(orderId))
    }

    when {
        uiState.value.isLoading -> LoadingView(modifier)
        uiState.value.order != null -> QrCodeContent(
            modifier = modifier,
            order = uiState.value.order!!,
            qrCodeData = uiState.value.qrCodeData,
            isPaid = uiState.value.isPaid,
            errorMessage = uiState.value.errorMessage
        )
        else -> ErrorView(modifier)
    }
}

@Composable
fun QrCodeContent(
    modifier: Modifier = Modifier,
    order: Order,
    qrCodeData: QrCodeData?,
    isPaid: Boolean,
    errorMessage: String?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QrCodeCard(
            order = order,
            qrCodeData = qrCodeData,
            isPaid = isPaid,
            errorMessage = errorMessage
        )
    }
}

@Composable
fun QrCodeCard(
    order: Order,
    qrCodeData: QrCodeData?,
    isPaid: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RowHeader(eventName = order.eventName, eventDate = order.eventDate)

            HorizontalDivider()

            if (isPaid && qrCodeData != null) {
                Text(
                    text = "Apresente este QR Code na entrada do evento",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center
                )

                QrCodeImage(
                    matrix = qrCodeData.matrix,
                    modifier = Modifier
                        .size(240.dp)
                        .padding(8.dp)
                )

                Text(
                    text = "Código do Ingresso: ${qrCodeData.content}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            } else {
                UnpaidTicketMessage(
                    status = order.status,
                    message = errorMessage ?: "QR Code indisponível para pedidos não pagos."
                )
            }

            HorizontalDivider()

            Text(
                text = "Pedido #${order.id} • ${order.ticketQuantity} ingresso(s)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun RowHeader(eventName: String, eventDate: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.QrCode,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = "Ingresso Digital",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = eventName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = eventDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun UnpaidTicketMessage(status: OrderStatus, message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = "QR Code Indisponível",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QrCodeImage(
    matrix: List<List<Boolean>>,
    modifier: Modifier = Modifier
) {
    val imageBitmap = remember(matrix) {
        val height = matrix.size
        val width = if (height > 0) matrix[0].size else 0
        if (width == 0 || height == 0) return@remember null

        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val row = matrix[y]
            for (x in 0 until width) {
                pixels[y * width + x] =
                    if (row[x]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            }
        }
        val bitmap = android.graphics.Bitmap.createBitmap(
            width,
            height,
            android.graphics.Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        bitmap.asImageBitmap()
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "QR Code do Ingresso",
            modifier = modifier
        )
    } else {
        Spacer(modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun QrCodeCardPaidPreview() {
    val sampleMatrix = List(20) { y ->
        List(20) { x -> (x + y) % 2 == 0 }
    }
    QrCodeContent(
        order = Order(
            id = 100L,
            eventId = 1L,
            eventName = "Festival de Verão 2026",
            eventDate = "15/02/2026",
            price = 200.0,
            purchaseDate = "10/01/2026",
            ticketQuantity = 2,
            status = OrderStatus.APPROVED
        ),
        qrCodeData = QrCodeData(
            orderId = 100L,
            content = "TICKET-ORDER-100-EVENT-1",
            width = 20,
            height = 20,
            matrix = sampleMatrix
        ),
        isPaid = true,
        errorMessage = null
    )
}

@Preview(showBackground = true)
@Composable
fun QrCodeCardUnpaidPreview() {
    QrCodeContent(
        order = Order(
            id = 101L,
            eventId = 2L,
            eventName = "Rock In Rio",
            eventDate = "20/09/2026",
            price = 350.0,
            purchaseDate = "12/01/2026",
            ticketQuantity = 1,
            status = OrderStatus.WAITING_PAYMENT
        ),
        qrCodeData = null,
        isPaid = false,
        errorMessage = "O QR Code só é gerado para ingressos com pagamento aprovado."
    )
}




