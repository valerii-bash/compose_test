package com.vbash.api

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    val data: List<Payment>
)

@Serializable
data class Payment(
    val id: String,
    val amount: Float,
    val currency: String,
    val description: String,
)