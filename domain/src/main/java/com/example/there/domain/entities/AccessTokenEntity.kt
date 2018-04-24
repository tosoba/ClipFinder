package com.example.there.domain.entities

data class AccessTokenEntity(
        val token: String,
        val timestamp: Long
) {
    val isValid: Boolean
        get() = System.currentTimeMillis() - timestamp < 3600 * 1000
}