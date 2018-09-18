package com.example.there.data.response

import com.example.there.data.entity.spotify.*
import com.google.gson.annotations.SerializedName

data class SearchAllResponse(
        @SerializedName("albums") val albumsResult: AlbumsResult?,
        @SerializedName("artists") val artistsResult: ArtistsResult?,
        @SerializedName("playlists") val playlistsResult: PlaylistsResult?,
        @SerializedName("tracks") val tracksResult: TracksResult?
)

data class TracksOnlyResponse(val tracks: List<TrackData>)

data class SimilarTracksResponse(val tracks: List<SimilarTrackData>)

data class ArtistsOnlyResponse(val artists: List<ArtistData>)

data class CategoriesResponse(
        @SerializedName("categories") val result: CategoriesResult,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

data class CategoriesResult(@SerializedName("items") val categories: List<CategoryData>)

data class PlaylistsResponse(@SerializedName("playlists") val result: PlaylistsResult)

data class PlaylistTracksResponse(
        @SerializedName("items") val playlistTracks: List<PlaylistTrackData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

data class NewReleasesResponse(
        @SerializedName("albums") val result: AlbumsResult
)

data class AlbumsResult(
        @SerializedName("items") val albums: List<AlbumData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

data class ArtistsResult(
        @SerializedName("items") val artists: List<ArtistData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

data class PlaylistsResult(
        @SerializedName("items") val playlists: List<PlaylistData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

data class TracksResult(
        @SerializedName("items") val tracks: List<TrackData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

data class SavedTrack(
        val track: TrackData
)

data class SavedTracksResult(
        @SerializedName("items") val savedTracks: List<SavedTrack>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

data class SavedAlbum(
        val album: AlbumData
)

data class SavedAlbumsResult(
        @SerializedName("items") val savedAlbums: List<SavedAlbum>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

