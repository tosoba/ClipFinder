package com.clipfinder.spotify.api.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object OffsetDateTimeAdapter {
    @ToJson
    fun toJson(value: OffsetDateTime): String = value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    @FromJson fun fromJson(value: String): OffsetDateTime = OffsetDateTime.parse(value)
}
