package com.example.spotifyapi

import com.example.core.SpotifyDefaults
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.CategoriesResponse
import com.example.spotifyapi.models.ErrorResponse
import com.example.spotifyapi.models.PlaylistsResponse
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
}