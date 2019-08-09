package com.example.spotifyapi

import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.models.ErrorResponse
import com.example.spotifyapi.models.SpotifyUserInformation
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpotifyUsersApi {
    @GET("me")
    fun getCurrentUser(
            @Header("Authorization") authorization: String
    ): Single<NetworkResponse<SpotifyUserInformation, ErrorResponse>>

    @GET("users/{user_id}")
    fun getUser(
            @Header("Authorization") authorization: String,
            @Path("user_id") userId: String
    ): Single<NetworkResponse<SpotifyUserInformation, ErrorResponse>>
}