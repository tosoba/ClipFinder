package com.clipfinder.core.spotify.provider

interface AccessTokenProvider {
    val token: String
    operator fun invoke(): String = token
}
