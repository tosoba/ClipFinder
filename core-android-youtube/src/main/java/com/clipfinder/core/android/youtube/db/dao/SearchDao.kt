package com.clipfinder.core.android.youtube.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.clipfinder.core.android.youtube.db.model.SearchResponseEntity
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface SearchDao {
    @Insert
    fun insert(responseEntity: SearchResponseEntity): Completable

    @Query("SELECT * FROM search_response WHERE `query`=:query AND (page_token=:pageToken OR (page_token IS NULL AND :pageToken IS NULL))")
    fun select(query: String, pageToken: String?): Maybe<SearchResponseEntity>

    @Query("DELETE FROM search_response WHERE `query`=:query AND (page_token=:pageToken OR (page_token IS NULL AND :pageToken IS NULL))")
    fun delete(query: String, pageToken: String?): Completable

    @Query("DELETE FROM search_response")
    fun deleteAll(): Completable

    @Query("DELETE FROM search_response WHERE datetime(cached_at) < datetime('now', '-1 day')")
    fun deleteExpired(): Completable
}
