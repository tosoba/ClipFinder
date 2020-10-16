package com.clipfinder.spotify.api.infrastructure

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

object ByteArrayAdapter {
    @ToJson
    fun toJson(data: ByteArray): String = String(data)

    @FromJson
    fun fromJson(data: String): ByteArray = data.toByteArray()
}
