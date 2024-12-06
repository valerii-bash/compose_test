package com.vbash.compose.domain

import androidx.annotation.StringRes
import com.vbash.compose.R

sealed class DomainError(@StringRes open val message: Int) {
    data class Http(@StringRes override val message: Int) : DomainError(message)
    data object Io : DomainError(R.string.no_internet_connection)
    data object Unknown : DomainError(R.string.unknown_error)
}