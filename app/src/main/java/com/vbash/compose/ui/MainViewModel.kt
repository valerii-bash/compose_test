package com.vbash.compose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbash.compose.auth.data.InMemoryDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AuthenticatedStatus.Unknown)
    val state: StateFlow<AuthenticatedStatus> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            InMemoryDb.db.collectLatest { isAuth ->
                if (isAuth) _state.emit(AuthenticatedStatus.Yes)
                else _state.emit(AuthenticatedStatus.No)
            }
        }
    }

    enum class AuthenticatedStatus {
        Yes, No, Unknown
    }

}