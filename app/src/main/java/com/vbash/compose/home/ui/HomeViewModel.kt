package com.vbash.compose.home.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbash.compose.home.domain.Payment
import com.vbash.compose.home.domain.PaymentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PaymentsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        uploadData()
    }

    fun uploadData() {
        viewModelScope.launch {
            sendAction(Action.LoadingStarted)
            repository.getPayments()
                .map { domainPayments: List<Payment> ->
                    domainPayments.map { item ->
                        val currencyInstance = Currency.getInstance(item.currency)
                        val format = NumberFormat.getCurrencyInstance().apply {
                            currency = currencyInstance
                        }
                        val formattedAmount = format.format(item.amount)

                        PaymentUi(item.id, formattedAmount, item.description)
                    }
                }
                .fold({ error ->
                    viewModelScope.launch(Dispatchers.Main.immediate) {
                        _events.emit(Event.ShowError(error.message))
                        sendAction(Action.LoadingFailed)
                    }
                }, {
                    sendAction(Action.LoadingFinished(it))
                })

        }
    }

    private fun onReduceState(action: Action): State {
        return when (action) {
            is Action.LoadingStarted -> _state.value.copy(isProgress = true)
            Action.LoadingFailed -> _state.value.copy(isProgress = false, isError = true)
            is Action.LoadingFinished -> _state.value.copy(
                isProgress = false,
                isError = false,
                data = action.list,
            )
        }
    }

    data class State(
        val isProgress: Boolean = false,
        val isError: Boolean = false,
        val data: List<PaymentUi> = emptyList(),
    )

    sealed interface Action {
        data object LoadingStarted : Action
        data object LoadingFailed : Action
        data class LoadingFinished(val list: List<PaymentUi>) : Action
    }

    private fun sendAction(action: Action) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _state.emit(onReduceState(action))
        }
    }


    sealed class Event {
        data class ShowError(@StringRes val message: Int) : Event()
    }

}

data class PaymentUi(
    val id: String,
    val currencyAmount: String,
    val description: String,
)