package com.example.core.android.spotify.api

import android.util.Base64

object SpotifyAuth {
    private const val id = "6dc5e6590b8b48c5bd73a64f6c206d8a"
    private const val secret = "d5c30ea11b90401980c6ca37dc0512ba"

    internal val clientDataHeader: String by lazy {
        val encoded = Base64.encodeToString("$id:$secret".toByteArray(), Base64.NO_WRAP)
        "Basic $encoded"
    }
}
