package com.clipfinder.core.spotify.ext

import okhttp3.Request

fun Request.authorizedWith(accessToken: String): Request = newBuilder()
    .header("Authorization", "Bearer $accessToken")
    .build()
