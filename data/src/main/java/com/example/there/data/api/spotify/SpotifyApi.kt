package com.example.there.data.api.spotify

import com.example.there.data.entity.spotify.AlbumData
import com.example.there.data.response.*
import io.reactivex.Observable
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
            @Query("offset") offset: String = DEFAULT_OFFSET,
            @Query("limit") limit: String = DEFAULT_LIMIT
    ): Observable<CategoriesResponse>

    @GET("browse/featured-playlists")
    fun getFeaturedPlaylists(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = DEFAULT_COUNTRY,
            @Query("offset") offset: String = DEFAULT_OFFSET,
            @Query("limit") limit: String = DEFAULT_LIMIT
    ): Observable<PlaylistsResponse>

    @GET("tracks")
    fun getTracks(
            @Header("Authorization") authorization: String,
            @Query("ids") ids: String
    ): Single<TracksOnlyResponse>

    @GET("search")
    fun searchAll(@Header("Authorization") authorization: String,
                  @Query("q") query: String,
                  @Query("type") type: String = ALL_SEARCH_TYPES,
                  @Query("offset") offset: String = DEFAULT_OFFSET,
                  @Query("limit") limit: String = DEFAULT_LIMIT): Observable<SearchAllResponse>

    @GET("browse/categories/{category_id}/playlists")
    fun getPlaylistsForCategory(
            @Header("Authorization") authorization: String,
            @Path("category_id") categoryId: String,
            @Query("offset") offset: String,
            @Query("country") country: String = DEFAULT_COUNTRY,
            @Query("limit") limit: String = DEFAULT_LIMIT
    ): Single<PlaylistsResponse>

    @GET("users/{user_id}/playlists/{playlist_id}/tracks")
    fun getPlaylistTracks(
            @Header("Authorization") authorization: String,
            @Path("user_id") userId: String,
            @Path("playlist_id") playlistId: String,
            @Query("offset") offset: String
    ): Single<PlaylistTracksResponse>

    @GET("artists")
    fun getArtists(
            @Header("Authorization") authorization: String,
            @Query("ids") artistIds: String
    ): Single<ArtistsOnlyResponse>

    @GET("albums/{id}")
    fun getAlbum(
            @Header("Authorization") authorization: String,
            @Path("id") albumId: String
    ): Single<AlbumData>

    @GET("recommendations")
    fun getSimilarTracks(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: String = DEFAULT_TRACKS_LIMIT,
            @Query("seed_tracks") trackId: String
    ): Observable<SimilarTracksResponse>

    @GET("artists/{id}/albums")
    fun getAlbumsFromArtist(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String,
            @Query("limit") limit: String = DEFAULT_LIMIT,
            @Query("offset") offset: String = DEFAULT_OFFSET
    ): Observable<AlbumsResult>

    @GET("artists/{id}/top-tracks")
    fun getTopTracksFromArtist(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String,
            @Query("country") country: String = DEFAULT_COUNTRY
    ): Single<TracksOnlyResponse>

    @GET("artists/{id}/related-artists")
    fun getRelatedArtists(
            @Header("Authorization") authorization: String,
            @Path("id") artistId: String
    ): Single<ArtistsOnlyResponse>

    @GET("albums/{id}/tracks")
    fun getTracksFromAlbum(
            @Header("Authorization") authorization: String,
            @Path("id") albumId: String,
            @Query("limit") limit: String = DEFAULT_LIMIT,
            @Query("offset") offset: String = DEFAULT_OFFSET
    ): Single<TracksResult>

    @GET("browse/new-releases")
    fun getNewReleases(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: String = DEFAULT_LIMIT,
            @Query("offset") offset: String = DEFAULT_OFFSET
    ): Single<NewReleasesResponse>

    @GET("me/playlists")
    fun getCurrentUsersPlaylists(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: String = DEFAULT_LIMIT,
            @Query("offset") offset: String = DEFAULT_OFFSET
    ): Single<PlaylistsResult>

    companion object {
        const val DEFAULT_LIMIT = "50"
        const val DEFAULT_TRACKS_LIMIT = "100"

        private const val DEFAULT_OFFSET = "0"
        private const val DEFAULT_COUNTRY = "US"
        private const val DEFAULT_LOCALE = "en_us"

        private const val ALL_SEARCH_TYPES = "album,artist,playlist,track"
    }
}