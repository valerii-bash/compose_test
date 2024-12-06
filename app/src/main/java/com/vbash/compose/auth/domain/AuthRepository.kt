package com.vbash.compose.auth.domain

interface AuthRepository {

    suspend fun auth(email: String, password: String): Token

}