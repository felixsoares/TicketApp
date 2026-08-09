package com.mobile.felix.ticketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.mobile.felix.ticketapp.core.presentation.Navigation
import com.mobile.felix.ticketapp.core.presentation.Route
import com.mobile.felix.ticketapp.feature.home.presentation.HomeScreen
import com.mobile.felix.ticketapp.ui.theme.TicketAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketAppTheme {
                val isVisibleBottomBar = remember { mutableStateOf(true) }
                val navController = rememberNavController()
                var selectedDestination by rememberSaveable { mutableIntStateOf(Route.Home.hashCode()) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            windowInsets = NavigationBarDefaults.windowInsets,
                            modifier = Modifier.visible(isVisibleBottomBar.value)
                        ) {

                            NavigationBarItem(
                                selected = selectedDestination == Route.Home.hashCode(),
                                onClick = {
                                    selectedDestination = Route.Home.hashCode()
                                    navController.navigate(Route.Home)
                                },
                                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                label = { Text("Home") }
                            )

                            NavigationBarItem(
                                selected = selectedDestination == Route.Tickets.hashCode(),
                                onClick = {
                                    selectedDestination = Route.Tickets.hashCode()
                                    navController.navigate(Route.Tickets)
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.ShoppingBasket,
                                        contentDescription = null
                                    )
                                },
                                label = { Text("Tickets") }
                            )

                        }
                    }
                ) { innerPadding ->
                    Navigation(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        isBottomBarVisible = { isVisible ->
                            isVisibleBottomBar.value = isVisible
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TicketAppTheme {
        HomeScreen() { }
    }
}