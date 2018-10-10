package com.example.there.data.response

import com.example.there.data.entity.spotify.*
import com.google.gson.annotations.SerializedName

class SearchAllResponse(
        @SerializedName("albums") val albumsResult: AlbumsResult?,
        @SerializedName("artists") val artistsResult: ArtistsResult?,
        @SerializedName("playlists") val playlistsResult: PlaylistsResult?,
        @SerializedName("tracks") val tracksResult: TracksResult?
)

class TracksOnlyResponse(val tracks: List<TrackData>)

class SimilarTracksResponse(val tracks: List<SimilarTrackData>)

class ArtistsOnlyResponse(val artists: List<ArtistData>)

class CategoriesResponse(
        @SerializedName("categories") val result: CategoriesResult,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

class CategoriesResult(@SerializedName("items") val categories: List<CategoryData>)

class PlaylistsResponse(@SerializedName("playlists") val result: PlaylistsResult)

class PlaylistTracksResponse(
        @SerializedName("items") val playlistTracks: List<PlaylistTrackData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

class NewReleasesResponse(
        @SerializedName("albums") val result: AlbumsResult
)

class AlbumsResult(
        @SerializedName("items") val albums: List<AlbumData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

class ArtistsResult(
        @SerializedName("items") val artists: List<ArtistData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

class PlaylistsResult(
        @SerializedName("items") val playlists: List<PlaylistData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

class TracksResult(
        @SerializedName("items") val tracks: List<TrackData>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

class SavedTrack(
        val track: TrackData
)

class SavedTracksResult(
        @SerializedName("items") val savedTracks: List<SavedTrack>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

class SavedAlbum(
        val album: AlbumData
)

class SavedAlbumsResult(
        @SerializedName("items") val savedAlbums: List<SavedAlbum>,
        val offset: Int,
        @SerializedName("total") val totalItems: Int
)

