package com.example.there.data.mappers.spotify

import com.example.there.data.entities.spotify.AccessTokenData
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.entities.spotify.AccessTokenEntity


object AccessTokenMapper : OneWayMapper<AccessTokenData, AccessTokenEntity>() {
    override fun mapFrom(from: AccessTokenData): AccessTokenEntity = AccessTokenEntity(token = from.accessToken, timestamp = System.currentTimeMillis())
}