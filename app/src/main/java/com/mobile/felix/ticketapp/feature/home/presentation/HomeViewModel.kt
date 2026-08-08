package com.mobile.felix.ticketapp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.feature.home.data.usecase.GetEventsUseCase
import com.mobile.felix.ticketapp.feature.home.data.usecase.InitDatabaseUseCase
import com.mobile.felix.ticketapp.feature.home.presentation.action.HomeAction
import com.mobile.felix.ticketapp.feature.home.presentation.state.HomeUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getEventsUseCase: GetEventsUseCase,
    private val initDatabaseUseCase: InitDatabaseUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<HomeAction>()

    var uiState = MutableStateFlow(HomeUiState())
        private set

    init {
        initDatabase()

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is HomeAction.Idle -> {}
                    is HomeAction.GetEvents -> getEvents()
                }
            }
        }
    }

    private fun initDatabase() = viewModelScope.launch {
        initDatabaseUseCase.invoke()
    }

    fun getEvents() = viewModelScope.launch {
        uiState.value = uiState.value.copy(isLoading = true)
        val events = getEventsUseCase.invoke()
        uiState.value = uiState.value.copy(
            events = events,
            isLoading = false,
        )
    }

    fun onAction(action: HomeAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}