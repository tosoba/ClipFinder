package com.example.db

import androidx.room.*
import com.example.db.model.spotify.*
import com.example.db.model.videos.RelatedVideoSearchDbModel
import com.example.db.model.videos.VideoDbModel
import com.example.db.model.videos.VideoPlaylistDbModel
import com.example.db.model.videos.VideoSearchDbModel
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
interface AlbumDao : BaseDao<AlbumDbModel> {
    @Query("SELECT * FROM albums")
    fun findAll(): Flowable<List<AlbumDbModel>>

    @Query("SELECT * FROM albums WHERE id = :id")
    fun findById(id: String): Maybe<AlbumDbModel>
}

@Dao
interface ArtistDao : BaseDao<ArtistDbModel> {
    @Query("SELECT * FROM artists")
    fun findAll(): Flowable<List<ArtistDbModel>>

    @Query("SELECT * FROM artists WHERE id = :id")
    fun findById(id: String): Maybe<ArtistDbModel>
}

@Dao
interface CategoryDao : BaseDao<CategoryDbModel> {
    @Query("SELECT * FROM categories")
    fun findAll(): Flowable<List<CategoryDbModel>>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun findById(id: String): Maybe<CategoryDbModel>
}

@Dao
interface SpotifyPlaylistDao : BaseDao<PlaylistDbModel> {
    @Query("SELECT * FROM spotify_playlists")
    fun findAll(): Flowable<List<PlaylistDbModel>>

    @Query("SELECT * FROM spotify_playlists WHERE id = :id")
    fun findById(id: String): Maybe<PlaylistDbModel>
}

@Dao
interface TrackDao : BaseDao<TrackDbModel> {
    @Query("SELECT * FROM tracks")
    fun findAll(): Flowable<List<TrackDbModel>>

    @Query("SELECT * FROM tracks WHERE id = :id")
    fun findById(id: String): Maybe<TrackDbModel>
}

@Dao
interface VideoDao : BaseDao<VideoDbModel> {
    @Query("SELECT * FROM videos WHERE playlist_id = :playlistId")
    fun findVideosFromPlaylist(playlistId: Long): Flowable<List<VideoDbModel>>

    @Query("SELECT * FROM videos WHERE search_query = :query")
    fun findAllWithQuery(query: String): Single<List<VideoDbModel>>

    @Query("SELECT * FROM videos WHERE related_video_id = :videoId")
    fun findAllRelatedToVideo(videoId: String): Single<List<VideoDbModel>>

    @Query("SELECT * FROM videos WHERE playlist_id = :playlistId LIMIT 5")
    fun find5VideosFromPlaylist(playlistId: Long): Flowable<List<VideoDbModel>>

    @Query("DELETE FROM videos WHERE playlist_id = NULL AND search_query = NULL AND related_video_id = NULL")
    fun deleteAllWithNullForeignKeys()
}

@Dao
interface VideoPlaylistDao : BaseDao<VideoPlaylistDbModel> {
    @Query("SELECT * FROM video_playlists")
    fun findAll(): Flowable<List<VideoPlaylistDbModel>>
}

@Dao
interface VideoSearchDao : BaseDao<VideoSearchDbModel> {
    @Query("SELECT next_page_token FROM video_search_data WHERE search_query = :query")
    fun getNextPageTokenForQuery(query: String): Maybe<String>

    @Query("UPDATE video_search_data SET next_page_token = :pageToken WHERE search_query = :query")
    fun updateNextPageTokenForQuery(query: String, pageToken: String?)

    @Query("DELETE FROM video_search_data")
    fun deleteAll()
}

@Dao
interface RelatedVideoSearchDao : BaseDao<RelatedVideoSearchDbModel> {
    @Query("SELECT next_page_token FROM related_video_search_data WHERE related_video_id = :videoId")
    fun getNextPageTokenForVideoId(videoId: String): Maybe<String>

    @Query("UPDATE related_video_search_data SET next_page_token = :pageToken WHERE related_video_id = :videoId")
    fun updateNextPageTokenForVideo(videoId: String, pageToken: String?)

    @Query("DELETE FROM related_video_search_data")
    fun deleteAll()
}