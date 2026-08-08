package com.mobile.felix.ticketapp.feature.home.presentation.action

sealed interface HomeAction {
    data object Idle : HomeAction
    data object GetEvents : HomeAction
}