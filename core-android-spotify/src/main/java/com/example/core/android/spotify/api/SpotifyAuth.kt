package com.example.core.android.spotify.api

import android.util.Base64

object SpotifyAuth {
    const val ID = "6dc5e6590b8b48c5bd73a64f6c206d8a"
    private const val SECRET = "d5c30ea11b90401980c6ca37dc0512ba"
    const val REDIRECT_URI = "testschema://callback"

    val scopes = arrayOf(
        "user-read-private",
        "user-library-read",
        "user-top-read",
        "playlist-read",
        "playlist-read-private",
        "streaming",
        "user-read-birthdate",
        "user-read-email"
    )

    internal val clientDataHeader: String by lazy {
        val encoded = Base64.encodeToString("$ID:$SECRET".toByteArray(), Base64.NO_WRAP)
        "Basic $encoded"
    }
}
