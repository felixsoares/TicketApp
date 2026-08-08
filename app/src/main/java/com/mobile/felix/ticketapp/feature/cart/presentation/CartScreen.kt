package com.mobile.felix.ticketapp.feature.cart.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.core.presentation.ErrorView
import com.mobile.felix.ticketapp.core.presentation.LoadingView
import com.mobile.felix.ticketapp.feature.cart.presentation.action.CartAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreen(modifier: Modifier = Modifier, onClickItem: (Long) -> Unit = {}) {
    val viewModel: CartViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.onAction(CartAction.LoadOrders)
    }

    when {
        uiState.value.isLoading -> LoadingView(modifier)
        uiState.value.orders != null -> OrderList(modifier, orders = uiState.value.orders!!, onClickItem = onClickItem)
        else -> ErrorView(modifier)
    }
}

@Composable
fun OrderList(modifier: Modifier = Modifier, orders: List<Order>, onClickItem: (Long) -> Unit = {}) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(orders) { order ->
            OrderCard(order = order, onClick = { onClickItem(order.id) })
        }
    }
}

@Composable
fun OrderCard(order: Order, onClick: () -> Unit = {}) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.eventDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                OrderStatusChip(status = order.status)
            }

            Text(
                text = order.eventName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${order.ticketQuantity} ingresso(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "R$ ${"%.2f".format(order.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun OrderStatusChip(status: OrderStatus) {
    val (label, containerColor, contentColor) = when (status) {
        OrderStatus.APPROVED -> Triple("Aprovada", Color(0xFF2E7D32), Color.White)
        OrderStatus.DENIED -> Triple("Negada", Color(0xFFC62828), Color.White)
        OrderStatus.CANCELLED -> Triple("Cancelada", Color(0xFF616161), Color.White)
    }

    SuggestionChip(
        onClick = {},
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = containerColor
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    OrderList(
        orders = listOf(
            Order(
                id = 1L,
                eventId = 2L,
                eventName = "Festival de Verão 2026",
                eventDate = "15/02/2026",
                amount = 240.0,
                purchaseDate = "10/01/2026",
                ticketQuantity = 2,
                status = OrderStatus.APPROVED
            ),
            Order(
                id = 2L,
                eventId = 3L,
                eventName = "Rock Night In Concert",
                eventDate = "20/03/2026",
                amount = 180.0,
                purchaseDate = "15/02/2026",
                ticketQuantity = 1,
                status = OrderStatus.DENIED
            ),
            Order(
                id = 3L,
                eventId = 5L,
                eventName = "Noite do Humour - Stand-up Comedy",
                eventDate = "05/06/2026",
                amount = 150.0,
                purchaseDate = "01/03/2026",
                ticketQuantity = 3,
                status = OrderStatus.CANCELLED
            )
        )
    )
}

