package com.example.spotifyrepo.mapper

import com.example.spotifyapi.model.AccessTokenApiModel
import com.example.there.domain.entity.spotify.AccessTokenEntity

val AccessTokenApiModel.domain: AccessTokenEntity
    get() = AccessTokenEntity(
        token = accessToken,
        timestamp = System.currentTimeMillis()
    )
