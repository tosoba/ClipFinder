package com.example.there.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.there.data.entity.spotify.*
import com.example.there.data.entity.videos.RelatedVideoSearchDbData
import com.example.there.data.entity.videos.VideoDbData
import com.example.there.data.entity.videos.VideoPlaylistData
import com.example.there.data.entity.videos.VideoSearchDbData

@Database(entities = [
    AlbumData::class,
    ArtistData::class,
    CategoryData::class,
    PlaylistData::class,
    TrackData::class,
    VideoDbData::class,
    VideoPlaylistData::class,
    VideoSearchDbData::class,
    RelatedVideoSearchDbData::class
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
    abstract fun videoSearchDao(): VideoSearchDao
    abstract fun relatedVideoSearchDao(): RelatedVideoSearchDao
}