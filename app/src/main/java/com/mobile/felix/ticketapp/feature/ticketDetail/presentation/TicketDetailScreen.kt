package com.mobile.felix.ticketapp.feature.ticketDetail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.core.presentation.ErrorView
import com.mobile.felix.ticketapp.core.presentation.LoadingView
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.action.TicketDetailAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReceiptScreen(modifier: Modifier = Modifier, orderId: Long) {
    val viewModel: TicketDetailViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.onAction(TicketDetailAction.LoadOrder(orderId))
    }

    when {
        uiState.value.isLoading -> LoadingView(modifier)
        uiState.value.order != null -> ReceiptContent(modifier, order = uiState.value.order!!)
        else -> ErrorView(modifier)
    }
}

@Composable
fun ReceiptContent(modifier: Modifier = Modifier, order: Order) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status banner
        ReceiptStatusBanner(status = order.status)

        // Event details card
        ReceiptSection(title = "Evento") {
            ReceiptRow(label = "Nome", value = order.eventName)
            ReceiptRow(label = "Data", value = order.eventDate)
        }

        // Payment details card
        ReceiptSection(title = "Pagamento") {
            ReceiptRow(label = "Data da compra", value = order.purchaseDate)
            ReceiptRow(label = "Quantidade de ingressos", value = order.ticketQuantity.toString())
            ReceiptRow(
                label = "Valor por ingresso",
                value = "R$ ${"%.2f".format(order.amount / order.ticketQuantity)}"
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            ReceiptRow(
                label = "Total",
                value = "R$ ${"%.2f".format(order.amount)}",
                valueStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Order info card
        ReceiptSection(title = "Pedido") {
            ReceiptRow(label = "Número do pedido", value = "#${order.id}")
            ReceiptRow(label = "ID do evento", value = order.eventId.toString())
        }
    }
}

@Composable
fun ReceiptStatusBanner(status: OrderStatus) {
    val (label, backgroundColor) = when (status) {
        OrderStatus.APPROVED -> "Compra Aprovada ✓" to Color(0xFF2E7D32)
        OrderStatus.DENIED -> "Compra Negada ✗" to Color(0xFFC62828)
        OrderStatus.CANCELLED -> "Compra Cancelada" to Color(0xFF616161)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ReceiptSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider()
            content()
        }
    }
}

@Composable
fun ReceiptRow(
    label: String,
    value: String,
    valueStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = valueStyle,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptScreenPreview() {
    ReceiptContent(
        order = Order(
            id = 1L,
            eventId = 2L,
            eventName = "Festival de Verão 2026",
            eventDate = "15/02/2026",
            amount = 240.0,
            purchaseDate = "10/01/2026",
            ticketQuantity = 2,
            status = OrderStatus.APPROVED
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ReceiptScreenDeniedPreview() {
    ReceiptContent(
        order = Order(
            id = 2L,
            eventId = 3L,
            eventName = "Rock Night In Concert",
            eventDate = "20/03/2026",
            amount = 180.0,
            purchaseDate = "15/02/2026",
            ticketQuantity = 1,
            status = OrderStatus.DENIED
        )
    )
}

