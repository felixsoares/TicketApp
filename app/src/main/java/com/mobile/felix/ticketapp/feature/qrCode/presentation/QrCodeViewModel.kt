package com.mobile.felix.ticketapp.feature.qrCode.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.qrCode.domain.usecase.GenerateTicketQrCodeUseCase
import com.mobile.felix.ticketapp.feature.qrCode.presentation.action.QrCodeAction
import com.mobile.felix.ticketapp.feature.qrCode.presentation.state.QrCodeUiState
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase.GetOrderByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QrCodeViewModel(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val generateTicketQrCodeUseCase: GenerateTicketQrCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QrCodeUiState())
    val uiState: StateFlow<QrCodeUiState> = _uiState.asStateFlow()

    fun onAction(action: QrCodeAction) {
        when (action) {
            is QrCodeAction.LoadQrCode -> loadQrCode(action.orderId)
        }
    }

    private fun loadQrCode(orderId: Long) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        val order = getOrderByIdUseCase(orderId)

        if (order == null) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Pedido não encontrado"
            )
            return@launch
        }

        if (order.status != OrderStatus.APPROVED) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                order = order,
                isPaid = false,
                errorMessage = "O QR Code só é gerado para ingressos com pagamento aprovado."
            )
            return@launch
        }

        val qrResult = generateTicketQrCodeUseCase(orderId)
        qrResult.fold(
            onSuccess = { qrData ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    order = order,
                    qrCodeData = qrData,
                    isPaid = true,
                    errorMessage = null
                )
            },
            onFailure = { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    order = order,
                    isPaid = false,
                    errorMessage = error.message ?: "Erro ao gerar QR Code"
                )
            }
        )
    }
}

