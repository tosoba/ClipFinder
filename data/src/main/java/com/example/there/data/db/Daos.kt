package com.example.there.data.db

import android.arch.persistence.room.*
import com.example.there.data.entities.spotify.*
import com.example.there.data.entities.videos.VideoData
import com.example.there.data.entities.videos.VideoDbData
import com.example.there.data.entities.videos.VideoPlaylistData
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
    fun findAll(): Single<List<AlbumData>>
}

@Dao
interface ArtistDao : BaseDao<ArtistData> {
    @Query("SELECT * FROM artists")
    fun findAll(): Single<List<ArtistData>>
}

@Dao
interface CategoryDao : BaseDao<CategoryData> {
    @Query("SELECT * FROM categories")
    fun findAll(): Single<List<CategoryData>>
}

@Dao
interface SpotifyPlaylistDao : BaseDao<PlaylistData> {
    @Query("SELECT * FROM spotify_playlists")
    fun findAll(): Single<List<PlaylistData>>
}

@Dao
interface TrackDao : BaseDao<TrackData> {
    @Query("SELECT * FROM tracks")
    fun findAll(): Single<List<TrackData>>
}

@Dao
interface VideoDao : BaseDao<VideoDbData> {
    @Query("SELECT * FROM videos WHERE playlist_id = :playlistId")
    fun findVideosFromPlaylist(playlistId: Long): Single<List<VideoDbData>>
}

@Dao
interface VideoPlaylistDao : BaseDao<VideoPlaylistData> {
    @Query("SELECT * FROM video_playlists")
    fun findAll(): Single<List<VideoPlaylistData>>
}