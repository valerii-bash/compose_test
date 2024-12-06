package com.vbash.compose.auth.data

import com.vbash.compose.auth.domain.Token
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    suspend fun auth(email: String, password: String): Token {
        delay(1000)
        InMemoryDb.auth()
        return Token("abc")
    }

}