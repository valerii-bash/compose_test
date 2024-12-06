package com.vbash.compose.auth.domain

import com.vbash.compose.auth.data.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String, password: String): Result<Token> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching { authRepository.auth(email, password) }
        }
    }

}

@JvmInline
value class Token(val token: String)