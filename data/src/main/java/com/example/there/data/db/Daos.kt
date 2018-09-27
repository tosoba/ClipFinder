package com.example.there.data.db

import android.arch.persistence.room.*
import com.example.there.data.entity.spotify.*
import com.example.there.data.entity.videos.RelatedVideoSearchDbData
import com.example.there.data.entity.videos.VideoDbData
import com.example.there.data.entity.videos.VideoPlaylistData
import com.example.there.data.entity.videos.VideoSearchDbData
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(vararg t: T): Array<Long>

    @Delete
    fun delete(t: T): Int

    @Delete
    fun deleteMany(vararg t: T)
}

@Dao
interface AlbumDao : BaseDao<AlbumData> {
    @Query("SELECT * FROM albums")
    fun findAll(): Flowable<List<AlbumData>>
}

@Dao
interface ArtistDao : BaseDao<ArtistData> {
    @Query("SELECT * FROM artists")
    fun findAll(): Flowable<List<ArtistData>>
}

@Dao
interface CategoryDao : BaseDao<CategoryData> {
    @Query("SELECT * FROM categories")
    fun findAll(): Flowable<List<CategoryData>>
}

@Dao
interface SpotifyPlaylistDao : BaseDao<PlaylistData> {
    @Query("SELECT * FROM spotify_playlists")
    fun findAll(): Flowable<List<PlaylistData>>
}

@Dao
interface TrackDao : BaseDao<TrackData> {
    @Query("SELECT * FROM tracks")
    fun findAll(): Flowable<List<TrackData>>
}

@Dao
interface VideoDao : BaseDao<VideoDbData> {
    @Query("SELECT * FROM videos WHERE playlist_id = :playlistId")
    fun findVideosFromPlaylist(playlistId: Long): Flowable<List<VideoDbData>>

    @Query("SELECT * FROM videos WHERE search_query = :query")
    fun findAllWithQuery(query: String): Single<List<VideoDbData>>

    @Query("SELECT * FROM videos WHERE related_video_id = :videoId")
    fun findAllRelatedToVideo(videoId: String): Single<List<VideoDbData>>

    @Query("SELECT * FROM videos WHERE playlist_id = :playlistId LIMIT 5")
    fun find5VideosFromPlaylist(playlistId: Long): Flowable<List<VideoDbData>>
}

@Dao
interface VideoPlaylistDao : BaseDao<VideoPlaylistData> {
    @Query("SELECT * FROM video_playlists")
    fun findAll(): Flowable<List<VideoPlaylistData>>
}

@Dao
interface VideoSearchDao : BaseDao<VideoSearchDbData> {
    @Query("SELECT next_page_token FROM video_search_data WHERE search_query = :query")
    fun getNextPageTokenForQuery(query: String): Maybe<String>

    @Query("UPDATE video_search_data SET next_page_token = :pageToken WHERE search_query = :query")
    fun updateNextPageTokenForQuery(query: String, pageToken: String?)

    @Query("DELETE FROM video_search_data")
    fun deleteAll()
}

@Dao
interface RelatedVideoSearchDao : BaseDao<RelatedVideoSearchDbData> {
    @Query("SELECT next_page_token FROM related_video_search_data WHERE related_video_id = :videoId")
    fun getNextPageTokenForVideoId(videoId: String): Maybe<String>

    @Query("UPDATE related_video_search_data SET next_page_token = :pageToken WHERE related_video_id = :videoId")
    fun updateNextPageTokenForVideo(videoId: String, pageToken: String?)

    @Query("DELETE FROM related_video_search_data")
    fun deleteAll()
}