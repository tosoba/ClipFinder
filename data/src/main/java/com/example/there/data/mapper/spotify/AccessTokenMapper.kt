package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.AccessTokenData
import com.example.there.domain.entity.spotify.AccessTokenEntity


val AccessTokenData.domain: AccessTokenEntity
    get() = AccessTokenEntity(
            token = accessToken,
            timestamp = System.currentTimeMillis()
    )