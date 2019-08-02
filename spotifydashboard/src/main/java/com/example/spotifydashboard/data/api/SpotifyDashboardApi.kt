package com.example.spotifydashboard.data.api

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.model.NewReleasesResponse
import com.example.spotifyapi.model.PlaylistsResponse
import com.example.spotifyapi.model.SpotifyErrorResponse
import com.example.spotifydashboard.data.api.model.CategoriesApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyDashboardApi {

    @GET("browse/categories")
    fun getCategories(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("locale") locale: String = SpotifyDefaults.LOCALE,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT
    ): Single<NetworkResponse<CategoriesApiResponse, SpotifyErrorResponse>>

    @GET("browse/featured-playlists")
    fun getFeaturedPlaylists(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("locale") locale: String = SpotifyDefaults.LOCALE
    ): Single<NetworkResponse<PlaylistsResponse, SpotifyErrorResponse>>

    @GET("browse/new-releases")
    fun getNewReleases(
            @Header("Authorization") authorization: String,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<NewReleasesResponse, SpotifyErrorResponse>>
}