package com.clipfinder.spotify.api.endpoint

import com.clipfinder.spotify.api.model.GrantType
import com.clipfinder.spotify.api.model.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthEndpoints {
    @POST("/token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("grant_type") grantType: GrantType,
        @Field("refresh_token") refreshToken: String?,
        @Field("client_id") clientId: String
    ): Call<RefreshTokenResponse>
}