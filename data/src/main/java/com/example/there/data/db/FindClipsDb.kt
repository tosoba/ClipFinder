package com.example.there.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.there.data.entities.spotify.*
import com.example.there.data.entities.videos.VideoData
import com.example.there.data.entities.videos.VideoDbData
import com.example.there.data.entities.videos.VideoPlaylistData

@Database(entities = [
    AlbumData::class,
    ArtistData::class,
    CategoryData::class,
    PlaylistData::class,
    TrackData::class,
    VideoDbData::class,
    VideoPlaylistData::class
], version = 1, exportSchema = false)
@TypeConverters(
        SimplifiedAlbumDataConverter::class,
        SimplifiedArtistDataConverter::class,
        IconDataConverter::class,
        OwnerDataConverter::class
)
abstract class FindClipsDb : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    abstract fun categoryDao(): CategoryDao
    abstract fun spotifyPlaylistDao(): SpotifyPlaylistDao
    abstract fun trackDao(): TrackDao
    abstract fun videoDao(): VideoDao
    abstract fun videoPlaylistDao(): VideoPlaylistDao
}