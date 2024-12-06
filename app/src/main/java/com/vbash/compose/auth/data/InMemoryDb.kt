package com.vbash.compose.auth.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

object InMemoryDb {
    private val _db: MutableStateFlow<Boolean>

    init {
        val isLoggedIn = Random.nextInt() % 2 == 0
        _db = MutableStateFlow(true)
    }
    val db = _db.asStateFlow()

    suspend fun auth() {
        _db.emit(true)
    }
}