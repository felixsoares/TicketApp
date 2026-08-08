package com.mobile.felix.ticketapp.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.presentation.ErrorView
import com.mobile.felix.ticketapp.core.presentation.LoadingView
import com.mobile.felix.ticketapp.feature.home.presentation.action.HomeAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onClickItem: (Long) -> Unit) {

    val viewModel: HomeViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.onAction(HomeAction.GetEvents)
    }

    if (uiState.value.isLoading) {
        LoadingView(modifier)
    } else if (uiState.value.events != null) {
        EventList(modifier, events = uiState.value.events!!, onClickItem)
    } else {
        ErrorView(modifier)
    }
}

@Composable
fun EventList(modifier: Modifier = Modifier, events: List<Event>, onClickItem: (Long) -> Unit) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(events) { event ->
            EventCard(event = event, onClickItem)
        }
    }
}

@Composable
fun EventCard(event: Event, onClickItem: (Long) -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = { onClickItem(event.id) },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val posterModel = ImageRequest.Builder(LocalContext.current)
                .data(event.poster)
                .crossfade(true)
                .build()

            AsyncImage(
                model = posterModel,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = event.name,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = event.location,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = event.date,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    EventList(
        events = listOf(
            Event(
                id = 2L,
                name = "Festival de Verão 2026",
                date = "15/02/2026",
                location = "Arena Anhembi - São Paulo, SP",
                poster = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500",
                description = "Grande festival com mais de 10 atrações nacionais e internacionais no palco principal."
            ),

            Event(
                id = 4L,
                name = "Conferência Internacional de Tecnologia, Inovação e Inteligência Artificial 2026",
                date = "10/05/2026",
                location = "Centro de Convenções Pro Magno - São Paulo, SP",
                poster = "https://images.unsplash.com/photo-1585699324551-f6c309eedeca?w=500",
                description = "Evento focado nas novas tendências do mercado de desenvolvimento, arquitetura de software, computação em nuvem e novos modelos de IA."
            ),
        ), onClickItem = {}
    )
}