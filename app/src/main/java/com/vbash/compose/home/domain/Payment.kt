package com.vbash.compose.home.domain

data class Payment(
    val id: String,
    val amount: Float,
    val currency: String,
    val description: String,
)