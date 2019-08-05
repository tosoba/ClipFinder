package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyBrowseApi {

    @GET("browse/categories/{category_id}/playlists")
    fun getPlaylistsForCategory(
            @Header("Authorization") authorization: String,
            @Path("category_id") categoryId: String,
            @Query("offset") offset: Int,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT
    ): Single<NetworkResponse<PlaylistsResponse, ErrorResponse>>

    @GET("browse/categories")
    fun getCategories(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("locale") locale: String = SpotifyDefaults.LOCALE,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT
    ): Single<NetworkResponse<CategoriesResponse, ErrorResponse>>

    @GET("browse/categories/{category_id}")
    fun getCategory(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("locale") locale: String = SpotifyDefaults.LOCALE
    ): Single<NetworkResponse<SpotifyCategory, ErrorResponse>>

    @GET("browse/featured-playlists")
    fun getFeaturedPlaylists(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("locale") locale: String = SpotifyDefaults.LOCALE
    ): Single<NetworkResponse<PlaylistsResponse, ErrorResponse>>

    @GET("browse/new-releases")
    fun getNewReleases(
            @Header("Authorization") authorization: String,
            @Query("country") country: String = SpotifyDefaults.COUNTRY,
            @Query("limit") limit: Int = SpotifyDefaults.LIMIT,
            @Query("offset") offset: Int = SpotifyDefaults.OFFSET
    ): Single<NetworkResponse<AlbumsResponse, ErrorResponse>>

    //TODO: get recommendations
}