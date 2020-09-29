package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.db.model.spotify.*
import com.example.db.model.videos.RelatedVideoSearchDbModel
import com.example.db.model.videos.VideoDbModel
import com.example.db.model.videos.VideoPlaylistDbModel
import com.example.db.model.videos.VideoSearchDbModel

@Database(
        entities = [
            AlbumDbModel::class,
            ArtistDbModel::class,
            PlaylistDbModel::class,
            TrackDbModel::class,
            VideoDbModel::class,
            VideoPlaylistDbModel::class,
            VideoSearchDbModel::class,
            RelatedVideoSearchDbModel::class
        ],
        version = 1,
        exportSchema = false
)
@TypeConverters(
        SimplifiedAlbumConverter::class,
        SimplifiedArtistConverter::class,
        StringListConverter::class
)
abstract class FindClipsDb : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    abstract fun spotifyPlaylistDao(): SpotifyPlaylistDao
    abstract fun trackDao(): TrackDao
    abstract fun videoDao(): VideoDao
    abstract fun videoPlaylistDao(): VideoPlaylistDao
    abstract fun videoSearchDao(): VideoSearchDao
    abstract fun relatedVideoSearchDao(): RelatedVideoSearchDao
}
