package com.vbash.compose.home.data

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import arrow.retrofit.adapter.either.networkhandling.IOError
import com.vbash.api.Payment
import com.vbash.api.PaymentsApi
import com.vbash.compose.domain.DomainError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

typealias DomainRepository = com.vbash.compose.home.domain.PaymentsRepository
typealias DomainPayment = com.vbash.compose.home.domain.Payment

class PaymentsRepository @Inject constructor(
    private val api: PaymentsApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DomainRepository {

    override suspend fun getPayments(): Either<DomainError, List<DomainPayment>> {
        return withContext(dispatcher) {
            api.getCurrencies()
                .mapLeft { error -> error.mapToDomain() }
                .map { response ->
                    response.data.map { it.toDomain() }
                }
        }
    }
}

fun Payment.toDomain(): DomainPayment {
    return DomainPayment(id, amount, currency, description)
}

fun CallError.mapToDomain(): DomainError {
    return when (this) {
        is IOError -> DomainError.Io
        else -> DomainError.Unknown
    }
}

