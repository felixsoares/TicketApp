package com.mobile.felix.ticketapp.feature.eventDetail.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.presentation.ErrorView
import com.mobile.felix.ticketapp.core.presentation.LoadingView
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.action.EventDetailAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventDetailScreen(
    modifier: Modifier = Modifier,
    eventId: Long,
    onNavigateToTicketDetail: (Long) -> Unit
) {

    val viewModel: EventDetailViewModel = koinViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.onAction(EventDetailAction.GetEventById(eventId))
    }

    val onBuyAction: (Event, Int) -> Unit = { event, quantity ->
        viewModel.onAction(EventDetailAction.BuyTicket(event, quantity))
    }

    when {
        state.value.orderId != null -> onNavigateToTicketDetail(state.value.orderId!!)
        state.value.isLoading -> LoadingView(modifier = modifier)
        state.value.event != null -> EventDetailContent(
            state.value.event!!,
            state.value.message,
            modifier,
            onBuyAction
        )

        else -> ErrorView(modifier = modifier)
    }
}

@Composable
private fun EventDetailContent(
    event: Event,
    message: String,
    modifier: Modifier,
    onBuyAction: (event: Event, quantity: Int) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        EventDetail(event = event)
        TicketContent(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            event = event,
            onBuyAction = onBuyAction
        )

        if (message.isNotBlank()) {
            val context = LocalContext.current
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun EventDetail(event: Event) {
    Column(
        modifier = Modifier
    ) {
        EventHeader(event.poster)
        EventDescription(event)
    }
}

@Composable
fun TicketContent(
    modifier: Modifier = Modifier,
    event: Event,
    onBuyAction: (event: Event, quantity: Int) -> Unit
) {
    var ticketCount by remember { mutableIntStateOf(1) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        TicketAmount(
            ticketCount,
            onIncrement = { ticketCount++ },
            onDecrement = { if (ticketCount > 1) ticketCount-- })

        TicketTotal(
            quantity = ticketCount,
            price = event.price
        )

        Button(
            onClick = { onBuyAction(event, ticketCount) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(text = "Comprar")
        }
    }
}

@Composable
private fun TicketAmount(ticketCount: Int, onDecrement: () -> Unit, onIncrement: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Quantidade de ingressos",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = { onDecrement() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                    )
                }

                Text(
                    text = "$ticketCount",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = { onIncrement() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun TicketTotal(quantity: Int, price: Double) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Total R$ ${"%.2f".format(price * quantity)}",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EventDescription(event: Event) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = event.name,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = event.location,
            textAlign = TextAlign.Start,
            maxLines = 1,
            fontSize = 10.sp,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = event.date,
            fontSize = 10.sp,
        )

        Text(
            text = "Valor do ingresso R$ ${"%.2f".format(event.price)}"
        )

        Text(
            text = event.description,
        )
    }
}

@Composable
fun EventHeader(posterUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val posterModel = ImageRequest.Builder(LocalContext.current)
            .data(posterUrl)
            .crossfade(true)
            .build()

        AsyncImage(
            model = posterModel,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun Preview() {
    EventDetailContent(
        event = Event(
            id = 1,
            name = "Show do Rock",
            location = "São Paulo, SP",
            date = "2024-06-15",
            description = "Um show incrível de rock com bandas renomadas.",
            poster = "https://example.com/poster.jpg",
            price = 10.0
        ),
        message = "teste",
        modifier = Modifier,
        onBuyAction = { event, quantity -> }
    )
}