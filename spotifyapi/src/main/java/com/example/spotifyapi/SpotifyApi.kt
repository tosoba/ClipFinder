package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApi {

    @GET("tracks")
    fun getTracks(
            @Header("Authorization") authorization: String,
            @Query("ids") ids: String
    ): Single<NetworkResponse<TracksOnlyResponse, SpotifyErrorResponse>>

    @GET("search")
    fun searchAll(
            @Header("Authorization") authorization: String,
            @Query("q") query: String,
            @Query("type") type: String = ALL_SEARCH_TYPES,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT
    ): Single<NetworkResponse<SearchAllResponse, SpotifyErrorResponse>>

    @GET("browse/categories/{category_id}/playlists")
    fun getPlaylistsForCategory(
            @Header("Authorization") authorization: String,
            @Path("category_id") categoryId: String,
            @Query("offset") offset: Int,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT
    ): Single<NetworkResponse<PlaylistsResponse, SpotifyErrorResponse>>

    @GET("users/{user_id}/playlists/{playlist_id}/tracks")
    fun getPlaylistTracks(
            @Header("Authorization") authorization: String,
            @Path("user_id") userId: String,
            @Path("playlist_id") playlistId: String,
            @Query("offset") offset: Int
    ): Single<NetworkResponse<PlaylistTracksResponse, SpotifyErrorResponse>>

    @GET("artists")
    fun getArtists(
            @Header("Authorization") authorization: String,
            @Query("ids") artistIds: String
    ): Single<NetworkResponse<ArtistsOnlyResponse, SpotifyErrorResponse>>

    @GET("items/{id}")
    fun getAlbum(
            @Header("Authorization") authorization: String,
            @Path("id") albumId: String
    ): Single<NetworkResponse<AlbumApiModel, SpotifyErrorResponse>>

    @GET("recommendations")
    fun getSimilarTracks(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.TRACKS_LIMIT,
            @Query("seed_tracks") trackId: String
    ): Single<NetworkResponse<SimilarTracksResponse, SpotifyErrorResponse>>

    @GET("artists/{id}/items")
    fun getAlbumsFromArtist(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<AlbumsResult, SpotifyErrorResponse>>

    @GET("artists/{id}/top-tracks")
    fun getTopTracksFromArtist(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY
    ): Single<NetworkResponse<TracksOnlyResponse, SpotifyErrorResponse>>

    @GET("artists/{id}/related-artists")
    fun getRelatedArtists(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String
    ): Single<NetworkResponse<ArtistsOnlyResponse, SpotifyErrorResponse>>

    @GET("items/{id}/tracks")
    fun getTracksFromAlbum(
            @Header("Authorization") authorization: String,
            @Path("id") albumId: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<TracksResult, SpotifyErrorResponse>>

    @GET("me/playlists")
    fun getCurrentUsersPlaylists(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<PlaylistsResult, SpotifyErrorResponse>>

    @GET("me/top/tracks")
    fun getCurrentUsersTopTracks(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<TracksResult, SpotifyErrorResponse>>

    @GET("me/top/artists")
    fun getCurrentUsersTopArtists(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<ArtistsResult, SpotifyErrorResponse>>

    @GET("me/tracks")
    fun getCurrentUsersSavedTracks(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<SavedTracksResult, SpotifyErrorResponse>>

    @GET("me/items")
    fun getCurrentUsersSavedAlbums(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<SavedAlbumsResult, SpotifyErrorResponse>>

    @GET("me")
    fun getCurrentUser(@Header("Authorization") authorization: String): Single<NetworkResponse<UserApiModel, SpotifyErrorResponse>>

    @GET("audio-features/{id}")
    fun getAudioFeatures(
            @Header("Authorization") authorization: String,
            @Path("id") trackId: String
    ): Single<NetworkResponse<AudioFeaturesApiModel, SpotifyErrorResponse>>

    companion object {
        private const val ALL_SEARCH_TYPES = "album,artist,playlist,track"
    }
}