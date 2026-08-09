package com.mobile.felix.ticketapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobile.felix.ticketapp.feature.tickets.presentation.CartScreen
import com.mobile.felix.ticketapp.feature.home.presentation.HomeScreen
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.TicketDetailScreen
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.EventDetailScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(modifier: Modifier = Modifier, navController: NavHostController, isBottomBarVisible: (Boolean) -> Unit) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = modifier
    ) {
        composable<Route.Home> {
            isBottomBarVisible(true)
            HomeScreen(onNavigateToDetail = { eventId ->
                navController.navigate(Route.EventDetail(eventId))
            })
        }

        composable<Route.EventDetail> { backStackEntry ->
            isBottomBarVisible(false)
            val eventId: Long = backStackEntry.arguments?.getLong("eventId") ?: 0L
            EventDetailScreen(eventId = eventId, onNavigateToTicketDetail = { orderId ->
                navController.navigate(Route.TicketDetail(orderId))
            })
        }

        composable<Route.Tickets> {
            isBottomBarVisible(true)
            CartScreen(onClickItem = { orderId ->
                navController.navigate(Route.TicketDetail(orderId))
            })
        }

        composable<Route.TicketDetail> { backStackEntry ->
            isBottomBarVisible(false)
            val orderId: Long = backStackEntry.arguments?.getLong("orderId") ?: 0L
            TicketDetailScreen(orderId = orderId)
        }
    }
}

sealed class Route {
    @Serializable
    data object Home : Route()

    @Serializable
    data class EventDetail(val eventId: Long) : Route()

    @Serializable
    data object Tickets : Route()

    @Serializable
    data class TicketDetail(val orderId: Long) : Route()
}