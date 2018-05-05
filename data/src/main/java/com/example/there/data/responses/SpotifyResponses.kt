package com.example.there.data.responses

import com.example.there.data.entities.spotify.*
import com.google.gson.annotations.SerializedName

data class SearchAllResponse(
        @SerializedName("albums") val albumsResult: AlbumsResult?,
        @SerializedName("artists") val artistsResult: ArtistsResult?,
        @SerializedName("playlists") val playlistsResult: PlaylistsResult?,
        @SerializedName("tracks") val tracksResult: TracksResult?
)

data class TracksOnlyResponse(val tracks: List<TrackData>)

data class CategoriesResponse(@SerializedName("categories") val result: CategoriesResult)

data class CategoriesResult(@SerializedName("items") val categories: List<CategoryData>)

data class AlbumsResult(@SerializedName("items") val albums: List<AlbumData>,
                        @SerializedName("previous") val previousPageUrl: String?,
                        @SerializedName("next") val nextPageUrl: String?,
                        @SerializedName("total") val totalItems: Int)

data class ArtistsResult(@SerializedName("items") val artists: List<ArtistData>,
                         @SerializedName("previous") val previousPageUrl: String?,
                         @SerializedName("next") val nextPageUrl: String?,
                         @SerializedName("total") val totalItems: Int)

data class PlaylistsResponse(@SerializedName("playlists") val result: PlaylistsResult)

data class PlaylistsResult(@SerializedName("items") val playlists: List<PlaylistData>,
                           @SerializedName("previous") val previousPageUrl: String?,
                           @SerializedName("next") val nextPageUrl: String?,
                           @SerializedName("total") val totalItems: Int)

data class TracksResult(@SerializedName("items") val tracks: List<TrackData>,
                        @SerializedName("previous") val previousPageUrl: String?,
                        @SerializedName("next") val nextPageUrl: String?,
                        @SerializedName("total") val totalItems: Int)

data class PlaylistTracksResponse(@SerializedName("items") val playlistTracks: List<PlaylistTrackData>,
                                  @SerializedName("previous") val previousPageUrl: String?,
                                  @SerializedName("next") val nextPageUrl: String?,
                                  @SerializedName("total") val totalItems: Int)

