package com.mobile.felix.ticketapp.feature.eventDetail.presentation

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
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
import com.google.gson.Gson
import com.mobile.felix.ticketapp.R
import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.domain.model.payment.Item
import com.mobile.felix.ticketapp.core.domain.model.payment.OrderRequest
import com.mobile.felix.ticketapp.core.presentation.ErrorView
import com.mobile.felix.ticketapp.core.presentation.LoadingView
import com.mobile.felix.ticketapp.core.util.getBase64
import com.mobile.felix.ticketapp.core.util.startForegroundServiceAndLaunchDeepLink
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.action.EventDetailAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun TicketScreen(modifier: Modifier = Modifier, eventId: Long) {

    val viewModel: EventDetailViewModel = koinViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.onAction(EventDetailAction.GetEventById(eventId))
    }

    if (state.value.isLoading) {
        LoadingView(modifier = modifier)
    } else if (state.value.event != null) {
        val event = state.value.event!!
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            EventContent(event = event)
            TicketContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                event = event,
            )
        }
    } else {
        ErrorView(modifier = modifier)
    }
}

@Composable
fun EventContent(event: Event) {
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
    event: Event
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        TicketAmount()
        Button(
            onClick = { makePayment(context = context, event = event, ticketCount = 1) },
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
private fun TicketAmount() {
    var ticketCount by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .background(
                color = Color(0xFFF2F0EF),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            text = "Quantidade de ingressos",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = { if (ticketCount >= 1) ticketCount-- },
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
                onClick = { ticketCount++ },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
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

private fun makePayment(context: Context, event: Event, ticketCount: Int) {
    val reference = "uriapp #" + (System.currentTimeMillis() / 1000)
    val callbackUrlSameActivity by lazy {
        "${context.getString(R.string.intent_scheme)}://${
            context.getString(
                R.string.intent_host
            )
        }"
    }

    val price = (500L..1000L).random()
    val randomSku: Int = (1000..100000).random()
    val item = Item(
        sku = randomSku.toString(),
        name = event.name,
        unitPrice = price,
        quantity = ticketCount,
        unitOfMeasure = "unidade"
    )
    val items = mutableListOf(item)

    val request = OrderRequest(
        "xxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxx",
        price * ticketCount,
        null,
        1,
        "felix@email.br",
        null,
        reference,
        items,
    )

    val json = Gson().toJson(request).toString()
    val base64 = getBase64(json)
    val checkoutUri = "lio://payment?request=$base64&urlCallback=$callbackUrlSameActivity"
    startForegroundServiceAndLaunchDeepLink(context, checkoutUri)
}

@Preview
@Composable
fun Preview() {
    EventContent(
        event = Event(
            id = 1,
            name = "Show do Rock",
            location = "São Paulo, SP",
            date = "2024-06-15",
            description = "Um show incrível de rock com bandas renomadas.",
            poster = "https://example.com/poster.jpg"
        )
    )
}