package com.example.spotifyapi.model

import com.google.gson.annotations.SerializedName

class SearchAllResponse(
        @SerializedName("albums")
        val albumsResult: AlbumsResult?,

        @SerializedName("artists")
        val artistsResult: ArtistsResult?,

        @SerializedName("playlists")
        val playlistsResult: PlaylistsResult?,

        @SerializedName("tracks")
        val tracksResult: TracksResult?
)

class TracksOnlyResponse(val tracks: List<TrackApiModel>)

class SimilarTracksResponse(val tracks: List<SimilarTrackApiModel>)

class ArtistsOnlyResponse(val artists: List<ArtistApiModel>)

class CategoriesResponse(
        @SerializedName("categories")
        val result: CategoriesResult,

        val offset: Int,

        @SerializedName("total")
        val totalItems: Int
)

class CategoriesResult(@SerializedName("items") val categories: List<CategoryApiModel>)

class PlaylistsResponse(@SerializedName("playlists") val result: PlaylistsResult)

class PlaylistTracksResponse(
        @SerializedName("items")
        val playlistTracks: List<PlaylistTrackApiModel>,

        val offset: Int,

        @SerializedName("total")
        val totalItems: Int
)

class NewReleasesResponse(
        @SerializedName("albums") val result: AlbumsResult
)

class AlbumsResult(
        @SerializedName("items")
        val albums: List<AlbumApiModel>,

        val offset: Int,

        @SerializedName("total")
        val totalItems: Int
)

class ArtistsResult(
        @SerializedName("items")
        val artists: List<ArtistApiModel>,

        val offset: Int,

        @SerializedName("total")
        val totalItems: Int
)

class PlaylistsResult(
        @SerializedName("items")
        val playlists: List<PlaylistApiModel>,

        val offset: Int,

        @SerializedName("total")
        val totalItems: Int
)

class TracksResult(
        @SerializedName("items")
        val tracks: List<TrackApiModel>,

        val offset: Int,

        @SerializedName("total")
        val totalItems: Int
)

class SavedTrack(val track: TrackApiModel)

class SavedTracksResult(
        @SerializedName("items")
        val savedTracks: List<SavedTrack>,

        val offset: Int,

        @SerializedName("total")
        val totalItems: Int
)

class SavedAlbum(val album: AlbumApiModel)

class SavedAlbumsResult(
        @SerializedName("items")
        val savedAlbums: List<SavedAlbum>,

        val offset: Int,

        @SerializedName("total")
        val totalItems: Int
)

