package com.clipfinder.core.android.youtube.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.api.services.youtube.model.SearchListResponse
import org.threeten.bp.OffsetDateTime
import java.util.*

@Entity(primaryKeys = ["query", "page_token"], tableName = "search_response")
data class SearchResponseEntity(
    val query: String,

    @ColumnInfo(name = "page_token")
    val pageToken: String? = null,

    val content: SearchListResponse,

    @ColumnInfo(name = "cached_at")
    val cachedAt: OffsetDateTime,
)
