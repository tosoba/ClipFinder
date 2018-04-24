package com.example.there.data.api

import com.example.there.data.response.CategoriesResponse
import com.example.there.data.response.PlaylistsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

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

    companion object {
        private const val DEFAULT_LIMIT = "50"
        private const val DEFAULT_OFFSET = "0"
        private const val DEFAULT_COUNTRY = "SE"
        private const val DEFAULT_LOCALE = "sv_se"
    }
}