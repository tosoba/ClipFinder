package com.clipfinder.spotify.api.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

object UUIDAdapter {
    @ToJson fun toJson(uuid: UUID): String = uuid.toString()

    @FromJson fun fromJson(uuid: String): UUID = UUID.fromString(uuid)
}
