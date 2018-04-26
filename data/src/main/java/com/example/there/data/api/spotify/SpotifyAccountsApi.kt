package com.example.there.data.api.spotify

import com.example.there.data.entities.spotify.AccessTokenData
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface SpotifyAccountsApi {

    @FormUrlEncoded
    @POST("token")
    fun getAccessToken(@Header("Authorization") authorization: String,
                       @Field("grant_type") grantType: String = "client_credentials"): Observable<AccessTokenData>
}