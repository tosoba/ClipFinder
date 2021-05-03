package com.clipfinder.core.android.youtube.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.clipfinder.core.android.youtube.db.model.SearchResponseEntity
import io.reactivex.Maybe

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(responseEntity: SearchResponseEntity)

    @Query(
        "SELECT * FROM search_response WHERE `query`=:query AND (page_token=:pageToken OR (page_token IS NULL AND :pageToken IS NULL))"
    )
    fun selectByQueryAndPageToken(query: String, pageToken: String?): Maybe<SearchResponseEntity>

    @Query(
        "SELECT * FROM search_response WHERE `related_video_id`=:relatedVideoId AND (page_token=:pageToken OR (page_token IS NULL AND :pageToken IS NULL))"
    )
    fun selectByRelatedVideoIdAndPageToken(
        relatedVideoId: String,
        pageToken: String?
    ): Maybe<SearchResponseEntity>

    @Query(
        "DELETE FROM search_response WHERE `query`=:query AND (page_token=:pageToken OR (page_token IS NULL AND :pageToken IS NULL))"
    )
    fun deleteByQueryAndPageToken(query: String, pageToken: String?)

    @Query(
        "DELETE FROM search_response WHERE `related_video_id`=:relatedVideoId AND (page_token=:pageToken OR (page_token IS NULL AND :pageToken IS NULL))"
    )
    fun deleteByRelatedVideoIdAndPageToken(relatedVideoId: String, pageToken: String?)

    @Query("DELETE FROM search_response") fun deleteAll()

    @Query("DELETE FROM search_response WHERE datetime(cached_at) < datetime('now', '-1 day')")
    fun deleteExpired()
}
