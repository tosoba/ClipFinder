package com.clipfinder.core.android.youtube.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.clipfinder.core.youtube.model.SearchType
import com.google.api.services.youtube.model.SearchListResponse
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "search_response",
    indices =
        [
            Index(value = ["query", "page_token"], unique = true),
            Index(value = ["related_video_id", "page_token"], unique = true)]
)
data class SearchResponseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String? = null,
    val searchType: SearchType,
    @ColumnInfo(name = "page_token") val pageToken: String? = null,
    val content: SearchListResponse,
    @ColumnInfo(name = "cached_at") val cachedAt: OffsetDateTime,
    @ColumnInfo(name = "related_video_id") val relatedVideoId: String? = null
)
