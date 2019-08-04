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

class PlaylistsResponse(
        @SerializedName("playlists") val result: PlaylistsResult
) : PagedResponse<PlaylistApiModel> {
    override val items: List<PlaylistApiModel> get() = result.items
    override val offset: Int get() = result.offset
    override val totalItems: Int get() = result.totalItems
}

interface PagedResponse<T> {
    val items: List<T>
    val offset: Int
    val totalItems: Int
}

class PlaylistTracksResponse(
        override val items: List<PlaylistTrackApiModel>,
        override val offset: Int,
        @SerializedName("total") override val totalItems: Int
) : PagedResponse<PlaylistTrackApiModel>

class NewReleasesResponse(
        @SerializedName("albums") val result: AlbumsResult
)

class AlbumsResult(
        override val items: List<AlbumApiModel>,
        override val offset: Int,
        @SerializedName("total") override val totalItems: Int
) : PagedResponse<AlbumApiModel>

class ArtistsResult(
        override val items: List<ArtistApiModel>,
        override val offset: Int,
        @SerializedName("total") override val totalItems: Int
) : PagedResponse<ArtistApiModel>

class PlaylistsResult(
        override val items: List<PlaylistApiModel>,
        override val offset: Int,
        @SerializedName("total") override val totalItems: Int
) : PagedResponse<PlaylistApiModel>

class TracksResult(
        override val items: List<TrackApiModel>,
        override val offset: Int,
        @SerializedName("total") override val totalItems: Int
) : PagedResponse<TrackApiModel>

class SavedTrack(val track: TrackApiModel)

class SavedTracksResult(
        override val items: List<SavedTrack>,
        override val offset: Int,
        @SerializedName("total") override val totalItems: Int
) : PagedResponse<SavedTrack>

class SavedAlbum(val album: AlbumApiModel)

class SavedAlbumsResult(
        override val items: List<SavedAlbum>,
        override val offset: Int,
        @SerializedName("total") override val totalItems: Int
) : PagedResponse<SavedAlbum>

class SpotifyErrorResponse(val error: SpotifyErrorApiModel)