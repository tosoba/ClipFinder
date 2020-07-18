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

val SpotifyAccountsApi.accessTokenHeader: Single<NetworkResponse<String, SpotifyAuthErrorApiModel>>
    get() = getAccessToken(authorization = SpotifyAuth.clientDataHeader)
        .map {
            when (it) {
                is NetworkResponse.Success -> NetworkResponse.Success("Bearer ${it.body.accessToken}")
                is NetworkResponse.ServerError -> NetworkResponse.ServerError(it.body, it.code)
                is NetworkResponse.NetworkError -> NetworkResponse.NetworkError(it.error)
                is NetworkResponse.DifferentError -> NetworkResponse.DifferentError(it.error)
            }
        }

val AccessTokenApiModel.domain: AccessTokenEntity
    get() = AccessTokenEntity(
        token = accessToken,
        timestamp = System.currentTimeMillis()
    )

