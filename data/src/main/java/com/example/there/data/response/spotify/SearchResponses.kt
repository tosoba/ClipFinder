package com.example.there.data.response.spotify

import com.example.there.data.entities.spotify.AlbumData
import com.example.there.data.entities.spotify.ArtistData
import com.example.there.data.entities.spotify.PlaylistData
import com.example.there.data.entities.spotify.TrackData
import com.google.gson.annotations.SerializedName

data class SearchAllResponse(
        @SerializedName("albums") val albumsResponse: AlbumsResponse?,
        @SerializedName("artists") val artistsResponse: ArtistsResponse?,
        @SerializedName("playlists") val playlistsResponse: PlaylistsResponse?,
        @SerializedName("tracks") val tracksResponse: TracksResponse?
)

data class AlbumsResponse(@SerializedName("items") val albums: List<AlbumData>,
                          @SerializedName("previous") val previousPageUrl: String?,
                          @SerializedName("next") val nextPageUrl: String?,
                          @SerializedName("total") val totalItems: Int)

data class ArtistsResponse(@SerializedName("items") val artists: List<ArtistData>,
                           @SerializedName("previous") val previousPageUrl: String?,
                           @SerializedName("next") val nextPageUrl: String?,
                           @SerializedName("total") val totalItems: Int)

data class PlaylistsResponse(@SerializedName("items") val playlists: List<PlaylistData>,
                             @SerializedName("previous") val previousPageUrl: String?,
                             @SerializedName("next") val nextPageUrl: String?,
                             @SerializedName("total") val totalItems: Int)

data class TracksResponse(@SerializedName("items") val tracks: List<TrackData>,
                          @SerializedName("previous") val previousPageUrl: String?,
                          @SerializedName("next") val nextPageUrl: String?,
                          @SerializedName("total") val totalItems: Int)