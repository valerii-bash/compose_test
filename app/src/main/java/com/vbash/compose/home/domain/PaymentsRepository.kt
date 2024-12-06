package com.vbash.compose.home.domain

import arrow.core.Either
import com.vbash.compose.domain.DomainError

interface PaymentsRepository {

    suspend fun getPayments(): Either<DomainError, List<Payment>>

}