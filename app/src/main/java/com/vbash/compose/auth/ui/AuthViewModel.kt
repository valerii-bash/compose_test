package com.vbash.compose.auth.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbash.compose.R
import com.vbash.compose.auth.domain.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: AuthUseCase,
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun onEmailChange(email: String) {
        _state.value = requireNotNull(_state.value).copy(
            email = email,
            isEmailError = false,
            emailErrorMessage = ""
        )
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(
            password = password,
            isPasswordError = false,
            passwordErrorMessage = ""
        )
    }

    fun onCheckBoxChecked(checked: Boolean) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(isCheckBoxChecked = checked))
        }
    }

    fun onLoginBtnClick() {
        viewModelScope.launch {
            if (!_state.value.isCheckBoxChecked) {
                _events.emit(Event.ShowError(R.string.select_checkbox))
                return@launch
            }
            if (state.value.email.isBlank() || state.value.email.length < 3) {
                _state.emit(
                    state.value.copy(
                        isEmailError = true,
                        emailErrorMessage = "Email too short"
                    )
                )

                return@launch
            }
            if (state.value.password.isBlank() || state.value.password.length < 3) {
                _state.emit(
                    state.value.copy(
                        isPasswordError = true,
                        passwordErrorMessage = "Password too short"
                    )
                )
                return@launch
            }
            viewModelScope.launch {
                auth(state.value.email, state.value.password).fold({

                }, {

                })
            }
        }
    }

    data class AuthState(
        val email: String = "",
        val isEmailError: Boolean = false,
        val emailErrorMessage: String = "",
        val password: String = "",
        val isPasswordError: Boolean = false,
        val isCheckBoxChecked: Boolean = false,
        val passwordErrorMessage: String = "",
    )

    sealed class Event {
        data class ShowError(@StringRes val message: Int) : Event()
    }

}