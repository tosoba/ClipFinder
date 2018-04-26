package com.example.there.data.api.spotify

import com.example.there.data.entities.spotify.TrackData
import com.example.there.data.response.spotify.CategoriesResponse
import com.example.there.data.response.spotify.PlaylistsResponse
import com.example.there.data.response.spotify.TracksResponse
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
                             @Query("limit") limit: String = DEFAULT_LIMIT): Observable<PlaylistsResponse>

    @GET("tracks/{id}")
    fun getTrack(@Header("Authorization") authorization: String,
                 @Path("id") id: String): Observable<TrackData>

    @GET("tracks")
    fun getTracks(@Header("Authorization") authorization: String,
                  @Query("ids") ids: String): Observable<TracksResponse>

    companion object {
        private const val DEFAULT_LIMIT = "50"
        private const val DEFAULT_OFFSET = "0"
        private const val DEFAULT_COUNTRY = "SE"
        private const val DEFAULT_LOCALE = "sv_se"
    }
}