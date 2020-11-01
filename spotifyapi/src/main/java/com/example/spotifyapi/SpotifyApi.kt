package com.example.spotifyapi

import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.model.SpotifyErrorResponse
import com.example.spotifyapi.model.UserApiModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyApi {
    @GET("me")
    fun getCurrentUser(@Header("Authorization") authorization: String): Single<NetworkResponse<UserApiModel, SpotifyErrorResponse>>
}
