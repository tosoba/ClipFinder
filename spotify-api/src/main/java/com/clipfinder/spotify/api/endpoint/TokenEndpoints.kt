package com.clipfinder.spotify.api.endpoint

import com.clipfinder.core.model.NetworkResponse
import com.clipfinder.spotify.api.model.AuthErrorObject
import com.clipfinder.spotify.api.model.GrantType
import com.clipfinder.spotify.api.model.TokensResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenEndpoints {
    @POST("token")
    @FormUrlEncoded
    fun getTokens(
        @Header("Authorization") authorization: String? = null,
        @Field("grant_type") grantType: GrantType,
        @Field("refresh_token") refreshToken: String? = null,
        @Field("client_id") clientId: String? = null
    ): Single<NetworkResponse<TokensResponse, AuthErrorObject>>
}
