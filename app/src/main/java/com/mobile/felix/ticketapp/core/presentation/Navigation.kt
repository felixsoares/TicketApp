package com.mobile.felix.ticketapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobile.felix.ticketapp.feature.tickets.presentation.CartScreen
import com.mobile.felix.ticketapp.feature.home.presentation.HomeScreen
import com.mobile.felix.ticketapp.feature.receipt.presentation.ReceiptScreen
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.TicketScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(modifier: Modifier = Modifier, navController: NavHostController, hideShowBottomBar: (Boolean) -> Unit) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = modifier
    ) {
        composable<Route.Home> {
            hideShowBottomBar(true)
            HomeScreen(onClickItem = { eventId ->
                navController.navigate(Route.Ticket(eventId))
            })
        }

        composable<Route.Ticket> { backStackEntry ->
            hideShowBottomBar(false)
            val eventId: Long = backStackEntry.arguments?.getLong("eventId") ?: 0L
            TicketScreen(eventId = eventId)
        }

        composable<Route.Cart> {
            hideShowBottomBar(true)
            CartScreen(onClickItem = { orderId ->
                navController.navigate(Route.Receipt(orderId))
            })
        }

        composable<Route.Receipt> { backStackEntry ->
            hideShowBottomBar(false)
            val orderId: Long = backStackEntry.arguments?.getLong("orderId") ?: 0L
            ReceiptScreen(orderId = orderId)
        }
    }
}

sealed class Route {
    @Serializable
    data object Home : Route()

    @Serializable
    data class Ticket(val eventId: Long) : Route()

    @Serializable
    data object Cart : Route()

    @Serializable
    data class Receipt(val orderId: Long) : Route()
}