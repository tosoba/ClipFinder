package com.example.there.data.api.spotify

import com.example.there.data.entities.spotify.TrackData
import com.example.there.data.response.spotify.*
import io.reactivex.Observable
import retrofit2.http.*

interface SpotifyApi {

    @GET("browse/categories")
    fun getCategories(@Header("Authorization") authorization: String,
                      @Query("country") country: String = DEFAULT_COUNTRY,
                      @Query("locale") locale: String = DEFAULT_LOCALE,
                      @Query("offset") offset: String = DEFAULT_OFFSET,
                      @Query("limit") limit: String = DEFAULT_LIMIT): Observable<CategoriesResponse>

    @GET("browse/featured-playlists")
    fun getFeaturedPlaylists(@Header("Authorization") authorization: String,
                             @Query("country") country: String = DEFAULT_COUNTRY,
                             @Query("offset") offset: String = DEFAULT_OFFSET,
                             @Query("limit") limit: String = DEFAULT_LIMIT): Observable<FeaturedPlaylistsResponse>

    @GET("tracks/{id}")
    fun getTrack(@Header("Authorization") authorization: String,
                 @Path("id") id: String): Observable<TrackData>

    @GET("tracks")
    fun getTracks(@Header("Authorization") authorization: String,
                  @Query("ids") ids: String): Observable<TracksOnlyResponse>

    @GET("search")
    fun searchAll(@Header("Authorization") authorization: String,
                  @Query("q") query: String,
                  @Query("type") type: String = ALL_SEARCH_TYPES,
                  @Query("offset") offset: String = DEFAULT_OFFSET,
                  @Query("limit") limit: String = DEFAULT_LIMIT): Observable<SearchAllResponse>

    companion object {
        private const val DEFAULT_LIMIT = "50"
        private const val DEFAULT_OFFSET = "0"
        private const val DEFAULT_COUNTRY = "SE"
        private const val DEFAULT_LOCALE = "sv_se"

        private const val ALL_SEARCH_TYPES = "album,artist,playlist,track"
    }
}