package com.clipfinder.core.android.youtube.db

import androidx.room.TypeConverter
import com.google.api.services.youtube.model.SearchListResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object YoutubeDbConverters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val searchListResponseAdapter: JsonAdapter<SearchListResponse> = Moshi.Builder()
        .build()
        .adapter(SearchListResponse::class.java)

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?) = value?.let { formatter.parse(value, OffsetDateTime::from) }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

    @TypeConverter
    @JvmStatic
    fun fromSearchListResponse(response: SearchListResponse): String = searchListResponseAdapter
        .toJson(response)

    @TypeConverter
    @JvmStatic
    fun toSearchListResponse(response: String): SearchListResponse? = searchListResponseAdapter
        .fromJson(response)
}