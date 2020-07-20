package com.example.core.android.spotify.api.ext

import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.retrofit.NetworkResponse
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.model.AccessTokenApiModel
import com.example.spotifyapi.model.SpotifyAuthErrorApiModel
import com.example.there.domain.entity.spotify.AccessTokenEntity
import io.reactivex.Single

val SpotifyAccountsApi.accessToken: Single<NetworkResponse<AccessTokenApiModel, SpotifyAuthErrorApiModel>>
    get() = getAccessToken(authorization = SpotifyAuth.clientDataHeader)

val AccessTokenApiModel.domain: AccessTokenEntity
    get() = AccessTokenEntity(
        token = accessToken,
        timestamp = System.currentTimeMillis()
    )
