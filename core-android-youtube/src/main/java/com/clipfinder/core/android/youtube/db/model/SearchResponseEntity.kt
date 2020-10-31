package com.clipfinder.core.android.youtube.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.api.services.youtube.model.SearchListResponse
import org.threeten.bp.OffsetDateTime
import java.util.*

@Entity(
    tableName = "search_response",
    indices = [Index(value = ["query", "page_token"], unique = true)]
)
data class SearchResponseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val query: String,

    @ColumnInfo(name = "page_token")
    val pageToken: String? = null,

    val content: SearchListResponse,

    @ColumnInfo(name = "cached_at")
    val cachedAt: OffsetDateTime
)