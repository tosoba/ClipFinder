package com.clipfinder.core.android.youtube.db

import androidx.room.TypeConverter
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.model.SearchListResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object YoutubeDbConverters {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val gsonFactory: GsonFactory = GsonFactory.getDefaultInstance()

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?) = value?.let { formatter.parse(value, OffsetDateTime::from) }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

    @TypeConverter
    @JvmStatic
    fun fromSearchListResponse(response: SearchListResponse?): String = gsonFactory.toString(response)

    @TypeConverter
    @JvmStatic
    fun toSearchListResponse(response: String?): SearchListResponse? = response?.let {
        gsonFactory.fromString(it, SearchListResponse::class.java)
    }
}