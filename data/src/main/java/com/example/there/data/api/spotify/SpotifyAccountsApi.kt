package com.example.there.data.api.spotify

import com.example.there.data.entity.spotify.AccessTokenData
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
    ): Single<AccessTokenData>
}