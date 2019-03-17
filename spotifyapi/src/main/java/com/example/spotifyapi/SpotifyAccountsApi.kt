package com.example.spotifyapi

import com.example.spotifyapi.model.AccessTokenApiModel
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface SpotifyAccountsApi {

    @FormUrlEncoded
    @POST("token")
    fun getAccessToken(
            @Header("Authorization") authorization: String,
            @Field("grant_type") grantType: String = "client_credentials"
    ): Single<AccessTokenApiModel>
}