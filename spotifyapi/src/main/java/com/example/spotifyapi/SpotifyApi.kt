package com.example.spotifyapi

import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApi {

    @GET("browse/categories")
    fun getCategories(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = DEFAULT_COUNTRY,
            @Query("locale") locale: String = DEFAULT_LOCALE,
            @Query("offset") offset: Int = DEFAULT_OFFSET,
            @Query("limit") limit: Int = DEFAULT_LIMIT
    ): Single<NetworkResponse<CategoriesResponse, SpotifyErrorResponse>>

    @GET("browse/featured-playlists")
    fun getFeaturedPlaylists(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = DEFAULT_COUNTRY,
            @Query("offset") offset: Int = DEFAULT_OFFSET,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("locale") locale: String = DEFAULT_LOCALE
    ): Single<NetworkResponse<PlaylistsResponse, SpotifyErrorResponse>>

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
            @Query("offset") offset: Int = DEFAULT_OFFSET,
            @Query("limit") limit: Int = DEFAULT_LIMIT
    ): Single<NetworkResponse<SearchAllResponse, SpotifyErrorResponse>>

    @GET("browse/categories/{category_id}/playlists")
    fun getPlaylistsForCategory(
            @Header("Authorization") authorization: String,
            @Path("category_id") categoryId: String,
            @Query("offset") offset: Int,
            @Query("country") country: String = DEFAULT_COUNTRY,
            @Query("limit") limit: Int = DEFAULT_LIMIT
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

    @GET("albums/{id}")
    fun getAlbum(
            @Header("Authorization") authorization: String,
            @Path("id") albumId: String
    ): Single<NetworkResponse<AlbumApiModel, SpotifyErrorResponse>>

    @GET("recommendations")
    fun getSimilarTracks(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = DEFAULT_TRACKS_LIMIT,
            @Query("seed_tracks") trackId: String
    ): Single<NetworkResponse<SimilarTracksResponse, SpotifyErrorResponse>>

    @GET("artists/{id}/albums")
    fun getAlbumsFromArtist(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("offset") offset: Int = DEFAULT_OFFSET
    ): Single<NetworkResponse<AlbumsResult, SpotifyErrorResponse>>

    @GET("artists/{id}/top-tracks")
    fun getTopTracksFromArtist(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String,
            @Query("country") country: String = DEFAULT_COUNTRY
    ): Single<NetworkResponse<TracksOnlyResponse, SpotifyErrorResponse>>

    @GET("artists/{id}/related-artists")
    fun getRelatedArtists(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String
    ): Single<NetworkResponse<ArtistsOnlyResponse, SpotifyErrorResponse>>

    @GET("albums/{id}/tracks")
    fun getTracksFromAlbum(
            @Header("Authorization") authorization: String,
            @Path("id") albumId: String,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("offset") offset: Int = DEFAULT_OFFSET
    ): Single<NetworkResponse<TracksResult, SpotifyErrorResponse>>

    @GET("browse/new-releases")
    fun getNewReleases(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("offset") offset: Int = DEFAULT_OFFSET
    ): Single<NetworkResponse<NewReleasesResponse, SpotifyErrorResponse>>

    @GET("me/playlists")
    fun getCurrentUsersPlaylists(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("offset") offset: Int = DEFAULT_OFFSET
    ): Single<NetworkResponse<PlaylistsResult, SpotifyErrorResponse>>

    @GET("me/top/tracks")
    fun getCurrentUsersTopTracks(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("offset") offset: Int = DEFAULT_OFFSET
    ): Single<NetworkResponse<TracksResult, SpotifyErrorResponse>>

    @GET("me/top/artists")
    fun getCurrentUsersTopArtists(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("offset") offset: Int = DEFAULT_OFFSET
    ): Single<NetworkResponse<ArtistsResult, SpotifyErrorResponse>>

    @GET("me/tracks")
    fun getCurrentUsersSavedTracks(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("offset") offset: Int = DEFAULT_OFFSET
    ): Single<NetworkResponse<SavedTracksResult, SpotifyErrorResponse>>

    @GET("me/albums")
    fun getCurrentUsersSavedAlbums(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = DEFAULT_LIMIT,
            @Query("offset") offset: Int = DEFAULT_OFFSET
    ): Single<NetworkResponse<SavedAlbumsResult, SpotifyErrorResponse>>

    @GET("me")
    fun getCurrentUser(@Header("Authorization") authorization: String): Single<NetworkResponse<UserApiModel, SpotifyErrorResponse>>

    @GET("audio-features/{id}")
    fun getAudioFeatures(
            @Header("Authorization") authorization: String,
            @Path("id") trackId: String
    ): Single<NetworkResponse<AudioFeaturesApiModel, SpotifyErrorResponse>>

    companion object {
        const val DEFAULT_LIMIT = 50
        const val DEFAULT_TRACKS_LIMIT = 100

        private const val DEFAULT_OFFSET = 0
        const val DEFAULT_COUNTRY = "US"
        const val DEFAULT_LOCALE = "en_us"

        private const val ALL_SEARCH_TYPES = "album,artist,playlist,track"
    }
}