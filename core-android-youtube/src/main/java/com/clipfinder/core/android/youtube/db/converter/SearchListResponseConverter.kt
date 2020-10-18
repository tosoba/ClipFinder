package com.clipfinder.core.android.youtube.db.converter

import androidx.room.TypeConverter
import com.google.api.services.youtube.model.SearchListResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class SearchListResponseConverter {
    private val adapter: JsonAdapter<SearchListResponse> = Moshi.Builder().build().adapter(SearchListResponse::class.java)

    @TypeConverter
    fun previewToString(response: SearchListResponse): String = adapter.toJson(response)

    @TypeConverter
    fun stringToPreview(response: String): SearchListResponse? = adapter.fromJson(response)
}
