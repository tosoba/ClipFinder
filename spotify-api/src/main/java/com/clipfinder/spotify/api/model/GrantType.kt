package com.clipfinder.spotify.api.model

enum class GrantType(val type: String) {
    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token"),
    CLIENT_CREDENTIALS("client_credentials");

    override fun toString(): String = type
}
